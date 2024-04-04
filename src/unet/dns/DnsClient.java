package unet.dns;

import unet.dns.messages.MessageBase;
import unet.dns.utils.Call;
import unet.dns.utils.ResponseCallback;
import unet.dns.utils.ResponseTracker;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DnsClient {

    private DatagramSocket server;
    private ResponseTracker tracker;
    private Random random;
    private List<InetSocketAddress> servers;

    public DnsClient(){
        servers = new ArrayList<>();
        tracker = new ResponseTracker();
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

                        if(packet != null){
                            new Thread(new Runnable(){
                                @Override
                                public void run(){
                                    onReceive(packet);
                                    tracker.removeStalled();
                                }
                            }).start();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
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


            call.getResponseCallback().onResponse(message);

        }else{
            System.out.println("REQUEST");
        }

    }

    public void send(MessageBase message, ResponseCallback callback)throws IOException {
        if(!message.isQR()){
            int id = random.nextInt(32767);
            message.setID(id);
            if(message.getDestination() == null){
                message.setDestination(servers.get(0));
            }

            tracker.add(id, new Call(message, callback));
        }

        byte[] buf = message.encode();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, message.getDestinationAddress(), message.getDestinationPort());
        server.send(packet);
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
}
