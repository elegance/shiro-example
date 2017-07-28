package org.orh.shiro.chapter12.web.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RetMsg {

    private String code;

    private String msg;

    private Object data;

    private Object page;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public static RetMsg error(RetCode retCode) {
        RetMsg m = new RetMsg();
        m.setCode(retCode.getRetcode());
        m.setMsg(retCode.getRetmsg());
        return m;
    }

    public static RetMsg error(String msg) {
        RetMsg m = new RetMsg();
        m.setCode(RetCode.OTHER_ERROR.getRetcode());
        m.setMsg(msg);
        return m;
    }

    public static RetMsg error(RetCode retCode, String msg) {
        return error(retCode, msg, null);
    }

    /**
     * 设置错误时也可以放错误数据
     */
    public static RetMsg error(RetCode retCode, String msg, Object data) {
        RetMsg m = new RetMsg();
        m.setCode(retCode.getRetcode());
        m.setMsg(msg);
        m.setData(data);
        return m;
    }

    public static RetMsg success() {
        return success("操作成功！");
    }

    public static RetMsg success(Object data) {
        return success(data, null);
    }

    public static RetMsg success(Object data, Object page) {
        RetMsg m = new RetMsg();
        RetCode rc = RetCode.INVOKE_SUCCESS;
        m.setCode(rc.getRetcode());
        m.setMsg(rc.getRetmsg());
        if (page != null) {
            m.setPage(page);
        }
        if (data != null) {
            m.setData(data);
        }
        return m;
    }

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

}
