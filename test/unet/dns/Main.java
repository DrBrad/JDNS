package unet.dns;

import unet.dns.messages.MessageBase;
import unet.dns.messages.inter.DnsClass;
import unet.dns.messages.inter.Types;
import unet.dns.records.ARecord;
import unet.dns.records.MXRecord;
import unet.dns.records.TXTRecord;
import unet.dns.records.inter.DnsRecord;
import unet.dns.events.ResponseEvent;
import unet.dns.store.RecordStore;
import unet.dns.utils.DnsQuery;
import unet.dns.utils.ResponseCallback;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

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
    //WE NEED OBSERVER FOR RECORD CHANGES - AS LENGTH WOULD CHANGE...

    public static void main(String[] args)throws Exception {

        DnsServer server = new DnsServer();
        server.addServer(new InetSocketAddress(InetAddress.getByName("1.1.1.1"), 53));
        server.addServer(new InetSocketAddress(InetAddress.getByName("1.0.1.0"), 53));

        server.getRecordStore().add(new ARecord("google.com", DnsClass.IN, 300, InetAddress.getByName("1.1.1.1")));
        server.getRecordStore().add(new TXTRecord("distributed.unet", DnsClass.IN, 300, "NULL"));
        server.getRecordStore().add(new MXRecord("distributed.unet", DnsClass.IN, 300, 10, "google.com"));
        server.getRecordStore().add(new ARecord("distributed.unet", DnsClass.IN, 300, InetAddress.getByName("1.1.1.1")));
        server.getRecordStore().add(new ARecord("distributed.unet", DnsClass.IN, 300, InetAddress.getByName("1.1.1.2")));
        server.getRecordStore().add(new ARecord("distributed.unet", DnsClass.IN, 300, InetAddress.getByName("1.1.1.3")));

        server.start(5053);


        DnsServer client = new DnsServer();
        client.addServer(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 5053));
        client.start(8080);

        MessageBase request = new MessageBase();
        request.addQuery(new DnsQuery("distributed.unet", Types.TXT, DnsClass.IN));
        //request.addQuery(new DnsQuery("one.one.one.one", Types.A, DnsClass.IN));
        client.send(request, new ResponseCallback(){
            @Override
            public void onResponse(ResponseEvent event){
                MessageBase response = event.getMessage();
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
