package com.common.admin.controller;

import com.common.admin.common.BaseResponse;
import com.common.admin.common.ThreadPoolManager;
import com.common.admin.utils.ExcelUtil;
import com.common.admin.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public BaseResponse test() {
        LogUtil.info("a = {},b={}","a","b");
        LogUtil.info("========>不带日期格式");
        return BaseResponse.success();
    }
    @RequestMapping("/test2")
    @ResponseBody
    public BaseResponse test22(Integer id) {
        Long start = System.currentTimeMillis();
        for(int i = 0; i<200000;i++) {
            LogUtil.error("========>日志1");
            LogUtil.info("========>日志{2}");
            testRun();
        }
        Long time = System.currentTimeMillis()-start;
        LogUtil.error("========>time="+time);
        return BaseResponse.success("hahahhahhahah");
    }

    /**
     * 多线程
     * @param
     */
    private void testRun(){
        ThreadPoolManager.getsInstance().execute(() ->{
            LogUtil.setLogName("test");
            LogUtil.error("========>线程1");
            LogUtil.info("========>线程1{2}");
        });
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
