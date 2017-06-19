package com.ccmm.stock.stock_monitor_announcement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ccmm.stock.stock_monitor_announcement.email.JavaMailWithAttachment;
import com.ccmm.stock.stock_monitor_announcement.mysql.AnnounceInfoMySQLTools;
import com.ccmm.stock.stock_monitor_announcement.pdf.AnnouncementParserTools;
import com.ccmm.stock.stock_monitor_announcement.pdf.SZAnnouncementParserTools;
import com.ccmm.stock.stock_monitor_announcement.pdf.StockAnnounceInfo;

public class Main {
    public static String GetNowDate(){  
        String temp_str="";  
        Date dt = new Date();  
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        temp_str=sdf.format(dt);  
        return temp_str;  
    } 
    
    public static void removeDir(String path){
    	 try {  
    		 String command="rm -rf "+path+"/*";
             Process ps = Runtime.getRuntime().exec(command);  
             ps.waitFor(); 
             
             BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
             StringBuffer sb = new StringBuffer();  
             String line;  
             while ((line = br.readLine()) != null) {  
                 sb.append(line).append("\n");  
             }  
             String result = sb.toString();  
             System.out.println(result);  
                         
             System.out.println(command);
             }   
         catch (Exception e) {  
             e.printStackTrace();  
             }  
     }  
    
    
    /**
     * 
     * @param args [0]--firefox bin path  [1]--pdf result path  [2]--endDate
     */
	public static void main(String [] args){
		
//		String firefoxBinPath="C:\\Program Files (x86)\\Mozilla Firefox_v28\\firefox.exe";
//		String filePath="C:\\Users\\cc\\Desktop\\pdfResult";
//		String endDate="20170525";
//		String args111[] = {firefoxBinPath,filePath,endDate};
//		args=args111;
		//1.上海证券爬网页
		SSAnnouncementProcessor.main(args);
		System.out.println("【上海证券】爬网页结束");
		//2.解析pdf保存到mysql

		AnnouncementParserTools.main(args);
		System.out.println("【上海证券】解析pdf结束");
		
		//removeDir(args[1]);
		
		//1.
		SZAnnouncementProcessor.main(args);
		System.out.println("【深圳证券】爬网页结束");
		
		//2.
		SZAnnouncementParserTools.main(args);
		System.out.println("【深圳证券】解析pdf结束");
		
		//3.读mysql
		String date = GetNowDate();
		List<StockAnnounceInfo> list = AnnounceInfoMySQLTools.selectTop10InfoFromMysql(date);
		String content="";
		content+=String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
				"证券编码","证券简称","公告日期","除权(息)日","股权登记日","现金分派日","现金分红","股价查询时间","股价","分红率");
		for(StockAnnounceInfo info :list){
			content+=String.format("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",info.getCode(),info.getName(),info.getPublicDate(),
					info.getDiveDate(),info.getRegisterDate(),
					info.getGiveMoneyDate(),info.getDividendsMoney(),info.getStockDate(),
					info.getStockMoney(),(Integer.parseInt(info.getDividendRate()))/100.0+"%");
			
		}
		System.out.println(content);
		//4.发邮件
		try {
			JavaMailWithAttachment.main(new String[]{content});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
