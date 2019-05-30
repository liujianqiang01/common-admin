package com.common.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: liujianqiang
 * @Date: 2019-04-02
 * @Description:日志封装
 */
public class LogUtil {
    /**
     * base基础版本日志无任何格式，只输出打印内容
     */
    private static final String NO_FORMAT_LOGGER = "noFormatLogger";
    /**
     * admin日志格式自带时间、uuid追踪等信息
     */
    private static final String FORMAT_LOGGER = "formatLogger";

    /**
     * 获取最原始被调用的堆栈信息
     */
    public static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        if(null == callStack) {return null;}

        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String logClassName = LogUtil.class.getName();
        // 循环遍历到日志类标识
        boolean isEachLogClass = false;

        // 遍历堆栈信息，获取出最原始被调用的方法信息
        for (StackTraceElement s : callStack) {
            // 遍历到日志类
            if(logClassName.equals(s.getClassName())) {
                isEachLogClass = true;
            }
            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if(isEachLogClass) {
                if(!logClassName.equals(s.getClassName())) {
                    isEachLogClass = false;
                    caller = s;
                    break;
                }
            }
        }
        return caller;
    }

    /**
     * 创建不带格式的日志
     * @param loggerName
     */
    public static void setNoFormatLogName(String loggerName){
        setLogName(NO_FORMAT_LOGGER,loggerName,null);
    }
    /**
     * 创建带格式的日志
     * @param loggerName
     */
    public static void setFormatLogName(String loggerName){
        setLogName(FORMAT_LOGGER,loggerName,"true");
    }
    /**
     * 设置日志文件名称，自定义多日志输出文件，需要在线程最开始设置日志文件名称。
     * 多线程情况下子线程不会继承MDC内容，需要手动设置（先getMDC 再赋值给子线程createMDC。
     * isBaseLog有三种值，
     */
    public static void setLogName(String logger, String loggerName,String format){
        //设置基础版本日志名称
        MDC.put(logger,loggerName);
        //状态标识成使用基础版
        MDC.put("format",format);
    }


    /**
     * 自动匹配请求类名，生成logger对象
     */
    private static Logger logger() {
        // 最原始被调用的堆栈对象
        StackTraceElement caller = findCaller();
        Logger log ;
        if(MDC.get("format") == null) {
            log = LoggerFactory.getLogger(NO_FORMAT_LOGGER);
        }else {
            log = LoggerFactory.getLogger(FORMAT_LOGGER);
            setMDC(caller);
        }
        return log;
    }

    /**
     * 日志需要的信息组装
     */
    private static void setMDC(StackTraceElement caller){
        //打印当前类
        MDC.put("action",caller.getFileName());
        //打印当前方法
        MDC.put("method",caller.getMethodName());
        //打印日志所在行数
        MDC.put("line", caller.getLineNumber()+"");
    }

    /**
     * 设置uuid追踪日志
     */
    public static void setUUID(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        MDC.put("traceId", uuid);
    }

    /**
     * 获取MDC
     */
    public static Map<String, String>  getMDC(){
        return  MDC.getCopyOfContextMap();

    }

    /**
     * 创建MDC
     * @param context
     */
    public static void createMDC( Map<String, String> context){
        MDC.setContextMap(context);
    }
    public static void trace(String msg) {
        trace(msg, null);
    }
    public static void trace(String msg, Throwable e) {
        logger().trace(msg, e);
    }
    public static void debug(String msg) {
        debug(msg, null);
    }
    public static void debug(String msg, Throwable e) {
        logger().debug(msg, e);
    }
    public static void info(String msg) {
        info(msg, null);
    }
    public static void info(String msg, Throwable e) {
        logger().info(msg, e);
    }
    public static void warn(String msg) {
        warn(msg, null);
    }
    public static void warn(String msg, Throwable e) {
        logger().warn(msg, e);
    }
    public static void error(String msg) {
        error(msg, null);
    }
    public static void error(String msg, Throwable e) {
        logger().error(msg, e);
    }
}