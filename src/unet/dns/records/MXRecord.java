package unet.dns.records;

import unet.dns.messages.inter.Types;
import unet.dns.utils.DomainUtils;
import unet.dns.records.inter.DnsRecord;

public class MXRecord extends DnsRecord {

    private int priority;
    private String domain;

    public MXRecord(){
        type = Types.MX;
    }

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        super.decode(buf, off);

        priority = ((buf[off+8] & 0xFF) << 8) | (buf[off+9] & 0xFF);
        domain = DomainUtils.unpackDomain(buf, off+10);
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain(){
        return domain;
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nPRIORITY: "+priority+"\r\nDOMAIN: "+domain;
    }
}
