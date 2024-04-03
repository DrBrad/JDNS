package unet.dns;

import unet.dns.messages.DnsRequest;
import unet.dns.messages.DnsResponse;
import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.MessageBase;
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
    //01 34 01 34 01 34 01 34 02 69 6e 07 61 64 64 72 61 04 61 72 70 61 00

    public static void main(String[] args)throws Exception {
        DnsClient client = new DnsClient();
        client.addServer(new InetSocketAddress(InetAddress.getByName("1.1.1.1"), 53));
        client.addServer(new InetSocketAddress(InetAddress.getByName("8.8.8.8"), 53));
        client.start(8080);

        DnsRequest request = new DnsRequest();
        request.addQuery(new DnsQuery("google.com", Types.A, DnsClass.IN));
        client.send(request, new ResponseCallback(){
            @Override
            public void onResponse(DnsResponse response){
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

        //new Resolver("1.1.1.1.in-addr.arpa");
        /*
        REQUEST:
        QUERY
        PTR
        EXPECTED OUTPUT

        TYPE: PTR
        CLASS: IN
        TTL: 1281
        QUERY: 1.1.1.1.in-addr.arpa
        DOMAIN: one.one.one.one
        */

        //new Resolver("gmail.com");
        /*
        REQUEST:
        QUERY
        A
        EXPECTED OUTPUT

        TYPE: A
        CLASS: IN
        TTL: 208
        QUERY: gmail.com
        ADDRESS: 142.250.72.5
        */


        //new Resolver("8.8.8.8");
        //new Resolver("bacon.circuitswest.com");
        //new Resolver("gmail.com");
        //new Resolver("netflix.com");
    }
}
