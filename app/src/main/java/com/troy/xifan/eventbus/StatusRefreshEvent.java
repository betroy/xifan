package com.troy.xifan.eventbus;

import com.troy.xifan.model.response.StatusRes;

/**
 * Created by chenlongfei on 2016/12/26.
 */

public class StatusRefreshEvent {
    public static final String TYPE_FAVORITE_STATUS = "favorite_status";
    public static final String TYPE_DELETE_STATUS = "delete_status";
    public static final String TYPE_REFRESH_STATUS = "refresh_status";

    private StatusRes statusRes;
    private String type;

    public StatusRefreshEvent(StatusRes statusRes, String type) {
        this.statusRes = statusRes;
        this.type = type;
    }

    public StatusRes getStatusRes() {
        return statusRes;
    }

    public void setStatusRes(StatusRes statusRes) {
        this.statusRes = statusRes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
