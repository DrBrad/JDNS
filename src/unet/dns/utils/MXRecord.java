package unet.dns.utils;

import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsRecord;

public class MXRecord extends DnsRecord {

    private int priority;
    private String domain;

    public MXRecord(){
        type = Types.MX;
    }

    @Override
    public byte[] encode(){
        byte[] buf = super.encode();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        super.decode(buf, off);



        //byte[] record = new byte[((buf[off+6] & 0xFF) << 8) | (buf[off+7] & 0xFF)];
        //System.arraycopy(buf, off+8, record, 0, record.length);

        priority = ((buf[off+8] & 0xFF) << 8) | (buf[off+9] & 0xFF);//((record[0] & 0xFF) << 8) | (record[1] & 0xFF);
        domain = DomainUtils.unpackDomain(buf, off+10);

        /*

        byte[] record = new byte[((buf[off+6] & 0xFF) << 8) | (buf[off+7] & 0xFF)];
        System.arraycopy(buf, off+8, record, 0, record.length);

        priority = ((buf[off+8] & 0xFF) << 8) | (buf[off+9] & 0xFF);
        domain = DomainUtils.unpackDomain(buf, off+10);*/
    }

    @Override
    public int getLength(){
        return super.getLength()+domain.length()+2;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain(){
        return domain;
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nPRIORITY: "+priority+"\r\nDOMAIN: "+domain;
    }
}
