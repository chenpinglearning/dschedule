package org.enterprise.api.response;

/**
 * @author: albert.chen
 * @create: 2023-11-28
 * @description:
 */
public class DscheduleResponse {
    private static final int FAIL = -1;
    private static final int SUCCESS = 0;
    /**
     * success code=0
     * fail code=-1
     */
    private int code;
    private String msg;
    private Object data;

    public static DscheduleResponse paramNotValid() {
        DscheduleResponse dscheduleResponse = new DscheduleResponse();
        dscheduleResponse.setCode(FAIL);
        dscheduleResponse.setMsg("param error");
        dscheduleResponse.setData(null);

        return dscheduleResponse;
    }

    public static DscheduleResponse success() {
        DscheduleResponse dscheduleResponse = new DscheduleResponse();
        dscheduleResponse.setCode(SUCCESS);
        dscheduleResponse.setMsg("success");
        dscheduleResponse.setData(null);

        return dscheduleResponse;
    }

    public static DscheduleResponse fail() {
        DscheduleResponse dscheduleResponse = new DscheduleResponse();
        dscheduleResponse.setCode(FAIL);
        dscheduleResponse.setMsg("push exception");
        dscheduleResponse.setData(null);

        return dscheduleResponse;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
