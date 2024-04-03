package unet.dns.records;

import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TXTRecord extends DnsRecord {

    private String record;

    public TXTRecord(){
        type = Types.TXT;
    }

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        super.decode(buf, off);

        this.record = new String(buf, off+8, ((buf[off+6] & 0xFF) << 8) | (buf[off+7] & 0xFF));
    }

    public void setRecord(String record){
        this.record = record;
    }

    public String getRecord(){
        return record;
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nRECORD: "+record;
    }
}
