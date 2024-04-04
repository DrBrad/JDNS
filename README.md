JDNS
=====
JDNS is a Java DNS library that allows you to host a DNS Server and make DNS Requests.

Why JDNS
-----
InetAddress is great because it will auto get the IP address from any hostname, however this isnt always a great option when it comes to security. This is because it will make requests to your DNS server posibbly bypassing SOCKS proxies.
This project will allow you to make requests to specific servers not what is defined by the host and potentially over SOCKS5.

Supported Records
-----
I have not been able to test every record type, so some may function but I'm uncertain.
| TYPE | Value |
| --- | --- |
| A | Working |
| AAAA | Working |
| NS | Working |
| CNAME | Working |
| SOA | Working |
| PTR | Working |
| MX | Working |
| TXT | Working |
| SRV | Untested |
| CAA | Untested |

> [!NOTE]
> IQUERY (Inverse Query) is depricated in [RFC-1035](https://datatracker.ietf.org/doc/html/rfc3425) and theirfore not implemented in this project

Usage
-----
To start the DNS server this is the usage:
```java
DnsServer server = new DnsServer();
//IF THE DOMAIN IS NOT IN OUR RECORDS USE THIS DNS SERVER
server.addServer(new InetSocketAddress(InetAddress.getByName("1.1.1.1"), 53));
//FALLBACK DNS SERVER - YOU CAN ADD AS MANY AS YOU WANT
server.addServer(new InetSocketAddress(InetAddress.getByName("1.0.1.0"), 53));
server.start(53);
```

To send a request you can do this:
```java
server.send(request, new ResponseCallback(){
    @Override
    public void onResponse(ResponseEvent event){
        MessageBase response = event.getMessage();
    }
});
```

