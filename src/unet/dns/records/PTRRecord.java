package unet.dns.records;

import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;
import unet.dns.utils.DomainUtils;

public class PTRRecord extends DnsRecord {

    private String domain;

    public PTRRecord(){
        type = Types.PTR;
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

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain(){
        return domain;
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nDOMAIN: "+domain;
    }
}
