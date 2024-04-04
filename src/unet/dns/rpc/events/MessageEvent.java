package unet.dns.rpc.events;

import unet.dns.messages.MessageBase;

public class MessageEvent extends Event {

    protected MessageBase message;
    protected long receivedTime;

    public MessageEvent(MessageBase message){
        this.message = message;
    }

    public MessageBase getMessage(){
        return message;
    }

    public void setReceivedTime(long receivedTime){
        this.receivedTime = receivedTime;
    }

    public long getReceivedTime(){
        return receivedTime;
    }

    public void received(){
        receivedTime = System.currentTimeMillis();
    }
}
