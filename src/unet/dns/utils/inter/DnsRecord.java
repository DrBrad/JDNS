package unet.dns.utils.inter;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsRecord {

    private Types type;
    private DnsClass dnsClass;
    private int ttl;

    public DnsRecord(Types type, DnsClass dnsClass, int ttl){
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
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

    @Override
    public String toString(){
        return "TYPE: "+type+"\r\nCLASS: "+dnsClass+"\r\nTTL: "+ttl;
    }
}
