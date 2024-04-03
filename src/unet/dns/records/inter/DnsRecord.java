package unet.dns.records.inter;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;

public class DnsRecord {

    protected Types type;
    protected DnsClass dnsClass;
    protected int ttl;

    protected String query;

    public DnsRecord(){
    }

    public DnsRecord(Types type, DnsClass dnsClass, int ttl){
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
    }

    public byte[] encode(){
        return null;
    }

    public void decode(byte[] buf, int off){
        dnsClass = DnsClass.getClassFromCode(((buf[off] & 0xFF) << 8) | (buf[off+1] & 0xFF));

        ttl = (((buf[off+2] & 0xff) << 24) |
                ((buf[off+3] & 0xff) << 16) |
                ((buf[off+4] & 0xff) << 8) |
                (buf[off+5] & 0xff));
    }

    public void setType(Types type){
        this.type = type;
    }

    public Types getType(){
        return type;
    }

    public void setDnsClass(DnsClass dnsClass){
        this.dnsClass = dnsClass;
    }

    public DnsClass getDnsClass(){
        return dnsClass;
    }

    public void setTTL(int ttl){
        this.ttl = ttl;
    }

    public int getTTL(){
        return ttl;
    }

    public void setQuery(String query){
        this.query = query;
    }

    public String getQuery(){
        return query;
    }

    @Override
    public String toString(){
        return "TYPE: "+type+"\r\nCLASS: "+dnsClass+"\r\nTTL: "+ttl+"\r\nQUERY: "+query;//+"\r\nRECORD: "+new String(record);
    }
}
