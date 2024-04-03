package unet.dns.utils;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsRecord;

public class RecordUtils {

    public static DnsRecord unpackRecord(byte[] buf, int offset){
        Types type = Types.getTypeFromCode(((buf[offset+2] & 0xFF) << 8) | (buf[offset+3] & 0xFF));

        DnsClass dnsClass = DnsClass.getClassFromCode(((buf[offset+4] & 0xFF) << 8) | (buf[offset+5] & 0xFF));

        int ttl = (((buf[offset+6] & 0xff) << 24) |
                ((buf[offset+7] & 0xff) << 16) |
                ((buf[offset+8] & 0xff) << 8) |
                (buf[offset+9] & 0xff));

        byte[] addr = new byte[((buf[offset+10] & 0xFF) << 8) | (buf[offset+11] & 0xFF)];
        System.arraycopy(buf, offset+12, addr, 0, addr.length);

        DnsRecord record;

        switch(type){
            case A: //USES ADDR
            case AAAA: //USES ADDR
                record = new AddressRecord(addr, type, dnsClass, ttl);
                break;

            case MX:
            case NS: //USES DOMAIN
            case SOA: //USES DOMAIN
                //System.err.println(type+"  \""+new String(addr)+"\"  "+ DomainUtils.unpackDomain(addr).length());
                record = new NameRecord(addr, type, dnsClass, ttl);
                break;

            case TXT:
                //MX  IN  1262   �.
                //TXT  IN  300  v=spf1 redirect=_spf.google.com
                System.out.println("TXT RECORD");
                return null;

            default:
                System.out.println(type);
                return null;
        }

        offset += addr.length+12;
        //answers.add(record);
        return record;
    }
}
