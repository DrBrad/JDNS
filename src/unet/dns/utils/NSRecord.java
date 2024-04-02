package unet.dns.utils;

import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsRecord;

import java.util.Arrays;

public class NSRecord extends DnsRecord {

    private String domain;

    public NSRecord(byte[] domain, DnsClass dnsClass, int ttl){
        super(Types.NS, dnsClass, ttl);
        this.domain = decodeDomainName(domain);
    }

    public NSRecord(String domain, DnsClass dnsClass, int ttl){
        super(Types.NS, dnsClass, ttl);
        this.domain = domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain(){
        return domain;
    }

    private String decodeDomainName(byte[] nsRecordBytes) {
        StringBuilder domainName = new StringBuilder();
        int offset = 0;

        while (offset < nsRecordBytes.length) {
            int labelLength = nsRecordBytes[offset++];

            if (labelLength == 0) {
                // End of domain name
                break;
            }

            if (domainName.length() > 0) {
                domainName.append(".");
            }

            if ((labelLength & 0xC0) == 0xC0) {
                // Compression pointer
                int pointer = ((labelLength & 0x3F) << 8) | (nsRecordBytes[offset++] & 0xFF);
                offset = pointer;
            } else {
                // Normal label
                byte[] labelBytes = Arrays.copyOfRange(nsRecordBytes, offset, offset + labelLength);
                domainName.append(new String(labelBytes));
                offset += labelLength;
            }
        }

        return domainName.toString();
    }

    @Override
    public String toString(){
        return super.toString()+"\r\nDOMAIN: "+domain;
    }
}
