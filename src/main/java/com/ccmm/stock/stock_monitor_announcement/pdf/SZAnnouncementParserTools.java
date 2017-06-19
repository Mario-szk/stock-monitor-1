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
public class SZAnnouncementParserTools {

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
			String content = announcementContent;
			try{
				content = announcementContent.replaceAll("\r\n", "");
				content=content.replaceAll(" ", "");
				
			}catch(Exception e){}
			
			String contentSeg=content;
			contentSeg=contentSeg.replace("\n", "");
			
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
						
					}else{
						
						p=Pattern.compile("证券号码[:：]\\d{6}"); 
						m=p.matcher(contentSeg); 
						if(m.find()){
							code=m.group();
							code=code.replaceAll("证券号码", "");
							code=code.replaceAll(":", "");
							code=code.replaceAll("：", "");
						}
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
			String content=announcementContent;
			try{
				content = announcementContent.replaceAll("\r\n", "");
				
				content=content.replaceAll(" ", "");
				int index = content.indexOf("权益分派实施公告");
				if(index>0 && index<end){
					end=index;
				}else{
					return "";
				}
			}catch(Exception e){}
			
			String contentSeg=content.substring(start, end);
			contentSeg=contentSeg.replace("\n", "");
			
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
			
			String contentSeg=announcementContent;
			//System.out.println(contentSeg);
			contentSeg=contentSeg.replace(" ", "");
			contentSeg=contentSeg.replace("\r\n", "");
			contentSeg=contentSeg.replace("\n", "");
			//向全体股东每10股派2.00元人民币现金
			//2.正则表达式解析code
			Pattern p=Pattern.compile("(向全体股东每10股派([0-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])元)"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				money=m.group();
				//System.out.println(m.group(0));
				//System.out.println(m.group(1));
				money=money.replaceAll("向全体股东每10股派", "");
				money=money.replaceAll("元", "");
				double moneyDouble = Double.parseDouble(money);
				moneyDouble=moneyDouble/10;
				money=moneyDouble+"";
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
			String contentSeg=announcementContent.replace(" ", "");
			contentSeg=contentSeg.replace("\r\n", "");
			contentSeg=contentSeg.replace("\n", "");
			Pattern p=Pattern.compile("股权登记日(为)：\\d{4}(年)\\d{1,2}(月)\\d{1,2}日"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				date=m.group();
				date=date.replaceAll("股权登记日", "");
				date=date.replaceAll("为", "");
				date=date.replaceAll("：", "");
				date=date.replaceAll("年", "-");
				date=date.replaceAll("月", "-");
				date=date.replaceAll("日", "");
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
			String contentSeg=announcementContent.replace(" ", "");
			contentSeg=contentSeg.replace("\r\n", "");
			contentSeg=contentSeg.replace("\n", "");
			Pattern p=Pattern.compile("除权除息日(为)：\\d{4}(年)\\d{1,2}(月)\\d{1,2}日"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				date=m.group();
				date=date.replaceAll("除权除息日", "");
				date=date.replaceAll("为", "");
				date=date.replaceAll("：", "");
				date=date.replaceAll("年", "-");
				date=date.replaceAll("月", "-");
				date=date.replaceAll("日", "");
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
			String contentSeg=announcementContent.replace(" ", "");
			contentSeg=contentSeg.replace("\r\n", "");
			contentSeg=contentSeg.replace("\n", "");
			Pattern p=Pattern.compile("红利发放日(为)：\\d{4}(年)\\d{1,2}(月)\\d{1,2}日"); 
			Matcher m=p.matcher(contentSeg); 
			if(m.find()){
				date=m.group();
				date=date.replaceAll("红利发放日", "");
				date=date.replaceAll("为", "");
				date=date.replaceAll("：", "");
				date=date.replaceAll("年", "-");
				date=date.replaceAll("月", "-");
				date=date.replaceAll("日", "");
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
		
		SZAnnouncementParserTools tools = new SZAnnouncementParserTools();
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
		info.setGiveMoneyDate(str.isEmpty()?info.getDiveDate():str);
		
		str=tools.parserStockRegisterDate();
		info.setRegisterDate(str);
		
		try{
			StockData data = GetDataFromSinaUtil.getStockCsvData(info.getCode());
			double closePrice=data.getClose();
			info.setStockMoney(closePrice+"");
			info.setStockDate(data.getDate());
			
			double mon = Double.parseDouble(info.getStockMoney());
			if (Double.compare(mon, new Double(0.0)) == 0) {
				double Rate=new Double(0.0);
				info.setDividendRate("0");
				return info;
			}else{
				
			}
			
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
		    	  if(f.getAbsolutePath().endsWith(".PDF"))
		    		  listFile.add(f.getAbsolutePath());
		      }
		  }
		  return listFile;
		}
	public static void main(String argvs[]){
		String path1=argvs[1];
		//String path1="C:\\Users\\cc\\Desktop\\pdfResult";
		PdfboxUtil pdfutil = new PdfboxUtil();
		List<String> path = getDirectory(new File(path1));
		for(String pdfPath:path){
			try {
				StockAnnounceInfo info = SZAnnouncementParserTools.parserInfo(pdfutil.getTextFromPdf(pdfPath));
				//StockAnnounceInfo info = SZAnnouncementParserTools.parserInfo(pdfutil.getTextFromPdf("C://Users//cc//Desktop//1203624859.PDF"));
				System.out.println(pdfPath);
				System.out.println(info);
				System.out.println();
				AnnounceInfoMySQLTools.saveInfoToMysql(info);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(pdfPath);
				e.printStackTrace();
			}
		 }
		


	}
	
}
