package unet.dns.utils;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressRecord extends DnsRecord {

    private InetAddress address;

    public AddressRecord(byte[] addr, Types type, DnsClass dnsClass, int ttl){
        super(type, dnsClass, ttl);
        try{
            address = InetAddress.getByAddress(addr);
        }catch(UnknownHostException e){
            throw new IllegalArgumentException(e);
        }
    }

    public AddressRecord(InetAddress address, Types type, DnsClass dnsClass, int ttl){
        super(type, dnsClass, ttl);
        this.address = address;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }

    public InetAddress getAddress(){
        return address;
    }

    public int getLength(){
        return super.getLength()+address.getAddress().length; //CHANGE THIS
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nADDRESS: "+address.getHostAddress();
    }
}
