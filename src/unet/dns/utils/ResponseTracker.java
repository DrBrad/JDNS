package unet.dns.utils;

import unet.dns.DnsClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResponseTracker {

    public static final int MAX_ACTIVE_CALLS = 512;

    public static final long STALLED_TIME = 10000;
    private final LinkedHashMap<Integer, Call> calls;

    public ResponseTracker(){
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
            calls.remove(id);

            /*
            if(call.getStaleCount() > 1){
                calls.remove(id);

                /*
                if(call.hasResponseCallback()){
                    StalledEvent event = new StalledEvent(call.getMessage());
                    event.setSentTime(call.getSentTime());

                    if(call.hasNode()){
                        event.setNode(call.getNode());
                    }

                    call.getResponseCallback().onStalled(event);
                }
                *./
                return;
            }

            call.fallBack();*/
        }
    }
}
