package com.common.admin.utils;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: liujianqiang
 * @Date: 2019-01-23
 * @Description:
 */
public class ExcelUtil {

    public static void generateExcel(HttpServletResponse response, String fileName,List<Map<String,Object>> datas) throws IOException {
        //创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建表单
        XSSFSheet sheet = genSheet(workbook, fileName);

        //创建Excel
        genExcel(sheet, datas);

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + fileName+"_" +System.currentTimeMillis()+ ".xlsx");

        //刷新缓冲
        response.flushBuffer();

        //将工作薄写入文件输出流中
        workbook.write(response.getOutputStream());
        //文本文件输出流，释放资源
    }

    public static void genExcel(XSSFSheet sheet, List<Map<String,Object>> datas) {
        XSSFRow  title = sheet.createRow(0);
        XSSFCell tCell;
        XSSFRow row ;
        XSSFCell cell;
       for(int i = 0; i< datas.size(); i++) {
           row = sheet.createRow(i+1);
           Map<String, Object> data = datas.get(i);
           if(data.isEmpty()){
               return;
           }
           int j = 0;
           for(Map.Entry<String, Object> entry : data.entrySet()){
               //创建列
               cell = row.createCell(j);
               //数据库列名做excel标题名
               if(i == 0){
                   tCell = title.createCell(j);
                   tCell.setCellValue(entry.getKey());
                   cell.setCellValue(entry.getValue().toString());
               }else {
                   cell.setCellValue(entry.getValue().toString());
               }
               j++;
           }
       }
    }

    /**
     *  设置表单，并生成表单
     */
    public static XSSFSheet genSheet(XSSFWorkbook workbook, String sheetName) {
        //生成表单
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //设置表单文本居中
        sheet.setHorizontallyCenter(true);
        sheet.setFitToPage(false);
        //打印时在底部右边显示文本页信息
        Footer footer = sheet.getFooter();
        footer.setRight("Page " + HeaderFooter.numPages() + " Of " + HeaderFooter.page());
        //打印时在头部右边显示Excel创建日期信息
        Header header = sheet.getHeader();
        header.setRight("Create Date " + HeaderFooter.date() + " " + HeaderFooter.time());
        //设置打印方式
        XSSFPrintSetup ps = sheet.getPrintSetup();
        // true：横向打印，false：竖向打印 ，因为列数较多，推荐在打印时横向打印
        ps.setLandscape(true);
        //打印尺寸大小设置为A4纸大小
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        return sheet;
    }
}