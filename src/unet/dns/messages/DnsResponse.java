package unet.dns.messages;

import unet.dns.messages.inter.*;

import static unet.dns.utils.DomainUtils.unpackDomain;

public class DnsResponse extends MessageBase {

    //private List<DnsRecord> records;

    public DnsResponse(int id){
        super();
        this.id = id;
        //records = new ArrayList<>();
    }

    @Override
    public byte[] encode(){
        return new byte[0];
    }

    @Override
    public void decode(byte[] buf){
        super.decode(buf);

        /*
        int anCount = ((buf[6] & 0xFF) << 8) | (buf[7] & 0xFF);
        int offset = length+12;

        for(int i = 0; i < anCount; i++){
            switch((buf[offset] & 0b11000000) >>> 6){
                case 3:
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
                            record = new ARecord(addr, dnsClass, ttl);
                            break;

                        case AAAA: //USES ADDR
                            record = new AAAARecord(addr, dnsClass, ttl);
                            break;

                        case NS: //USES DOMAIN
                        case SOA: //USES DOMAIN
                            record = new NSRecord(addr, dnsClass, ttl);
                            break;

                        default:
                            return;
                    }

                    offset += addr.length+12;
                    records.add(record);

                    break;

                case 0:
                    offset++;
                    break;
            }*/
        //}
        /*
        System.out.println(qdCount+"  "+anCount);

        int offset = 12;


        for(int i = 0; i < qdCount; i++){
            String domainName = parseDomainName(buf, offset);
            type = Types.getTypeFromCode(((buf[offset+1] & 0xFF) << 8) | (buf[offset+2] & 0xFF));
            dnsClass = DnsClass.getClassFromCode(((buf[offset+3] & 0xFF) << 8) | (buf[offset+4] & 0xFF));
            System.out.println("R: "+domainName);

            queries.add(new DnsQuery(domainName, type, dnsClass));
        }

        //QNAME
        /*
        StringBuilder builder = new StringBuilder();
        builder.append(new String(buf, 13, buf[12]));


        int offset = buf[12]+13;

        while(buf[offset] > 0){
            builder.append('.'+new String(buf, offset+1, buf[offset]));
            offset += buf[offset]+1;
        }

        query = builder.toString();

        type = Types.getTypeFromCode(((buf[offset+1] & 0xFF) << 8) | (buf[offset+2] & 0xFF));
        dnsClass = DnsClass.getClassFromCode(((buf[offset+3] & 0xFF) << 8) | (buf[offset+4] & 0xFF));

        offset += 5;
        */

        //System.out.println(new String(buf, offset, buf.length-offset));

        /*
        for(int i = 0; i < anCount; i++){
            //System.out.println(buf[offset]);
            switch((buf[offset] & 0b11000000) >>> 6){
                case 3:
                    byte current = buf[offset+1];

                    Types type = Types.getTypeFromCode(((buf[offset+2] & 0xFF) << 8) | (buf[offset+3] & 0xFF));

                    DnsClass dnsClass = DnsClass.getClassFromCode(((buf[offset+4] & 0xFF) << 8) | (buf[offset+5] & 0xFF));

                    int ttl = (((buf[offset+6] & 0xff) << 24) |
                            ((buf[offset+7] & 0xff) << 16) |
                            ((buf[offset+8] & 0xff) << 8) |
                            (buf[offset+9] & 0xff));

                    offset += 10;

                    byte[] addr = new byte[((buf[offset] & 0xFF) << 8) | (buf[offset+1] & 0xFF)];
                    System.arraycopy(buf, offset+2, addr, 0, addr.length);

                    DnsRecord record;

                    switch(type){
                        case A:
                            record = new ARecord(addr, dnsClass, ttl);
                            break;

                        case NS:
                            record = new NSRecord(addr, dnsClass, ttl);
                            break;

                        default:
                            return;
                    }

                    offset += addr.length+2;
                    records.add(record);


                    break;

                case 0:
                    offset++;
                    break;
            }
        }*/

        //System.out.println(offset);
    }

    /*
    public boolean containsRecord(DnsRecord record){
        return records.contains(record);
    }

    public void addRecord(DnsRecord record){
        records.add(record);
    }

    public void removeRecord(DnsRecord record){
        records.remove(record);
    }

    public List<DnsRecord> getRecords(){
        return records;
    }

    public int totalRecords(){
        return records.size();
    }*/
}
