package unet.dns.utils.inter;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsRecord {

    private Types type;
    private DnsClass dnsClass;
    private int ttl;
    private byte[] record;

    public DnsRecord(){
    }

    public DnsRecord(Types type, DnsClass dnsClass, int ttl, byte[] record){
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
        this.record = record;
    }

    public byte[] encode(){
        return null;
    }

    public void decode(byte[] buf, int off){
        type = Types.getTypeFromCode(((buf[off] & 0xFF) << 8) | (buf[off+1] & 0xFF));
        dnsClass = DnsClass.getClassFromCode(((buf[off+2] & 0xFF) << 8) | (buf[off+3] & 0xFF));

        ttl = (((buf[off+4] & 0xff) << 24) |
                ((buf[off+5] & 0xff) << 16) |
                ((buf[off+6] & 0xff) << 8) |
                (buf[off+7] & 0xff));

        record = new byte[((buf[off+8] & 0xFF) << 8) | (buf[off+9] & 0xFF)];
        System.arraycopy(buf, off+10, record, 0, record.length);
    }

    public int getLength(){
        return record.length+10;
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

    public void setRecord(byte[] record){
        this.record = record;
    }

    public byte[] getRecord(){
        return record;
    }

    @Override
    public String toString(){
        return "TYPE: "+type+"\r\nCLASS: "+dnsClass+"\r\nTTL: "+ttl+"\r\nRECORD: "+new String(record);
    }
}
