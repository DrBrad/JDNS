package unet.dns.records;

import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;
import unet.dns.utils.DomainUtils;

public class NSRecord extends DnsRecord {

    private int priority;
    private String domain;

    public NSRecord(){
        type = Types.NS;
    }

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        super.decode(buf, off);

        domain = DomainUtils.unpackDomain(buf, off+8);
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
