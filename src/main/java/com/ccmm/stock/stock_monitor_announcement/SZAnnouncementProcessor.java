package com.ccmm.stock.stock_monitor_announcement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 深圳公告抽取器。<br>
 * 使用Selenium做页面动态渲染。<br>
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午4:08 <br>
 */
public class SZAnnouncementProcessor implements PageProcessor {

    private Site site= Site.me().setRetryTimes(3);
    private static String endDate="";
    private static Date lastdate=null;
    private static String filePath=null;
    static SeleniumDownloader downlader = null;
 
    
    
    //private Site site=null;
    @Override
    public void process(Page page) {
    	//System.out.println(page.getRawText().contains("权益分派"));
    	//System.out.println(page.getUrl());
    	
        if (page.getUrl().toString().endsWith("PDF")) {
        	
        	//pdf公告信息
        	System.out.println("pdf信息："+page.getUrl());
            //page.putField("img", page.getHtml().xpath("//div[@class='image-holder']/a/img/@src").toString());
        	String urlsSeg[] = page.getUrl().toString().split("/");
        	String name = urlsSeg[urlsSeg.length-1];        	
        	PdfDownloaderUtil.downloaderPDF(page.getUrl().toString(), filePath+"//"+name);

        	
        } else if(page.getUrl().toString().endsWith("drgg.htm")){
        	//1. 公告的首页，需要输入关键字，选择公告日期，点击确定
        	//1.1 输入搜索关键字
        	WebElement webElement = downlader.webDriver.findElement(By.name("keyword"));
        	webElement.clear();
        	webElement.sendKeys("权益分派实施公告");
        	//1.2选择日期

        	//String js = "$('input[id=txtBeginDate]').attr('readonly','')" ;
        	//downlader.webDriver.execute_script(js);
        	webElement=downlader.webDriver.findElement(By.name("startTime"));
        	webElement.clear();
        	webElement.sendKeys(endDate);		
//        	webElement=downlader.webDriver.findElement(By.name("endTime"));
//        	webElement.clear();
//        	webElement.sendKeys("2016-06-12");
        	
        	//1.3点击确定按钮
        	webElement = downlader.webDriver.findElement(By.name("imageField"));
        	//webElement.submit();
        	((JavascriptExecutor)downlader.webDriver).executeScript("arguments[0].click();", webElement);
            //4.更新page
            webElement = downlader.webDriver.findElement(By.xpath("/html"));
    		String content = webElement.getAttribute("outerHTML");
    		page.setRawText(content);
    		page.setHtml(new Html(content));
        	
        	//2.0 解析公告链接
        	while(true){
        		//System.out.println(page.getHtml());
        		//1.添加公告信息
        		List<String> targetLinks =page.getHtml().links().regex(".*PDF").all();       		
        		List<String> crawlLinks = new LinkedList<String>();
	        	for(String str:targetLinks){
	        		//System.out.println(str);
		        	crawlLinks.add("http://disclosure.szse.cn/"+str);
	        	}
	            page.addTargetRequests(crawlLinks);

        		//2.获得下一页按钮
	            //<td>当前第 <span>2</span> 页  共 <span>13</span> 页</td>
	            Pattern p1=Pattern.compile("当前第 <span>\\d+</span> 页"); 
	            Matcher m1=p1.matcher(page.getHtml().toString());
	            String currentPage ="";
	            if(m1.find()){
	            	 currentPage = m1.group(0).replaceAll("当前第 <span>", "").replaceAll("</span> 页", "");
	            	 //System.out.println(currentPage+"\t");
	            };
	            
	            Pattern p2=Pattern.compile("共 <span>\\d+</span> 页"); 
	            Matcher m2=p2.matcher(page.getHtml().toString());
	            String totalPage ="";
	            if(m2.find()){
	            	totalPage = m2.group(0).replaceAll("共 <span>", "").replaceAll("</span> 页", "");
	            	 //System.out.println(totalPage+"\t");
	            };
	            int current=0;
	            int total=0;
	            try{
	            	current=Integer.parseInt(currentPage);
	            	total=Integer.parseInt(totalPage);
	            }catch(Exception e){break;}
	           
	            System.out.println(currentPage+"\t"+totalPage);
	            if(current>=total) break;
	            WebElement element=downlader.webDriver.findElement(By.className("input_top_bar"));
	            element.sendKeys((current+1)+"");
	            element.sendKeys("\n");
                //((JavascriptExecutor)downlader.webDriver).executeScript("arguments[0].click();", element);
	            //4.更新page
	            webElement = downlader.webDriver.findElement(By.xpath("/html"));
	    		content = webElement.getAttribute("outerHTML");
	    		page.setRawText(content);
	    		page.setHtml(new Html(content));
        	}
        	
        }
        
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	
    	
    	String url="http://disclosure.szse.cn/m/drgg.htm";
    	int sleepTime=5000;
		String firefoxBinPath="C:\\Program Files (x86)\\Mozilla Firefox_v28\\firefox.exe";
    	filePath="C:\\Users\\cc\\Desktop\\result";
    	endDate="20170616";
    	try{
    		firefoxBinPath=args[0];
        	filePath=args[1];
        	endDate=args[2];
    	}catch(Exception e){}
    	DateFormat format1 = new SimpleDateFormat("yyyyMMdd");    
    	try {    
            lastdate = format1.parse(endDate);   
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	endDate = sdf.format(lastdate);
		} catch (ParseException e) {    
		            e.printStackTrace();    
		}
    	System.out.println(firefoxBinPath);
    	System.out.println(filePath);
    	System.out.println(endDate);
    	downlader = new SeleniumDownloader(firefoxBinPath).setSleepTime(sleepTime);
    	
        Spider.create(new SZAnnouncementProcessor()).thread(1)
                .addPipeline(new FilePipeline(filePath))
                .setDownloader(downlader)
                .addUrl(url)
                .run();
    }
}
