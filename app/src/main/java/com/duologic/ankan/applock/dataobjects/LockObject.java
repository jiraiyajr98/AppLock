package com.duologic.ankan.applock.dataobjects;

public class LockObject {

    private String pkg_Name;

    private Boolean status;

    public LockObject(String pkg_Name, Boolean status) {
        this.pkg_Name = pkg_Name;
        this.status = status;
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
}
