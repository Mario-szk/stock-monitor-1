package com.ccmm.stock.stock_monitor_announcement.pdf;

/**
* 股票实体类
* @author 祁丛生 2013.01.02
*/
public class StockData {
   private String code; // 股票编码
   private String name; // 股票名称
   private String date; // 交易日期
   private double open = 0.0; // 开盘价
   private double high = 0.0; // 最高价
   private double low = 0.0; // 最低价
   private double close = 0.0; // 最后一次交易价格，相当于收盘价
   private double volume = 0.0;// 总交易手
   private double adj = 0.0; // 最后一次交易价格 (今天的收盘价当做加权价格)
   
   public String toString(){
	   String result="StockData [";
	   result+=("code="+code+",");
	   result+=("date="+date+",");
	   result+=("open="+open+",");
	   result+=("high="+high+",");
	   result+=("low="+low+",");
	   result+=("close="+close+",");
	   result+=("volume="+volume+",");
	   result+=("adj="+adj+",");
       result+="]";
       return result;
   }
   public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public double getOpen() {
	return open;
}
public void setOpen(double open) {
	this.open = open;
}
public double getHigh() {
	return high;
}
public void setHigh(double high) {
	this.high = high;
}
public double getLow() {
	return low;
}
public void setLow(double low) {
	this.low = low;
}
public double getClose() {
	return close;
}
public void setClose(double close) {
	this.close = close;
}
public double getVolume() {
	return volume;
}
public void setVolume(double volume) {
	this.volume = volume;
}
public double getAdj() {
	return adj;
}
public void setAdj(double adj) {
	this.adj = adj;
}


}





