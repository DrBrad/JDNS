package unet.dns;

import unet.dns.events.StalledEvent;
import unet.dns.utils.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResponseTracker {

    public static final int MAX_ACTIVE_CALLS = 512;

    public static final long STALLED_TIME = 1000;
    private DnsServer server;
    private final LinkedHashMap<Integer, Call> calls;

    public ResponseTracker(DnsServer server){
        this.server = server;
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
            call.fallBack();

            if(call.getStaleCount() < server.servers.size()){
                try{
                    call.getMessage().setDestination(server.servers.get(call.getStaleCount()));
                    server.send(call.getMessage());

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
