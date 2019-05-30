package com.common.admin.controller;

import com.common.admin.common.AdminResponse;
import com.common.admin.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 日志测试类
 */
@Controller
public class LogTestController {

    @RequestMapping("/test")
    @ResponseBody
    public AdminResponse test() {
        LogUtil.setFormatLogName("test");
        LogUtil.info("========>带日期格式");

        LogUtil.setNoFormatLogName("test1");
        LogUtil.info("========>不带日期格式");
        return AdminResponse.success();
    }
    @RequestMapping("/test2")
    @ResponseBody
    public AdminResponse test22(Integer id) {
        LogUtil.info("========>id = "+id);
        LogUtil.warn("========>warn");
        LogUtil.error("========>error");

        testRun(LogUtil.getMDC());
        return AdminResponse.success("hahahhahhahah");
    }

    /**
     * 多线程
     * @param context
     */
    private void testRun(Map<String, String> context){
      new Thread(()->  {
          LogUtil.createMDC(context);
          LogUtil.info("========>线程1");
      }).start();

      new Thread(()->  {
          LogUtil.createMDC(context);
      LogUtil.info("========>线程2");}).start();
    }
}
