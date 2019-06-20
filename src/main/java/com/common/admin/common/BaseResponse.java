package com.common.admin.common;

import com.alibaba.fastjson.JSON;
import com.common.admin.enums.MsgEnum;
import com.common.admin.utils.LogUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: liujianqiang
 * @Date: 2019-01-01
 * @Description:
 */
public class BaseResponse<T> implements Serializable {

    /**
     * 返回状态码
     */
    int code;

    /**
     * 返回结果
     */
    T result;

    /**
     * 返回状态描述
     */
    String msg;

    private BaseResponse(int code, T result, String msg) {
        this.code = code;
        this.result = result;
        this.msg = msg;
    }
    private static <T> BaseResponse newInstance(int code, T result, String msg) {
        BaseResponse apiResponse = new BaseResponse<>(code, result, msg);
        //返回数据打印在adminLog中
        LogUtil.setLogName("adminLogger");
        LogUtil.info("返回数据:"+ JSON.toJSONString(apiResponse));
        return apiResponse;
    }
    private static <E extends Enum<E>, T> BaseResponse<T> fail(T result, E error) {

        int code = MsgEnum.FAILURE.getCode();
        String msg =MsgEnum.FAILURE.getMsg();
        Class clazz = error.getDeclaringClass();
        try {
            for (Object obj : clazz.getEnumConstants()) {
                if (obj.equals(error)) {
                    code = (int) clazz.getMethod("getCode", new Class[0]).invoke(obj);
                    msg = (String) clazz.getMethod("getMsg", new Class[0]).invoke(obj);
                    if (code < 0) {
                        return newInstance(code, result, msg);
                    } else {
                        return newInstance(code, result, msg);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            LogUtil.error("组装返回数据异常", e);
        } catch (InvocationTargetException e) {
            LogUtil.error("组装返回数据异常", e);
        } catch (NoSuchMethodException e) {
            LogUtil.error("组装返回数据异常", e);
        }

        return newInstance(code, result, msg);

    }

    public static final BaseResponse success() {
        return success(null);
    }

    public static final <T> BaseResponse<T> success(T result) {
        return newInstance(MsgEnum.SUCCESS.getCode(), result, MsgEnum.SUCCESS.getMsg());
    }

    public static final <E extends Enum<E>, T> BaseResponse<T> fail(E error) {
        return fail(null, error);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}