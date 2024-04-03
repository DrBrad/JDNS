package unet.dns;

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
        //new Resolver("8.8.8.8");
        new Resolver("8.8.8.8.in-addr.arpa"); //METHOD B
        //new Resolver("bacon.circuitswest.com");
        //new Resolver("gmail.com");
        //new Resolver("netflix.com");
    }
}
