package unet.dns;

public class Main {

    /*
    TESTED
    - A
    - AAAA
    - NS
    - CNAME
    - SOA
    x PTR
    - MX
    - TXT
    x SRV
    x CAA
    */

    public static void main(String[] args)throws Exception {
        //Resolver resolver = new Resolver("104.26.15.196.in-addr.arpa");
        new Resolver("m.facebook.com");
        //new Resolver("gmail.com");
        //new Resolver("netflix.com");
    }
}
