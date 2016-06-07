package com.zk.bean;

/**
 * Created by Administrator on 2016/6/7.
 */
public class bin {

    String id;
    String kind ; //物品类型
    String x;       //横坐标
    String y;       //纵坐标

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "bin{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
