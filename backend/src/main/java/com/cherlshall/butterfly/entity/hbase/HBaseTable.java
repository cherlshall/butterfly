package com.cherlshall.butterfly.entity.hbase;

import lombok.Data;
import org.apache.hadoop.hbase.client.Result;

import java.util.*;

@Data
public class HBaseTable {

    private List<AbstractTableColumn> tableHead;

    /**
     * key: family + . + qualifier
     * value: value
     */
    private List<Map<String, String>> dataList;

    @Data
    abstract class AbstractTableColumn {

        /**
         * family 或 qualifier
         */
        protected String title;
    }

    @Data
    private class TableColumn extends AbstractTableColumn {
        /**
         * family + . + qualifier
         */
        private String dataIndex;

        private TableColumn(String title, String dataIndex) {
            this.title = title;
            this.dataIndex = dataIndex;
        }
    }

    @Data
    private class TableColumnWithChildren extends AbstractTableColumn {
        private List<TableColumn> children;

        private TableColumnWithChildren(String title) {
            this.title = title;
            this.children = new ArrayList<>();
        }
    }

    public HBaseTable(List<Result> results) {
        Map<String, Set<String>> familyStrMap = new TreeMap<>();
        tableHead = new ArrayList<>();
        dataList = new ArrayList<>();
        for (Result result : results) {
            Map<String, String> data = new HashMap<>();
            dataList.add(data);
            String rowkey = new String(result.getRow());
            data.put("rowkey", rowkey);
            // key: family
            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyMap = result.getMap();
            for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyEntry :
                    familyMap.entrySet()) {
                String family = new String(familyEntry.getKey());
                // 添加一级表头
                Set<String> qualifierStrSet = new TreeSet<>();
                Set<String> qualifierStrSetBefore = familyStrMap.putIfAbsent(family, qualifierStrSet);
                if (qualifierStrSetBefore != null) {
                    qualifierStrSet = qualifierStrSetBefore;
                }
                // key: qualifier
                NavigableMap<byte[], NavigableMap<Long, byte[]>> qualifierMap = familyEntry.getValue();
                for (Map.Entry<byte[], NavigableMap<Long, byte[]>> qualifierEntry : qualifierMap.entrySet()) {
                    String qualifier = new String(qualifierEntry.getKey());
                    qualifierStrSet.add(qualifier);
                    // key: timestamp
                    NavigableMap<Long, byte[]> timestampMap = qualifierEntry.getValue();
                    Map.Entry<Long, byte[]> firstEntry = timestampMap.firstEntry();
                    String value = new String(firstEntry.getValue());
                    data.put(family + "." + qualifier, value);
                }
            }
        }

        // 生成表头
        tableHead.add(new TableColumn("rowkey", "rowkey"));
        for (Map.Entry<String, Set<String>> entry : familyStrMap.entrySet()) {
            String key = entry.getKey();
            TableColumnWithChildren tcwc = new TableColumnWithChildren(key);
            Set<String> value = entry.getValue();
            for (String qualifier : value) {
                tcwc.children.add(new TableColumn(qualifier, key + "." + qualifier));
            }
            tableHead.add(tcwc);
        }
    }
}
