package unet.dns.events;

import unet.dns.events.inter.MessageEvent;
import unet.dns.messages.MessageBase;

public class ErrorResponseEvent extends MessageEvent {

    private MessageBase request;
    private long sentTime;

    public ErrorResponseEvent(MessageBase message){
        super(message);
    }

    public boolean hasRequest(){
        return (request != null);
    }

    public void setRequest(MessageBase request){
        this.request = request;
    }

    public MessageBase getRequest(){
        return request;
    }

    public void setSentTime(long sentTime){
        this.sentTime = sentTime;
    }

    public long getSentTime(){
        return sentTime;
    }
}
