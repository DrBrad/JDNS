package unet.dns.utils.inter;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;

import java.nio.charset.StandardCharsets;

import static unet.dns.utils.DomainUtils.unpackDomain;

public class DnsQuery {

    private String query;
    private Types type;
    private DnsClass dnsClass;

    public DnsQuery(){
    }

    public DnsQuery(String query, Types type, DnsClass dnsClass){
        this.query = query;
        this.type = type;
        this.dnsClass = dnsClass;
    }

    public byte[] encode(){
        byte[] buf = new byte[getLength()];
        int offset = 0;

        for(String part : query.split("\\.")){
            byte[] addr = part.getBytes();
            buf[offset] = (byte) addr.length;
            System.arraycopy(addr, 0, buf, offset+1, addr.length);
            offset += addr.length+1;
        }

        // End of domain name (null)
        buf[offset] = 0x00;

        // QTYPE (16 bits) - A record
        buf[offset+1] = ((byte) (type.getCode() >> 8));
        buf[offset+2] = ((byte) type.getCode());

        // QCLASS (16 bits) - IN class
        buf[offset+3] = ((byte) (dnsClass.getCode() >> 8));
        buf[offset+4] = ((byte) dnsClass.getCode());

        // Truncate unused portion of the byte array
        //byte[] truncatedFrame = new byte[offset+5];
        //System.arraycopy(buf, 0, truncatedFrame, 0, offset+5);

        return buf;
    }

    public void decode(byte[] buf, int off){
        query = unpackDomain(buf, off);
        off += query.length()+2;

        type = Types.getTypeFromCode(((buf[off] & 0xFF) << 8) | (buf[off+1] & 0xFF));
        dnsClass = DnsClass.getClassFromCode(((buf[off+2] & 0xFF) << 8) | (buf[off+3] & 0xFF));
    }

    public int getLength(){
        return query.getBytes().length+6;
    }

    public void setQuery(String query){
        this.query = query;
    }

    public String getQuery(){
        return query;
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

    @Override
    public String toString(){
        return "TYPE: "+type+"\r\nCLASS: "+dnsClass+"\r\nQUERY: "+query;
    }
}
