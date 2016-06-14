package com.zk.bean;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BinInfo {

    /**
     * desc : 原料
     * empty : false
     * materialIsEmpty : false
     * type : Material
     */
    private int id;
    private String desc;
    private boolean empty;
    private boolean materialIsEmpty;
    private String type;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isMaterialIsEmpty() {
        return materialIsEmpty;
    }

    public void setMaterialIsEmpty(boolean materialIsEmpty) {
        this.materialIsEmpty = materialIsEmpty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
