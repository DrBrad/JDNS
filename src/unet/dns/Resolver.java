package unet.dns;

import unet.dns.messages.DnsRequest;
import unet.dns.messages.DnsResponse;
import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.OpCodes;
import unet.dns.messages.inter.Types;
import unet.dns.utils.DnsQuery;
import unet.dns.records.inter.DnsRecord;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Resolver {

    public static final int DNS_SERVER_PORT = 53;

    public Resolver(String query)throws IOException {
        //InetAddress ipAddress = InetAddress.getByName("elisabeth.ns.cloudflare.com");
        InetAddress ipAddress = InetAddress.getByName("1.1.1.1");
        //InetAddress ipAddress = InetAddress.getByName("192.168.16.2");


        Random random = new Random();
        int id = random.nextInt(32767);
        DnsRequest request = new DnsRequest(id);
        //request.setQuery(query);

        //request.setOpCode(OpCodes.IQUERY);
        request.addQuery(new DnsQuery(query, Types.A, DnsClass.IN));
        //request.addQuery(new DnsQuery(query, Types.PTR, DnsClass.IN));
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

        StringBuilder sb = new StringBuilder();
        for (byte b : packet.getData()) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());

        //System.out.println(packet.getLength());

        id = ((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF);

        DnsResponse response = new DnsResponse(id);
        response.decode(buf);

        //System.err.println("A: "+packet.getLength());
        //System.out.println("A: "+response.getQuery());

    }
}
