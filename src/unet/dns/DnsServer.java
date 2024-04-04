package unet.dns;

import unet.dns.messages.MessageBase;
import unet.dns.events.ResponseEvent;
import unet.dns.utils.Call;
import unet.dns.utils.ResponseCallback;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DnsServer {

    private DatagramSocket server;
    private ResponseTracker tracker;
    private Random random;
    protected List<InetSocketAddress> servers;
    private ConcurrentLinkedQueue<DatagramPacket> sendPool;

    public DnsServer(){
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

        if(message.isQR()){
            Call call = tracker.poll(id);

            if(call == null){
                throw new IllegalArgumentException("Packet is invalid");
            }

            ResponseEvent event = new ResponseEvent(message);
            event.received();
            event.setSentTime(call.getSentTime());
            event.setRequest(call.getMessage());

            call.getResponseCallback().onResponse(event);

        }else{
            System.out.println("REQUEST");
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
