package unet.dns.store;

import unet.dns.messages.inter.Types;
import unet.dns.records.inter.DnsRecord;
import unet.dns.utils.DnsQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordStore {

    //TODO
    /*
    DOMAIN
    - A Records
    - AAAA Records
    - MX Records
    - CName Records
    - NS Records
    - Txt Records

    IP
    - Domain
    */
    private Map<String, Map<Types, List<DnsRecord>>> domains;//, inverse;

    public RecordStore(){
        domains = new HashMap<>();
        //inverse = new ArrayList<>();
    }

    public void addRecord(DnsRecord record){
        if(domains.containsKey(record.getQuery())){
            if(domains.get(record.getQuery()).containsKey(record.getType())){
                domains.get(record.getQuery()).get(record.getType()).add(record);
                return;
            }

            List<DnsRecord> records = new ArrayList<>();
            records.add(record);
            domains.get(record.getQuery()).put(record.getType(), records);
            return;
        }

        Map<Types, List<DnsRecord>> types = new HashMap<>();
        List<DnsRecord> records = new ArrayList<>();
        records.add(record);

        types.put(record.getType(), records);
        domains.put(record.getQuery(), types);
    }

    public List<DnsRecord> getRecord(DnsQuery query){
        if(domains.containsKey(query.getQuery())){
            if(domains.get(query.getQuery()).containsKey(query.getType())){
                return domains.get(query.getQuery()).get(query.getType());
            }
        }

        return null;
    }

    public boolean hasAnswers(DnsQuery query){
        if(domains.containsKey(query.getQuery())){
            return domains.get(query.getQuery()).containsKey(query.getType());
        }

        return false;
    }
}
