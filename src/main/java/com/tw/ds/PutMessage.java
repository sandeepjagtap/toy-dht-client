package com.tw.ds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PutMessage  implements Serializable {

    private static final long serialVersionUID = 1L;

    private int key;
    private int value;

    public Map<String, Integer> getVersionInfoMap() {
        return versionInfoMap;
    }

    private Map<String, Integer> versionInfoMap = new HashMap<>();


    public PutMessage(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}
