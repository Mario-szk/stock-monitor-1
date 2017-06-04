package com.ccmm.stock.stock_monitor_announcement.pdf;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccmm.stock.stock_monitor_announcement.mysql.AnnounceInfoMySQLTools;

/***
 * 解析公告的工具类
 * @author cc
 *
 */
public class AnnouncementParserTools {

	public String announcementContent=""; //公告的内容

	
	/***
	 * 从公告里获得股票编码
	 * @return
	 */
	public String parserStockCode(){
		String code="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得证券代码的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				String content = announcementContent.replaceAll("\r\n", "");
				content=content.replaceAll(" ", "");
				int index = content.indexOf("权益分派实施公告");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			
			
			//2.正则表达式解析code
			Pattern p=Pattern.compile("证券代码[:：]\\d{6}"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				code=m.group();
				code=code.replaceAll("证券代码", "");
				code=code.replaceAll(":", "");
				code=code.replaceAll("：", "");
			}else {
				//匹配不到证券代码，匹配A股代码
				p=Pattern.compile("A股代码[:：]\\d{6}"); 
				m=p.matcher(contentSeg); 
				if(m.find()){
					code=m.group();
					code=code.replaceAll("A股代码", "");
					code=code.replaceAll(":", "");
					code=code.replaceAll("：", "");
					
				}else{
					p=Pattern.compile("股票代码[:：]\\d{6}"); 
					m=p.matcher(contentSeg); 
					if(m.find()){
						code=m.group();
						code=code.replaceAll("股票代码", "");
						code=code.replaceAll(":", "");
						code=code.replaceAll("：", "");
						
					}
				}
			}
		}
		return code;
	}
	
	
	/***
	 * 从公告里获得股票名称
	 * @return
	 */
	public String parserStockName(){
		String name="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得证券代码的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				String content = announcementContent.replaceAll("\r\n", "");
				content=content.replaceAll(" ", "");
				int index = content.indexOf("权益分派实施公告");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			
			
			//2.正则表达式解析code
			Pattern p=Pattern.compile("证券简称[:：][a-zA-Z0-9_\u4e00-\u9fa5]{3,4}"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				name=m.group();
				name=name.replaceAll("证券简称", "");
				name=name.replaceAll(":", "");
				name=name.replaceAll("：", "");
			}else {
				//匹配不到证券代码，匹配A股代码
				p=Pattern.compile("A股简称[:：][a-zA-Z0-9_\u4e00-\u9fa5]{3,4}"); 
				m=p.matcher(contentSeg); 
				if(m.find()){
					name=m.group();
					name=name.replaceAll("A股简称", "");
					name=name.replaceAll(":", "");
					name=name.replaceAll("：", "");
					
				}else{
					p=Pattern.compile("股票简称[:：][a-zA-Z0-9_\u4e00-\u9fa5]{3,4}"); 
					m=p.matcher(contentSeg); 
					if(m.find()){
						name=m.group();
						name=name.replaceAll("股票简称", "");
						name=name.replaceAll(":", "");
						name=name.replaceAll("：", "");
						
					}
				}
			}
		}
		return name;
	}
	
	/***
	 * 从公告里获得每股的现金分红
	 * @return
	 */
	public String parserStrockDividendsMoney(){
		String money="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得现金分红的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				int index = announcementContent.indexOf("每股分配比例");
				if(index>0 && index>start){
					start=index;
				}else{
					return "";
				}
				index = announcementContent.indexOf("相关日期");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
				
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			//System.out.println(contentSeg);
			contentSeg=contentSeg.replace(" ", "");
			//A股每股现金红利0.04元
			//2.正则表达式解析code
			Pattern p=Pattern.compile("A股每股现金红利([0-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])元"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				money=m.group();
				money=money.replaceAll("A股每股现金红利", "");
				money=money.replaceAll("元", "");
			}
		}
		return money;
	}
	
	/***
	 * 从公告里获得 发布公告的日期
	 * @return
	 */
	public String parserStockPublishDate(){
		return "";
	}
	
	/***
	 * 从公告里获得股权登记日
	 * @return
	 */
	public String parserStockRegisterDate(){
		String date="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得现金分红的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				int index = announcementContent.indexOf("相关日期");
				if(index>0 && index>start){
					start=index+6;
				}else{
					return "";
				}
				index = announcementContent.indexOf("一、");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
				
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			//System.out.println(contentSeg);
			
			
			int index = contentSeg.indexOf("A股");
			if(index==-1){
				index = contentSeg.indexOf("Ａ股");
			}
			//List<String> contentList = new LinkedList<String>();
			String Beforecontent = contentSeg.substring(0,index-2);
			String afterContent = contentSeg.substring(index-2);
			Beforecontent=Beforecontent.replace("\r\n", "");
			contentSeg=Beforecontent+afterContent;
			String contentList[] = contentSeg.split("\n");
			int i=0;int flagIndex=-1;
			for(String str:contentList){
				//System.out.println(i+"\t"+str);
				if(str.contains("股权登记日")){
					flagIndex=i;break;
				}
				i++;
			}
			
			if(flagIndex>=0&&flagIndex<contentList.length-1){
				String flagStr = contentList[flagIndex];
				String dataStr = contentList[flagIndex+1];
				
				String ss[] = flagStr.split(" ");
				String ss1[] = dataStr.split(" ");
				int j=0;
				for(String s:ss){
					if(s.equals("股权登记日")){
						date=ss1[j];
					}
					j++;
				}
				//System.out.println(ss[1]);
			}
		}
		return date;
	}
	
	/***
	 * 从公告里获得  除权（息）日
	 * @return
	 */
	public String parserStockDiveceDate(){
		String date="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得现金分红的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				int index = announcementContent.indexOf("相关日期");
				if(index>0 && index>start){
					start=index+6;
				}else{
					return "";
				}
				index = announcementContent.indexOf("一、");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
				
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			//System.out.println(contentSeg);
			
			int index = contentSeg.indexOf("A股");
			if(index==-1){
				index = contentSeg.indexOf("Ａ股");
			}
			//List<String> contentList = new LinkedList<String>();
			String Beforecontent = contentSeg.substring(0,index-2);
			String afterContent = contentSeg.substring(index-2);
			Beforecontent=Beforecontent.replace("\r\n", "");
			contentSeg=Beforecontent+afterContent;
			String contentList[] = contentSeg.split("\n");
			int i=0;int flagIndex=-1;
			for(String str:contentList){
				//System.out.println(i+"\t"+str);
				if(str.contains("除权（息）日")){
					flagIndex=i;break;
				}
				i++;
			}
			
			if(flagIndex>=0&&flagIndex<contentList.length-1){
				String flagStr = contentList[flagIndex];
				String dataStr = contentList[flagIndex+1];
				
				String ss[] = flagStr.split(" ");
				String ss1[] = dataStr.split(" ");
				int j=0;
				for(String s:ss){
					if(s.equals("除权（息）日")){
						date=ss1[j];
					}
					j++;
				}
				//System.out.println(ss[1]);
			}
		}
		return date;
	}
	
	/***
	 * 从公告里获得  现金红利发放日
	 * @return
	 */
	public String parserStockGiveMoneyDate(){
		String date="";
		if(announcementContent!=null && !announcementContent.isEmpty()){
			//System.out.println(announcementContent);
			
			//1.获得现金分红的内容片段
			int start=0;
			int end=announcementContent.length();
			try{
				int index = announcementContent.indexOf("相关日期");
				if(index>0 && index>start){
					start=index+6;
				}else{
					return "";
				}
				index = announcementContent.indexOf("一、");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
				
			}catch(Exception e){}
			
			String contentSeg=announcementContent.substring(start, end);
			//System.out.println(contentSeg);
			
			int index = contentSeg.indexOf("A股");
			if(index==-1){
				index = contentSeg.indexOf("Ａ股");
			}
			//List<String> contentList = new LinkedList<String>();
			String Beforecontent = contentSeg.substring(0,index-2);
			String afterContent = contentSeg.substring(index-2);
			Beforecontent=Beforecontent.replace("\r\n", "");
			contentSeg=Beforecontent+afterContent;
			String contentList[] = contentSeg.split("\n");
			int i=0;int flagIndex=-1;
			for(String str:contentList){
				//System.out.println(i+"\t"+str);
				if(str.contains("现金红利发放日")){
					flagIndex=i;break;
				}
				i++;
			}
			
			if(flagIndex>=0&&flagIndex<contentList.length-1){
				String flagStr = contentList[flagIndex];
				String dataStr = contentList[flagIndex+1];
				
				String ss[] = flagStr.split(" ");
				String ss1[] = dataStr.split(" ");
				int j=0;
				for(String s:ss){
					if(s.equals("现金红利发放日")){
						date=ss1[j];
					}
					j++;
				}
				//System.out.println(ss[1]);
			}
		}
		return date;
	}
	
	/**
	 * 从公告里 解析出信息
	 * @return
	 */
	public static StockAnnounceInfo  parserInfo(String announce){
		StockAnnounceInfo info = new StockAnnounceInfo();
		
		AnnouncementParserTools tools = new AnnouncementParserTools();
		tools.setAnnouncementContent(announce);
		
		
		String str =tools.parserStockCode();
		info.setCode(str);
		str =tools.parserStockName();
		info.setName(str);
		
		str =tools.parserStockDiveceDate();
		info.setDiveDate(str);
		
		str =tools.parserStrockDividendsMoney();
		info.setDividendsMoney(str);

		str =tools.parserStockGiveMoneyDate();
		info.setGiveMoneyDate(str);
		
		str=tools.parserStockRegisterDate();
		info.setRegisterDate(str);
		
		try{
			StockData data = GetDataFromSinaUtil.getStockCsvData(info.getCode());
			double closePrice=data.getClose();
			info.setStockMoney(closePrice+"");
			info.setStockDate(data.getDate());
			
			double Rate=(Double.parseDouble(info.getDividendsMoney()))/(Double.parseDouble(info.getStockMoney()));
			info.setDividendRate((int)(Rate*100*100)+"");
		}catch(Exception e){}	

		return info;
	}
	
	
	/***
	 * 根据证券号，获得当前的股票价格
	 * @param code
	 * @return
	 */
	public static String getNowStockPrice(String code){
		
		return "";
	}
	
	public String getAnnouncementContent() {
		return announcementContent;
	}

	public void setAnnouncementContent(String announcementContent) {
		this.announcementContent = announcementContent;
	}
	
	
	private static  List<String> getDirectory(File file) {
		  File flist[] = file.listFiles();
		  List<String> listFile = new LinkedList<String>();
		  if (flist == null || flist.length == 0) {
		      return listFile;
		  }
		  for (File f : flist) {
		      if (f.isDirectory()) {
		          //这里将列出所有的文件夹
		      } else {
		         //这里将列出所有的文件
		    	  if(f.getAbsolutePath().endsWith(".pdf"))
		    		  listFile.add(f.getAbsolutePath());
		      }
		  }
		  return listFile;
		}
	public static void main(String argvs[]){
		String path1=argvs[0];
		PdfboxUtil pdfutil = new PdfboxUtil();
		List<String> path = getDirectory(new File(path1));
		for(String pdfPath:path){
			try {
				StockAnnounceInfo info = AnnouncementParserTools.parserInfo(pdfutil.getTextFromPdf(pdfPath));
				AnnounceInfoMySQLTools.saveInfoToMysql(info);
				System.out.println(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(pdfPath);
				e.printStackTrace();
			}
		 }
		


	}
	
}
