package unet.dns;

import unet.dns.messages.MessageBase;
import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;
import unet.dns.utils.DnsQuery;
import unet.dns.utils.ResponseCallback;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {

    /*
    TESTED
    - A
    - AAAA
    - NS
    - CNAME
    - SOA
    - PTR
    - MX
    - TXT
    x SRV
    x CAA
    */
    //.in-addr.arpa

    public static void main(String[] args)throws Exception {
        DnsClient client = new DnsClient();
        client.addServer(new InetSocketAddress(InetAddress.getByName("1.1.1.1"), 53));
        client.addServer(new InetSocketAddress(InetAddress.getByName("8.8.8.8"), 53));
        client.start(8080);

        MessageBase request = new MessageBase();
        request.setDestination(new InetSocketAddress(InetAddress.getByName("8.8.8.8"), 53));
        request.addQuery(new DnsQuery("google.com", Types.NS, DnsClass.IN));
        client.send(request, new ResponseCallback(){
            @Override
            public void onResponse(MessageBase response){
                System.out.println("Z: "+response.getResponseCode());
                System.out.println(response.isAuthoritative());
                //System.out.println(response.getResponseCode());
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
                    System.out.println(record.getQuery());
                    System.out.println();
                }
            }
        });
    }
}
