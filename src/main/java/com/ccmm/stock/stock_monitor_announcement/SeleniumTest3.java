package com.ccmm.stock.stock_monitor_announcement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午12:27 <br>
 */
public class SeleniumTest3 {
	/** 
	 * 获得不加载 css，图片，flash的浏览器 
	 * @return 
	 */  
	public static WebDriver getNoResouceWebDriver(){  
	    FirefoxProfile firefoxProfile = new FirefoxProfile();  
	    //去掉css  
	    firefoxProfile.setPreference("permissions.default.stylesheet", 2);  
	    //去掉图片  
	    firefoxProfile.setPreference("permissions.default.image", 2);  
	    //去掉flash  
	    firefoxProfile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", false);  
	    return new FirefoxDriver(firefoxProfile);  
	} 
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
    	// 如果你的 FireFox 没有安装在默认目录，那么必须在程序中设置
    	System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox_v28\\firefox.exe");

    	WebDriver driver = getNoResouceWebDriver();
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // 让浏览器访问 Baidu
        //driver.get("http://www.sse.com.cn/disclosure/listedinfo/announcement/");
    	driver.get("http://www.sse.com.cn/home/search/?webswd=%E6%9D%83%E7%9B%8A%E5%88%86%E6%B4%BE");
        // 用下面代码也可以实现
        // driver.navigate().to("http://www.sse.com.cn/home/search/?webswd=%E6%9D%83%E7%9B%8A%E5%88%86%E6%B4%BE");
        // 获取 网页的 title
        System.out.println("1 Page title is: " + driver.getTitle());
        Thread.sleep(5000);
        
        System.out.println(driver.getPageSource().contains("权益分派实施公告"));
        List<String> ll =new LinkedList();
        ll.add(driver.getPageSource());
        IOUtils.writeLines(ll, null, new FileOutputStream("C:\\Users\\cc\\Desktop\\test.txt"));   
        
        
        //1.点击下一页按钮
        String xpathExpression="//button[@class='btn btn-default navbar-btn next-page']";
        WebElement element = driver.findElement(By.xpath(xpathExpression));
        element=driver.findElement(By.className("next-page"));
        //element.click();
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);

        Thread.sleep(5000);
        System.out.println(driver.getPageSource().contains("江苏银行"));
        ll =new LinkedList();
        ll.add(driver.getPageSource());
        IOUtils.writeLines(ll, null, new FileOutputStream("C:\\Users\\cc\\Desktop\\tes1t.txt"));  
        //FileUtils.writeLines(new FileOutputStream("C:\\Users\\cc\\Desktop\\test.txt"), "UTF-8", (Collection<String>)ll);   
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(new ExpectedCondition<WebElement>() 
//        {
//          public WebElement apply(WebDriver d) 
//          {
//            return d.findElement(By.cssSelector(".blue_box"));
//          }
//        });
        
//        // 通过 id 找到 input 的 DOM
//        WebElement element = driver.findElement(By.id("kw"));
//
//        // 输入关键字
//        element.sendKeys("zTree");
//
//        // 提交 input 所在的  form
//        element.submit();
//        
//        // 通过判断 title 内容等待搜索页面加载完毕，Timeout 设置10秒
//
//        // 显示搜索结果页面的 title
//        System.out.println("2 Page title is: " + driver.getTitle());
//        
        //关闭浏览器
        //driver.quit();
    }
}
