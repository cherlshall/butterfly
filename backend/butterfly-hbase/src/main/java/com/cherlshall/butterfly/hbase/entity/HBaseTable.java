package com.cherlshall.butterfly.hbase.entity;

import lombok.Data;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

@Data
public class HBaseTable {

    private List<FamilyAndQualifier> familyAndQualifiers;

    /**
     * key: family + . + qualifier
     * value: value
     */
    private List<Map<String, String>> dataList;

    private List<HBaseBeanAdv> dataColList;

    @Data
    private class FamilyAndQualifier {
        private String family;
        private Set<String> qualifiers;

        private FamilyAndQualifier(String family) {
            this.family = family;
            qualifiers = new TreeSet<>();
        }
    }

    public HBaseTable(List<Result> results) {
        familyAndQualifiers = new ArrayList<>();
        dataList = new ArrayList<>();
        dataColList = new ArrayList<>();
        Map<String, FamilyAndQualifier> map = new HashMap<>();
        for (Result result : results) {
            fromResult(result, map);
        }
    }

    public HBaseTable(Result result) {
        familyAndQualifiers = new ArrayList<>();
        dataList = new ArrayList<>();
        dataColList = new ArrayList<>();
        Map<String, FamilyAndQualifier> map = new HashMap<>();
        fromResult(result, map);
    }

    private void fromResult(Result result, Map<String, FamilyAndQualifier> map) {
        Map<String, String> data = new HashMap<>();
        dataList.add(data);
        String rowKey = Bytes.toHex(result.getRow());
        data.put("rowKey", rowKey);
        // key: family
        NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyMap = result.getMap();
        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyEntry :
                familyMap.entrySet()) {
            String family = new String(familyEntry.getKey());
            FamilyAndQualifier faq = map.get(family);
            if (faq == null) {
                faq = new FamilyAndQualifier(family);
                familyAndQualifiers.add(faq);
                map.put(family, faq);
            }
            // key: qualifier
            NavigableMap<byte[], NavigableMap<Long, byte[]>> qualifierMap = familyEntry.getValue();
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> qualifierEntry : qualifierMap.entrySet()) {
                String qualifier = new String(qualifierEntry.getKey());
                faq.qualifiers.add(qualifier);
                // key: timestamp
                NavigableMap<Long, byte[]> timestampMap = qualifierEntry.getValue();
                Map.Entry<Long, byte[]> firstEntry = timestampMap.firstEntry();
                String value = new String(firstEntry.getValue());
                data.put(family + "." + qualifier, value);
                for (Map.Entry<Long, byte[]> timestampEntry : timestampMap.entrySet()) {
                    HBaseBeanAdv hBaseBeanAdv = new HBaseBeanAdv(
                            rowKey, family, qualifier, value, timestampEntry.getKey());
                    dataColList.add(hBaseBeanAdv);
                }
            }
        }
    }
}
