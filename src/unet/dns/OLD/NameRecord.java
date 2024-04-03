package unet.dns.OLD;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.DomainUtils;
import unet.dns.utils.inter.DnsRecord;

import static unet.dns.utils.DomainUtils.unpackDomain;

public class NameRecord extends DnsRecord {

    private String domain;

    public NameRecord(byte[] domain, Types type, DnsClass dnsClass, int ttl){
        //super(type, dnsClass, ttl);
        this.domain = DomainUtils.unpackDomain(domain);
    }

    public NameRecord(String domain, Types type, DnsClass dnsClass, int ttl){
        //super(type, dnsClass, ttl);
        this.domain = domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain(){
        return domain;
    }

    public int getLength(){
        return super.getLength()+domain.length(); //CHANGE THIS
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nDOMAIN: "+domain;
    }
}
