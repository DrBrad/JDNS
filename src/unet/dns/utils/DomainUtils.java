package unet.dns.utils;

import java.util.Arrays;

public class DomainUtils {

    public static String unpackDomain(byte[] buf, int off) {
        StringBuilder domainName = new StringBuilder();

        // Parse each label in the domain name
        while (true) {
            int labelLength = buf[off++];

            if (labelLength == 0) {
                // End of domain name
                break;
            }

            if (domainName.length() > 0) {
                domainName.append(".");
            }

            if ((labelLength & 0xC0) == 0xC0) {
                // Compression pointer
                int pointer = ((labelLength & 0x3F) << 8) | (buf[off++] & 0xFF);
                off = pointer;
            } else {
                // Normal label
                byte[] labelBytes = Arrays.copyOfRange(buf, off, off + labelLength);
                domainName.append(new String(labelBytes));
                off += labelLength;
            }
        }

        return domainName.toString();
    }
}
