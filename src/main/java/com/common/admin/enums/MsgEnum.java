package com.common.admin.enums;

/**
 * @Author: liujianqiang
 * @Date: 2019-03-06
 * @Description:
 */
public enum MsgEnum {
    SUCCESS(10000, "成功"),
    FAILURE(10001, "失败");
    private int code;

    private String msg;

    MsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static MsgEnum getMsgEnum(String msg) {
        for (MsgEnum returnType : MsgEnum.values()) {
            if (msg.equals(returnType.getMsg()))
                return returnType;
        }
        return null;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}