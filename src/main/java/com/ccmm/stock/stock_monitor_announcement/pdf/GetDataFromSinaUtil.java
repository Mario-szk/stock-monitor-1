package com.ccmm.stock.stock_monitor_announcement.pdf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDataFromSinaUtil {
	
	 public static final String SINA_FINANCE_URL_SH ="http://hq.sinajs.cn/list=sh";
	 public static final String SINA_FINANCE_URL_SZ ="http://hq.sinajs.cn/list=sz";
	   /**
	    * 根据 股票编码、日期 获取股票数据
	    * @author 祁丛生
	    * @param stockName   沪市：000000.ss 深市：000000.sz
	    * @param date 日期
	    * @return StockData
	    */
	   public static StockData getStockCsvData(String stockName){
		   String urlStr = null;
		   if(stockName.startsWith("0")){
			   urlStr = SINA_FINANCE_URL_SZ+stockName;
		   }else if(stockName.startsWith("6")){
			   urlStr = SINA_FINANCE_URL_SH+stockName;
		   }else{
			   return null;
		   }
		   String result=WebPageUtil.getHtmlByURL(urlStr,"GBK");
	        
	        try{
		        String ss[] = result.split("=");
		        ss[1]=ss[1].replaceAll("\"", "");
		        ss[1]=ss[1].replaceAll(";", "");
		        String sss[] = ss[1].split(",");
		        
		        StockData stock= new StockData();
		        stock.setAdj(0);
		        stock.setClose(Double.parseDouble(sss[3]));
		        stock.setCode(stockName);
		        stock.setDate(sss[30]);
		        stock.setHigh(Double.parseDouble(sss[4]));
		        stock.setLow(Double.parseDouble(sss[5]));
		        stock.setName(sss[0]);
		        stock.setOpen(Double.parseDouble(sss[1]));
		        stock.setVolume(Integer.parseInt(sss[8])/100);
		        return stock;
	        }catch(Exception e){}
	        return null;

	   }
	   
	  
	   
}
