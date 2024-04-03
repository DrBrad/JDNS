package unet.dns.utils;

import java.util.Arrays;

public class DomainUtils {

    public static byte[] packDomain(String domain){
        byte[] buf = new byte[domain.length()];

        int offset = 0;
        for(String part : domain.split("\\.")){
            byte[] addr = part.getBytes();
            buf[offset] = (byte) addr.length;
            System.arraycopy(addr, 0, buf, offset+1, addr.length);
            offset += addr.length+1;
        }

        // End of domain name (null)
        buf[offset] = 0x00;

        return buf;
    }

    public static String unpackDomain(byte[] addr, int off){
        StringBuilder builder = new StringBuilder();

        int i = off;
        while(i < addr.length){
            int length = addr[i++];

            if(length == 0){
                break;
            }

            if((length & 0xc0) == 0xc0){
                i = ((length & 0x3f) << 8) | (addr[i++] & 0xff);

            }else{
                if(builder.length() > 0){
                    builder.append(".");
                }

                byte[] label = new byte[length];
                System.arraycopy(addr, i, label, 0, length);
                builder.append(new String(label));
                i += length;
            }
        }

        return builder.toString();
    }

    public static String parseDomainName(byte[] buf, int off){
        StringBuilder domainName = new StringBuilder();

        while(true){
            int labelLength = buf[off++];

            if(labelLength == 0){
                break;
            }

            if(domainName.length() > 0){
                domainName.append(".");
            }

            if((labelLength & 0xC0) == 0xC0){
                int pointer = ((labelLength & 0x3F) << 8) | (buf[off++] & 0xFF);
                off = pointer;

            }else{
                byte[] labelBytes = Arrays.copyOfRange(buf, off, off+labelLength);
                domainName.append(new String(labelBytes));
                off += labelLength;
            }
        }

        return domainName.toString();
    }
}
