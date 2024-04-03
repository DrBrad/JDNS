package unet.dns.messages;

import unet.dns.messages.inter.MessageBase;

public class DnsRequest extends MessageBase {

    public DnsRequest(){
        super();
    }

    public DnsRequest(int id){
        super();
        this.id = id;
        //qdCount = 1;
        recursionDesired = true;
    }
}
