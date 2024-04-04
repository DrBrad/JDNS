package unet.dns.utils;

import unet.dns.messages.MessageBase;

public interface ResponseCallback {

    void onResponse(MessageBase response);
}
