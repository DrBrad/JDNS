package unet.dns.utils;

import unet.dns.messages.MessageBase;

import static unet.dns.utils.ResponseTracker.STALLED_TIME;

public class Call {

    private MessageBase message;
    private ResponseCallback callback;
    private long sentTime;

    public Call(MessageBase message, ResponseCallback callback){
        this.message = message;
        this.callback = callback;
        sentTime = System.currentTimeMillis();
    }

    public MessageBase getMessage(){
        return message;
    }

    public boolean hasResponseCallback(){
        return (callback != null);
    }

    public ResponseCallback getResponseCallback(){
        return callback;
    }

    public void setResponseCallback(ResponseCallback callback){
        this.callback = callback;
    }

    public void setSentTime(long sentTime){
        this.sentTime = sentTime;
    }

    public long getSentTime(){
        return sentTime;
    }

    public boolean isStalled(long now){
        return (now-sentTime > STALLED_TIME);
    }
}
