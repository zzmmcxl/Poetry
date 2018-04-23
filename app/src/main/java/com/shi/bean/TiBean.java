package com.shi.bean;

import java.util.Arrays;

/**
 * Author: Yunr
 * Date: 2018-04-21 16:48
 */
public class TiBean {

    private String ti;
    private String key;
    private boolean isEdit;
    private String[] selectStr;

    public TiBean() {
    }

    public TiBean(String ti, String key) {
        this.ti = ti;
        this.key = key;
        this.isEdit = true;
    }

    public TiBean(String ti, String[] selectStr,String key) {
        this.ti = ti;
        this.isEdit = false;
        this.key = key;
        this.selectStr = selectStr;
    }

    public String[] getSelectStr() {
        return selectStr;
    }

    public void setSelectStr(String[] selectStr) {
        this.selectStr = selectStr;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getTi() {
        return ti == null ? "" : ti;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public String getKey() {
        return key == null ? "" : key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "TiBean{" +
                "ti='" + ti + '\'' +
                ", key='" + key + '\'' +
                ", isEdit=" + isEdit +
                ", selectStr=" + Arrays.toString(selectStr) +
                '}';
    }
}
