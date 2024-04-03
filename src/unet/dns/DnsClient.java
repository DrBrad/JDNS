package unet.dns;

import unet.dns.messages.DnsResponse;
import unet.dns.messages.inter.MessageBase;
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

        Call call = tracker.poll(id);
        System.out.println("QR "+id);

        if(call == null){
            return; //THROW..
        }

        DnsResponse response = new DnsResponse(id);
        response.decode(buf);

        call.getResponseCallback().onResponse(response);
    }

    public void send(MessageBase message, ResponseCallback callback)throws IOException {
        if(!message.isQR()){
            int id = random.nextInt(32767);
            message.setID(id);
            tracker.add(id, new Call(message, callback));
            System.out.println(id);
        }

        byte[] buf = message.encode();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, servers.get(0).getAddress(), servers.get(0).getPort());
        server.send(packet);

        //
        //STALE HANDLER ADD AND CALLBACK
        //
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
