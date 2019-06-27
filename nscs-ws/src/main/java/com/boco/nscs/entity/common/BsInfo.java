package com.boco.nscs.entity.common;

import java.io.Serializable;

/**
 * Created by rc on 2019/5/22.
 */
public class BsInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String longitude;
    private String latitude;
    private String county;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
