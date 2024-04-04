package unet.dns.rpc.events;

import unet.dns.messages.MessageBase;

public class RequestEvent extends MessageEvent {

    private MessageBase response;

    public RequestEvent(MessageBase message){
        super(message);
    }

    public boolean hasResponse(){
        return (response != null);
    }

    public MessageBase getResponse(){
        return response;
    }

    public void setResponse(MessageBase message){
        response = message;
    }

    //public MessageBase getResponse(){
    //    return response;
    //}

    /*
    public RequestEvent(MessageBase message, MessageCallback callback){
        this.callback = callback;
    }
    */
}
