package com.zk.event;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/13.
 */
public class EventAA {
    public static final int ACTION_SEND_MSG = 1;
    public static final int ACTION_SEND_CHARGE = 2;
    protected String message;
    private Map<String, String> mapMessage;
    protected int actionType;

    public EventAA(String message, int actionType) {
        this.message = message;
        this.actionType = actionType;
    }

    public EventAA(Map<String, String> message, int actionType) {
        this.mapMessage = message;
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }

    public Map<String, String> getMapMessage() {
        return mapMessage;
    }

    public String getMessage() {
        return message;
    }
}
