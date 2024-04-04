package unet.dns.events;

import unet.dns.messages.MessageBase;
import unet.dns.events.inter.MessageEvent;

public class StalledEvent extends MessageEvent {

    private long sentTime;

    public StalledEvent(MessageBase message){
        super(message);
    }

    public void setSentTime(long sentTime){
        this.sentTime = sentTime;
    }

    public long getSentTime(){
        return sentTime;
    }
}
