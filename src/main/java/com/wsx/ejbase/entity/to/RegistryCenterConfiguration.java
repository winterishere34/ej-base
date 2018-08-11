package com.wsx.ejbase.entity.to;

import java.io.Serializable;

public class RegistryCenterConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String zkAddressList;

    private String namespace;

    private String digest;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZkAddressList() {
        return zkAddressList;
    }

    public void setZkAddressList(String zkAddressList) {
        this.zkAddressList = zkAddressList;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
