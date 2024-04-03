package unet.dns.records;

import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ARecord extends DnsRecord {

    private InetAddress address;

    public ARecord(){
        type = Types.A;
    }

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        super.decode(buf, off);

        byte[] record = new byte[((buf[off+6] & 0xFF) << 8) | (buf[off+7] & 0xFF)];
        System.arraycopy(buf, off+8, record, 0, record.length);

        try{
            address = InetAddress.getByAddress(record);
        }catch(UnknownHostException e){
            throw new IllegalArgumentException("Invalid Inet Address");
        }
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
