package unet.dns.utils;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ARecord extends DnsRecord {

    private InetAddress address;

    public ARecord(byte[] addr, DnsClass dnsClass, int ttl){
        super(Types.A, dnsClass, ttl);
        try{
            address = InetAddress.getByAddress(addr);
        }catch(UnknownHostException e){
            throw new IllegalArgumentException(e);
        }
    }

    public ARecord(InetAddress address, DnsClass dnsClass, int ttl){
        super(Types.A, dnsClass, ttl);
        this.address = address;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }

    public InetAddress getAddress(){
        return address;
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nADDRESS: "+address.getHostAddress();
    }
}
