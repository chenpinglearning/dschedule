package org.enterprise.domian.constants;

/**
 * @author: albert.chen
 * @create: 2023-12-01
 * @description:
 */
public enum DealStatus {
    NOT_DEAL(0, "未处理"),
    DEAL_SUCCESS(1, "处理成功"),
    DEAL_FAIL(-1, "处理失败");

    private final int status;
    private final String msg;

    DealStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
