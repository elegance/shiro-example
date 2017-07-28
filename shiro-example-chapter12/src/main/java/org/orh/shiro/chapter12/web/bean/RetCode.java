package org.orh.shiro.chapter12.web.bean;

public enum RetCode {
    RESOURCE_NOT_FOUND("404", "can't found resource"), // 资源不存在
    LOCKED_RESOURCE("405", "can't found resource"), // 资源被锁定
    SYS_ERROR("500", "system is busy,please wait"), // 系统出错了
    INVOKE_ALLOW("403", "Do not have permission "), // 没有权限调用接口
    SESSION_TIMEOUT("400", "session timeout "), // 超时

    INVOKE_SUCCESS("0000", "操作成功"), // 调用成功

    ILLEGALARGUMENT("1007", "illegal argument"), // 非法参数
    
    UNAUTHORIZED_ERR("4001", "unauthorize error"),

    OTHER_ERROR("9999", " Unknown Error"); // 未知错误

    private String retcode;

    private String retmsg;

    public static RetCode getRetCodeByCode(String code) {
        for (RetCode retCode : RetCode.values()) {
            if (retCode.retcode.equals(code)) {
                return retCode;
            }
        }
        return null;
    }

    private RetCode(String retcode, String retmsg) {
        this.retcode = retcode;
        this.retmsg = retmsg;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

}
