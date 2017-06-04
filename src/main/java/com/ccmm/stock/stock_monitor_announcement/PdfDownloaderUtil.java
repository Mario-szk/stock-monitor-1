package com.ccmm.stock.stock_monitor_announcement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class PdfDownloaderUtil {
	
    public static void downloaderPDF (String url,String path) {
    	
    	
    	HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);

        HttpContext  httpContext=new BasicHttpContext();
    	
        System.out.println("[INFO] Download From : "+url);
        File file=new File(path);
        if(file.exists())file.delete();
        try {
            //使用file来写入本地数据
            file.createNewFile();
            FileOutputStream outStream = new FileOutputStream(path);
            
            //执行请求，获得响应
            HttpResponse httpResponse = httpClient.execute(httpGet,httpContext);
            
            System.out.println("[STATUS] Download : "+httpResponse.getStatusLine()+" [FROM] "+path);
            
            HttpEntity httpEntity=httpResponse.getEntity();
            InputStream inStream=httpEntity.getContent();
            while(true){//这个循环读取网络数据，写入本地文件
                byte[] bytes=new byte[1024*1000];
                int k=inStream.read(bytes);
                if(k>=0){
                    outStream.write(bytes,0,k);
                    outStream.flush();
                }
                else break;
            }
            inStream.close();
            outStream.close();
        } catch (IOException e){
            httpGet.abort();
            System.out.println("[ERROR] Download IOException : "+e.toString()+" [FROM] : "+path);
            //e.printStackTrace();
        }finally{
       
        }
    }

    public static void main(String argvs[]){
    	downloaderPDF("http://www.sse.com.cn/disclosure/listedinfo/announcement/c/2017-05-26/603566_20170526_1.pdf","C:\\Users\\cc\\Desktop\\result\\1.pdf");
    }
}
