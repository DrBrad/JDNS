package unet.dns.messages.inter;

import unet.dns.utils.DnsQuery;
import unet.dns.utils.inter.DnsRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static unet.dns.utils.DomainUtils.unpackDomain;

public class MessageBase {

    protected int id;
    //protected String query;
    protected OpCodes opCode = OpCodes.QUERY;
    protected ResponseCodes responseCode = ResponseCodes.NO_ERROR;

    protected boolean qr, authoritative, truncated, recursionDesired, recursionAvailable;
    protected int /*qdCount, anCount, */nsCount, arCount, length;
    //QDCOUNT = Question count
    //ANCOUNT = Answer Count

    protected List<DnsQuery> queries;
    private List<DnsRecord> answers;

    public MessageBase(){
        queries = new ArrayList<>();
        answers = new ArrayList<>();
    }

    public byte[] encode(){
        byte[] buf = new byte[12+length];
        buf[0] = (byte) (id >> 8); // First 8 bits
        buf[1] = (byte) id; // Second 8 bits

        int z = 0;

        int flags = (qr ? 0x8000 : 0) | // QR bit
                ((opCode.getCode() & 0x0F) << 11) | // Opcode bits
                (authoritative ? 0x0400 : 0) | // AA bit
                (truncated ? 0x0200 : 0) | // TC bit
                (recursionDesired ? 0x0100 : 0) | // RD bit
                (recursionAvailable ? 0x0080 : 0) | // RA bit
                ((z & 0x07) << 4) | // Z bits
                (responseCode.getCode() & 0x0F); // RCODE bits

        buf[2] = (byte) (flags >> 8); // First 8 bits
        buf[3] = (byte) flags; // Second 8 bits

        // QDCOUNT (16 bits)
        buf[4] = (byte) (queries.size() >> 8);
        buf[5] = (byte) queries.size();

        // ANCOUNT (16 bits)
        buf[6] = (byte) (answers.size() >> 8);
        buf[7] = (byte) answers.size();

        // NSCOUNT (16 bits)
        buf[8] = (byte) (nsCount >> 8);
        buf[9] = (byte) nsCount;

        // ARCOUNT (16 bits)
        buf[10] = (byte) (arCount >> 8);
        buf[11] = (byte) arCount;

        int offset = 12;

        for(DnsQuery query : queries){
            byte[] q = query.encode();
            System.arraycopy(q, 0, buf, offset, q.length);
            offset += q.length;
        }

        return buf;
    }

    public void decode(byte[] buf){
        qr = ((buf[2] >> 7) & 0x1) == 1;
        opCode = OpCodes.getOpFromCode((buf[2] >> 3) & 0xF);
        authoritative = ((buf[2] >> 2) & 0x1) == 1;
        truncated = ((buf[2] >> 1) & 0x1) == 1;
        //boolean recursionDesired = (buf[2] & 0x1) == 1;
        recursionAvailable = ((buf[3] >> 7) & 0x1) == 1;
        int z = (buf[3] >> 4) & 0x3;
        responseCode = ResponseCodes.getResponseCodeFromCode(buf[3] & 0xF);

        int qdCount = ((buf[4] & 0xFF) << 8) | (buf[5] & 0xFF);
        int anCount = ((buf[6] & 0xFF) << 8) | (buf[7] & 0xFF);
        nsCount = ((buf[8] & 0xFF) << 8) | (buf[9] & 0xFF);
        arCount = ((buf[10] & 0xFF) << 8) | (buf[11] & 0xFF);

        int offset = 12;

        for(int i = 0; i < qdCount; i++){
            String domainName = unpackDomain(buf, offset);
            offset += domainName.length()+2;

            Types type = Types.getTypeFromCode(((buf[offset] & 0xFF) << 8) | (buf[offset+1] & 0xFF));
            DnsClass dnsClass = DnsClass.getClassFromCode(((buf[offset+2] & 0xFF) << 8) | (buf[offset+3] & 0xFF));

            queries.add(new DnsQuery(domainName, type, dnsClass));

            offset += 4;
            length += domainName.length()+6;
        }
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public void setOpCode(OpCodes opCode){
        this.opCode = opCode;
    }

    public OpCodes getOpCode(){
        return opCode;
    }

    public void setAuthoritative(boolean authoritative){
        this.authoritative = authoritative;
    }

    public boolean isAuthoritative(){
        return authoritative;
    }

    public void setTruncated(boolean truncated){
        this.truncated = truncated;
    }

    public boolean isTruncated(){
        return truncated;
    }

    public void setRecursionDesired(boolean recursionDesired){
        this.recursionDesired = recursionDesired;
    }

    public boolean isRecursionDesired(){
        return recursionDesired;
    }

    public void setRecursionAvailable(boolean recursionAvailable){
        this.recursionAvailable = recursionAvailable;
    }

    public boolean isRecursionAvailable(){
        return recursionAvailable;
    }

    public void setResponseCode(ResponseCodes responseCode){
        this.responseCode = responseCode;
    }

    public ResponseCodes getResponseCode(){
        return responseCode;
    }

    public int totalQueries(){
        return queries.size();
    }

    public void addQuery(DnsQuery query){
        length += query.getLength();
        queries.add(query);
    }

    public List<DnsQuery> getQueries(){
        return queries;
    }

    /*
    public void setAnCount(int anCount){
        this.anCount = anCount;
    }

    public int getAnCount(){
        return anCount;
    }

    public void setNsCount(int nsCount){
        this.nsCount = nsCount;
    }

    public int getNsCount(){
        return nsCount;
    }

    public void setArCount(int arCount){
        this.arCount = arCount;
    }

    public int getArCount(){
        return arCount;
    }
    */
}
