package unet.dns;

import unet.dns.messages.DnsRequest;
import unet.dns.messages.DnsResponse;
import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.utils.inter.DnsQuery;
import unet.dns.records.inter.DnsRecord;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Resolver {

    public static final int DNS_SERVER_PORT = 53;

    public Resolver(String query)throws IOException {
        InetAddress ipAddress = InetAddress.getByName("elisabeth.ns.cloudflare.com");
        //InetAddress ipAddress = InetAddress.getByName("8.8.8.8");


        Random random = new Random();
        int id = random.nextInt(32767);
        DnsRequest request = new DnsRequest(id);
        //request.setQuery(query);

        //request.setOpCode(OpCodes.IQUERY);
        //request.addQuery(new DnsQuery(query, Types.A, DnsClass.IN));
        request.addQuery(new DnsQuery(query, Types.CNAME, DnsClass.IN));
        //request.addQuery(new DnsQuery(query, Types.A, DnsClass.IN));
        //request.addQuery(new DnsQuery("google.com", Types.A, DnsClass.IN));
        //request.addQuery(new DnsQuery(query, Types.A, DnsClass.IN));


        //request.setOpCode(OpCodes.IQUERY);
        //request.setType(Types.PTR);
        //request.setType(Types.NS);


        byte[] buf = request.encode();

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket dnsReqPacket = new DatagramPacket(buf, buf.length, ipAddress, DNS_SERVER_PORT);
        socket.send(dnsReqPacket);


        //System.out.println(id);


        buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        id = ((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF);

        DnsResponse response = new DnsResponse(id);
        response.decode(buf);

        //System.err.println("A: "+packet.getLength());
        //System.out.println("A: "+response.getQuery());
        //System.out.println("A: "+response.getResponseCode());
        //System.out.println(response.getResponseCode());
        //System.out.println(response.isAuthoritative()+"  "+packet.getLength());
        //System.out.println("QUERIES");
        System.out.println();
        for(DnsQuery q : response.getQueries()){
            System.out.println(q);
            System.out.println();
        }

        //System.out.println();
        //System.out.println("ANSWERS");
        //System.out.println();

        for(DnsRecord record : response.getAnswers()){
            System.out.println(record);
            System.out.println();
        }

        //System.out.println();
        //System.out.println("NAME SERVERS");
        //System.out.println();

        for(DnsRecord record : response.getNameServers()){
            System.out.println(record);
            System.out.println();
        }
    }
}
