package com.example.imatch.JavaClasses;

public class Detail {

    public String dataname , dataDetail ;

    public Detail(String dataname, String dataDetail) {
        this.dataname = dataname;
        this.dataDetail = dataDetail;
    }

    public Detail() {
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(String dataDetail) {
        this.dataDetail = dataDetail;
    }
}
