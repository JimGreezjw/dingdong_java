package com.dingdong.common.model;

import java.io.Serializable;

/**
 * 所有bean的父类，包含bean的id值，要求实现接口<code>Serializable</code>
 * @author chenliang
 * @version 2015年12月18日 上午12:09:12
 */
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 1886225807068944477L;
    
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
