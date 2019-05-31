package com.common.admin.controller;

import com.common.admin.common.AdminResponse;
import com.common.admin.utils.ExcelUtil;
import com.common.admin.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
        int a = 1/0;
        LogUtil.setNoFormatLogName("test1");
        LogUtil.info("========>不带日期格式");
        return AdminResponse.success();
    }
    @RequestMapping("/test2")
    @ResponseBody
    public AdminResponse test22(Integer id) {
        Long start = System.currentTimeMillis();
        for(int i = 0; i<50000;i++) {
            LogUtil.info("========>id = " + id);
            LogUtil.warn("========>warn");
            LogUtil.error("========>error");

            testRun(LogUtil.getMDC());
        }
        Long time = System.currentTimeMillis()-start;
        LogUtil.error("========>time="+time);
        return AdminResponse.success("hahahhahhahah");
    }

    /**
     * 多线程
     * @param context
     */
    private void testRun(Map<String, String> context){
      new Thread(()->  {
          LogUtil.info("========>线程1");
      }).start();

      new Thread(()->  {
          LogUtil.createMDC(context);
      LogUtil.info("========>线程2");}).start();
        new Thread(()->  {
            LogUtil.setFormatLogName("test");
            LogUtil.info("========>线程3-打印到test");
            LogUtil.error("========================================================");}).start();
        new Thread(()->  {
            LogUtil.setNoFormatLogName("test1");
            LogUtil.info("========>线程4-打印到test1");
            LogUtil.error("========================================================");}).start();
    }

    @RequestMapping("/export")
    public void export(HttpServletResponse response) {
        String fileName = "test";
        List<Map<String,Object>> datas = new ArrayList<>();
        testExport(datas);
        try {
            ExcelUtil.generateExcel(response,fileName,datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testExport(List<Map<String,Object>> datas){
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("第一列","1");
        data.put("第二列","2");
        data.put("第三列","3");
        datas.add(data);
        Map<String,Object> data2 = new LinkedHashMap<>();
        data2.put("第一列","123");
        data2.put("第二列",123.34);
        data2.put("第三列",true);
        datas.add(data2);
    }
}
