package unet.dns.utils;

import unet.dns.messages.DnsResponse;

public interface ResponseCallback {

    void onResponse(DnsResponse response);
}
