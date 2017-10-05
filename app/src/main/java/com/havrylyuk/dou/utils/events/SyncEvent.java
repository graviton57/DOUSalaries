package com.havrylyuk.dou.utils.events;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class SyncEvent {

    public static final int START_SYNC = 0;
    public static final int END_SYNC = 1;
    public static final int UPDATE_SYNC = 2;

    private int code;
    private String Status;

    public SyncEvent(int code, String status) {
        this.code = code;
        Status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
