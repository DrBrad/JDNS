package unet.dns.messages;

import unet.dns.messages.inter.MessageBase;
import unet.dns.utils.DnsQuery;

import java.nio.charset.StandardCharsets;

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

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();
        return buf;

        /*
        int offset = 12;

        for(DnsQuery query : queries){
            byte[] q = query.encode();
            System.arraycopy(q, 0, buf, offset, q.length);
            offset += q.length;
        }

        return buf;

/*
        for(String part : query.split("\\.")){
            byte[] domainBytes = part.getBytes(StandardCharsets.UTF_8);
            buf[offset] = (byte) domainBytes.length;
            System.arraycopy(domainBytes, 0, buf, offset+1, domainBytes.length);
            offset += domainBytes.length+1;
        }


        // End of domain name (null)
        buf[offset] = 0x00;

        // QTYPE (16 bits) - A record
        buf[offset+1] = ((byte) (type.getCode() >> 8));
        buf[offset+2] = ((byte) type.getCode());

        // QCLASS (16 bits) - IN class
        buf[offset+3] = ((byte) (dnsClass.getCode() >> 8));
        buf[offset+4] = ((byte) dnsClass.getCode());

        // Truncate unused portion of the byte array
        byte[] truncatedFrame = new byte[offset+5];
        System.arraycopy(buf, 0, truncatedFrame, 0, offset+5);

        return truncatedFrame;*/
    }

    @Override
    public void decode(byte[] buf){
    }
}
