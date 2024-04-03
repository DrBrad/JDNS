package unet.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class DnsServer {

    private DatagramSocket server;
    private List<InetSocketAddress> fallback;

    public DnsServer(){
        fallback = new ArrayList<>();
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
                        DatagramPacket packet = new DatagramPacket(new byte[65535], 65535);
                        server.receive(packet);

                        if(packet != null){
                            /*
                            new Thread(new Runnable(){
                                @Override
                                public void run(){
                                    onReceive(packet);
                                    tracker.removeStalled();
                                }
                            }).start();
                            */
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

    public boolean containsFallBack(InetSocketAddress address){
        return fallback.contains(address);
    }

    public void addFallBack(InetSocketAddress address){
        fallback.add(address);
    }

    public InetSocketAddress getFallBack(int i){
        return fallback.get(i);
    }
}
