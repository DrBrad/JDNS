package unet.dns.utils;

import unet.dns.events.ErrorResponseEvent;
import unet.dns.events.ResponseEvent;
import unet.dns.events.StalledEvent;

public abstract class ResponseCallback {

    public abstract void onResponse(ResponseEvent event);

    public void onErrorResponse(ErrorResponseEvent event){
    }

    //public void onException(MessageException exception){
    //}

    public void onStalled(StalledEvent event){
    }
}
