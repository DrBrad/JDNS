package unet.dns;

import unet.dns.DnsClient;
import unet.dns.rpc.events.StalledEvent;
import unet.dns.utils.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResponseTracker {

    public static final int MAX_ACTIVE_CALLS = 512;

    public static final long STALLED_TIME = 1000;
    private DnsClient client;
    private final LinkedHashMap<Integer, Call> calls;

    public ResponseTracker(DnsClient client){
        this.client = client;
        calls = new LinkedHashMap<>(MAX_ACTIVE_CALLS);
    }

    public synchronized void add(Integer id, Call call){
        calls.put(id, call);
    }

    public synchronized Call get(Integer id){
        return calls.get(id);
    }

    public synchronized boolean contains(Integer id){
        return calls.containsKey(id);
    }

    public synchronized void remove(Integer id){
        calls.remove(id);
    }

    public synchronized Call poll(Integer id){
        Call call = calls.get(id);
        calls.remove(id);
        return call;
    }

    public synchronized void removeStalled(){
        long now = System.currentTimeMillis();

        List<Integer> stalled = new ArrayList<>();

        for(Integer id : calls.keySet()){
            if(!calls.get(id).isStalled(now)){
                break;
            }

            stalled.add(id);
        }

        for(Integer id : stalled){
            Call call = calls.get(id);

            System.out.println("STALLED");

            if(call.getStaleCount() < client.servers.size()){
                System.out.println("RETRY");
                try{
                    call.fallBack();
                    call.getMessage().setDestination(client.servers.get(call.getStaleCount()));
                    client.send(call.getMessage());

                }catch(IOException e){
                }
                continue;
            }

            calls.remove(call);

            StalledEvent event = new StalledEvent(call.getMessage());
            event.setSentTime(call.getSentTime());

            call.getResponseCallback().onStalled(event);

        }
    }
}
