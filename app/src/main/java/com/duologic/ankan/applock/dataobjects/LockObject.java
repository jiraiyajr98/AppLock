package com.duologic.ankan.applock.dataobjects;

import android.graphics.drawable.Drawable;

public class LockObject {

    private String pkg_Name;

    private Boolean status;



    private String name;


    public LockObject(String pkg_Name, Boolean status, String name) {
        this.pkg_Name = pkg_Name;
        this.status = status;

        this.name = name;
    }


    public String getPkg_Name() {
        return pkg_Name;
    }

    public void setPkg_Name(String pkg_Name) {
        this.pkg_Name = pkg_Name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
