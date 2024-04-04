package unet.dns;

import unet.dns.messages.MessageBase;
import unet.dns.events.ResponseEvent;
import unet.dns.messages.inter.DnsClass;
import unet.dns.records.ARecord;
import unet.dns.records.CNameRecord;
import unet.dns.records.MXRecord;
import unet.dns.records.TXTRecord;
import unet.dns.records.inter.DnsRecord;
import unet.dns.store.RecordStore;
import unet.dns.utils.Call;
import unet.dns.utils.DnsQuery;
import unet.dns.utils.ResponseCallback;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DnsServer {

    private DatagramSocket server;
    private RecordStore store;
    private ResponseTracker tracker;
    private Random random;
    protected List<InetSocketAddress> servers;
    private ConcurrentLinkedQueue<DatagramPacket> sendPool;

    public DnsServer(){
        store = new RecordStore();
        servers = new ArrayList<>();
        sendPool = new ConcurrentLinkedQueue<>();
        tracker = new ResponseTracker(this);
        random = new Random();
    }

    public void start(int port)throws SocketException {
        if(isRunning()){
            throw new IllegalArgumentException("DnsClient has already started.");
        }

        server = new DatagramSocket(port);

        new Thread(new Runnable(){
            @Override
            public void run(){
                while(!server.isClosed()){
                    try{
                        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                        server.receive(packet);
                        sendPool.offer(packet);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable(){
            @Override
            public void run(){
                while(!server.isClosed()){
                    tracker.removeStalled();

                    DatagramPacket packet = sendPool.poll();
                    if(packet != null){
                        onReceive(packet);
                    }
                }
            }
        }).start();
    }

    public void stop(){
        if(!isRunning()){
            throw new IllegalArgumentException("DnsClient is not currently running.");
        }
        server.close();
    }

    public boolean isRunning(){
        return (server != null && !server.isClosed());
    }

    public int getPort(){
        return server.getPort();
    }

    public void onReceive(DatagramPacket packet){
        byte[] buf = packet.getData();

        int id = ((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF);

        MessageBase message = new MessageBase(id);
        message.decode(buf);
        message.setOrigin(packet.getAddress(), packet.getPort());

        try{
            if(message.isQR()){
                Call call = tracker.poll(id);

                if(call == null){
                    throw new IOException("Packet is invalid");
                }

                ResponseEvent event = new ResponseEvent(message);
                event.received();
                event.setSentTime(call.getSentTime());
                event.setRequest(call.getMessage());

                call.getResponseCallback().onResponse(event);

            }else{
                System.out.println("REQUEST");


                MessageBase response = new MessageBase();
                response.setID(id);
                response.setQR(true);
                response.setDestination(message.getOrigin());
                //response.addQuery(message.getQueries().get(0));

                for(DnsQuery query : message.getQueries()){
                    if(store.hasAnswers(query)){
                        response.addQuery(query);

                        for(DnsRecord record : store.getRecord(query)){
                            response.addAnswer(record);
                        }
                    }
                }

                try{
                    send(response);
                }catch(IOException e){
                    e.printStackTrace();
                }


                //for(DnsQuery q : message.getQueries()){
                //    System.out.println(q);
                //    System.out.println();
                //}

                /*
                MessageBase response = new MessageBase();
                response.setID(id);
                response.setQR(true);
                response.setDestination(message.getOrigin());
                response.addQuery(message.getQueries().get(0));
                try{
                    response.addAnswer(new ARecord(message.getQueries().get(0).getQuery(), DnsClass.IN, 300, InetAddress.getByName("127.0.0.1")));
                    //response.addAnswer(new MXRecord(message.getQueries().get(0).getQuery(), DnsClass.IN, 300, 10, "hello.com"));

                    //response.addAnswer(new CNameRecord(message.getQueries().get(0).getQuery(), DnsClass.IN, 300, "hello.com"));
                    //response.addAnswer(new TXTRecord(message.getQueries().get(0).getQuery(), DnsClass.IN, 300, "THIS IS A TEST"));

                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    send(response);
                }catch(IOException e){
                    e.printStackTrace();
                }


                /*
                send(message, new ResponseCallback(){
                    @Override
                    public void onResponse(ResponseEvent event){
                        MessageBase response = event.getMessage();
                        System.out.println(response.getQueries().size());
                        response.setID(id);
                        response.setDestination(message.getOrigin());

                        /*
                        try{
                            send(response);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        */
                //    }
                //});


            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void send(MessageBase message)throws IOException {
        byte[] buf = message.encode();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, message.getDestinationAddress(), message.getDestinationPort());
        server.send(packet);
    }

    public void send(MessageBase message, ResponseCallback callback)throws IOException {
        if(!message.isQR()){
            int id = random.nextInt(32767);
            message.setID(id);

            message.setDestination(servers.get(0));

            tracker.add(id, new Call(message, callback));
        }

        send(message);
    }

    public RecordStore getRecordStore(){
        return store;
    }

    public boolean containsServer(InetSocketAddress address){
        return servers.contains(address);
    }

    public void addServer(InetSocketAddress address){
        servers.add(address);
    }

    public InetSocketAddress getServer(int i){
        return servers.get(i);
    }

    public List<InetSocketAddress> getServers(){
        return servers;
    }
}
