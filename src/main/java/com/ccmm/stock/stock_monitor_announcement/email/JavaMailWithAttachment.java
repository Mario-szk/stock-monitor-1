package com.ccmm.stock.stock_monitor_announcement.email;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.MailSSLSocketFactory;

public class JavaMailWithAttachment {
    private MimeMessage message;
    private Session session;
    private Transport transport;

    private String mailHost = "";
    private String sender_username = "";
    private String sender_password = "";

    private Properties properties = new Properties();

    /**
     * 初始化方法
     */
    public JavaMailWithAttachment(boolean debug) {
        InputStream in = JavaMailWithAttachment.class.getResourceAsStream("/MailServer.properties");
        try {
            properties.load(in);
            this.mailHost = properties.getProperty("mail.smtp.host");
            this.sender_username = properties.getProperty("mail.sender.username");
            this.sender_password = properties.getProperty("mail.sender.password");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //Session session = Session.getInstance(props);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        session = Session.getInstance(properties);
        session.setDebug(debug);// 开启后有调试信息
        message = new MimeMessage(session);
    }

    /**
     * 发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param ccUsers
     *            抄送人地址
     * @param attachment
     *            附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, List<String> receiveUsers,List<String> ccUsers, List<File> attachments) {
        try {
            // 发件人
            InternetAddress from = new InternetAddress(sender_username);
            message.setFrom(from);

            // 收件人
            InternetAddress[] toS=new InternetAddress[receiveUsers.size()];
            for(int i=0;i<receiveUsers.size();i++){
            	String receiveUser = receiveUsers.get(i);
            	InternetAddress to = new InternetAddress(receiveUser);
            	toS[i]=to;
                
            }
            message.setRecipients(Message.RecipientType.TO, toS);
            //抄送人
            InternetAddress[] ccS=new InternetAddress[ccUsers.size()];
            for(int i=0;i<ccUsers.size();i++){
            	String receiveUser = ccUsers.get(i);
            	InternetAddress to = new InternetAddress(receiveUser);
            	ccS[i]=to;
                
            }
            message.setRecipients(Message.RecipientType.CC, ccS);
            
            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachments != null) {
            	for(File attachment:attachments){
            		 BodyPart attachmentBodyPart = new MimeBodyPart();
                     DataSource source = new FileDataSource(attachment);
                     attachmentBodyPart.setDataHandler(new DataHandler(source));
                     
                     // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                     // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                     //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                     //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                     
                     //MimeUtility.encodeWord可以避免文件名乱码
                     attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                     multipart.addBodyPart(attachmentBodyPart);
            	}
           
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            System.out.println("send success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	/***
	 * 获取文件所有内容
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static String readToString(File file, String encoding) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return "";
		}
	}
    
    public void sendEmailTest(String[] args) throws FileNotFoundException, IOException{
    	//0. 获得配置文件
    	Properties pps=new Properties();  
    	pps.load(new InputStreamReader(new FileInputStream(args[0])));
    	
    	
    	List<String> receiveUsers = new LinkedList<String>();
    	List<String> ccUsers = new LinkedList<String>();
    	List<File> affixs = new LinkedList<File>();
    	String title = "";
    	String content="";
    	
    	//1. 增加收件人
    	String allRece = pps.getProperty("receivers");
    	if(allRece==null) return ;
    	String ss[] = allRece.split(";");
    	for(String s: ss){
    		if(!s.isEmpty())
    			receiveUsers.add(s);
    	}
    	
    	//2. 增加抄送人
    	String allcc = pps.getProperty("copyto");
    	if(allcc!=null){
    		String ss1[] = allcc.split(";");
        	for(String s: ss1){
        		if(!s.isEmpty())
        			ccUsers.add(s);
        	}
    	} 
    	//3. 增加附件
    	String allaffixs = pps.getProperty("affixs");
    	if(allaffixs!=null){
    		String ss1[] = allaffixs.split(";");
        	for(String s: ss1){
        		if(!s.isEmpty()){
        			File affix = new File(s);
            		affixs.add(affix);
        		}
        	}
    	} 
    	//4. 设置标题
        title=pps.getProperty("title");
    	//5. 设置内容
        String contentPath=pps.getProperty("content");
        content=readToString(new File(contentPath),"utf-8");
        content=content.replace("\n", "<br>");
        content=content.replace("\t", "<pre>");
        //6. 发邮件
        JavaMailWithAttachment se = new JavaMailWithAttachment(true);
        se.doSendHtmlEmail(title, content,receiveUsers ,ccUsers, affixs);
    }
    public static String GetNowDate(){  
        String temp_str="";  
        Date dt = new Date();  
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        temp_str=sdf.format(dt);  
        return temp_str;  
    }  
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	List<String> receiveUsers = new LinkedList<String>();
    	List<String> ccUsers = new LinkedList<String>();
    	List<File> affixs = new LinkedList<File>();
    	String title = "";
    	String content="test";
    	String date1=GetNowDate();
    	receiveUsers.add("chongwangcc@gmail.com");
    	receiveUsers.add("770408342@qq.com");
    	title="["+date1+"]股票分紅率Top10";
    	content=args[0];
    	content=content.replace("\n", "<br>");
        content=content.replace("\t", "<pre>");
    	JavaMailWithAttachment se = new JavaMailWithAttachment(false);
        se.doSendHtmlEmail(title, content,receiveUsers ,ccUsers, affixs);
    }
}
