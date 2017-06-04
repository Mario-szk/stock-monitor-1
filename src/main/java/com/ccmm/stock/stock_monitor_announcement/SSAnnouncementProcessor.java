package com.ccmm.stock.stock_monitor_announcement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
 * 上证公告抽取器。<br>
 * 使用Selenium做页面动态渲染。<br>
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午4:08 <br>
 */
public class SSAnnouncementProcessor implements PageProcessor {

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
    	
        if (page.getUrl().toString().endsWith("pdf")) {
        	//pdf公告信息
        	System.out.println("pdf信息："+page.getUrl());
            //page.putField("img", page.getHtml().xpath("//div[@class='image-holder']/a/img/@src").toString());
        	String urlsSeg[] = page.getUrl().toString().split("/");
        	String name = urlsSeg[urlsSeg.length-1];
        	String dataStr = name.split("_")[1];
        	DateFormat format1 = new SimpleDateFormat("yyyyMMdd"); 
        	try {    
                Date nowdate = format1.parse(dataStr);   
            	if(nowdate.after(lastdate)){
            		PdfDownloaderUtil.downloaderPDF(page.getUrl().toString(), filePath+"//"+name);
        		}
    		 } catch (ParseException e) {    
    		    e.printStackTrace();    
    		 }
        } else {
        	int i=0;
        	while(true){
	        	//公告列表信息
	        	//System.out.println("公告信息:"+page.getUrl());
	        	
	        	//1.添加公告信息
	        	List<String> targetLinks =page.getHtml().links().regex("/disclosure/listedinfo/announcement/c/.*pdf").all();
	        	//System.out.println(targetLinks.size());
	        	List<String> crawlLinks = new LinkedList<String>();
	        	boolean nextPage=true;

	        	for(String str:targetLinks){
	        		
		        	if(endDate!=null && !endDate.isEmpty()){
		        		System.out.println(str+"\t"+endDate);
		        		if(str.contains("_"+endDate+"_")){
		        			nextPage=false;
		        			
		        			continue;
		        		}
		        	}
		        	crawlLinks.add("http://www.sse.com.cn"+str);
	        	}
	            page.addTargetRequests(crawlLinks);
	            
	            //2.不保存这个页面信息
	            page.getResultItems().setSkip(true);
	            
	            //3.获得下一页按钮位置,点击下一页
	            System.out.println("nextPage:"+nextPage);
	            if(!nextPage) break;
	            if(downlader!=null && downlader.webDriver!=null){
	                WebElement element=downlader.webDriver.findElement(By.className("next-page"));
	                ((JavascriptExecutor)downlader.webDriver).executeScript("arguments[0].click();", element);
	            }
	            
	            //4.更新page
	            WebElement webElement = downlader.webDriver.findElement(By.xpath("/html"));
	    		String content = webElement.getAttribute("outerHTML");
	    		page.setRawText(content);
	    		page.setHtml(new Html(content));
	            i++;
	            if(i>10) break;
        	}
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	
    	
    	String url="http://www.sse.com.cn/home/search/?webswd=%E6%9D%83%E7%9B%8A%E5%88%86%E6%B4%BE";
    	int sleepTime=5000;
		String firefoxBinPath="C:\\Program Files (x86)\\Mozilla Firefox_v28\\firefox.exe";
    	filePath="C:\\Users\\cc\\Desktop\\result";
    	endDate="20170525";
    	try{
    		firefoxBinPath=args[0];
        	filePath=args[1];
        	endDate=args[2];
    	}catch(Exception e){}
    	DateFormat format1 = new SimpleDateFormat("yyyyMMdd");    
    	try {    
            lastdate = format1.parse(endDate);   
		} catch (ParseException e) {    
		            e.printStackTrace();    
		}
    	System.out.println(firefoxBinPath);
    	System.out.println(filePath);
    	System.out.println(endDate);
    	downlader = new SeleniumDownloader(firefoxBinPath).setSleepTime(sleepTime);
    	
        Spider.create(new SSAnnouncementProcessor()).thread(1)
                .addPipeline(new FilePipeline(filePath))
                .setDownloader(downlader)
                .addUrl(url)
                .run();
    }
}
