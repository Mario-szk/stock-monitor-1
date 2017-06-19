package com.ccmm.stock.stock_monitor_announcement.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CCMySQL {

	//public static String url="jdbc:mysql://115.28.186.15:3306/dmcc_data?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";    //JDBC的URL    
	//public static String user="root";
	//public static String password="dmccCompanyB$id3823~02";
	/** 线程内共享Connection，ThreadLocal通常是全局的，支持泛型 */  
    private  ThreadLocal<Connection> thread_local_Connection = new ThreadLocal<Connection>();  
    private  String thread_local_ip =null; 
    private  String thread_local_port = null; 
    private  String thread_local_user = null; 
    private  String thread_local_password = null; 


    public CCMySQL(){
    	//set("10.28.86.78","3306","root","root");
    	set("139.129.230.162","3306","root","root");
    }
	public  interface QueryHandler{
		void handler(ResultSet resultSet);
	}
	
	/***
	 * 设置mysql的ip,port,user,password
	 * @return
	 */
	public  void set(String ip,String port,String user,String password){
		if(ip!=null){
			thread_local_ip=ip;
		}
		if(port!=null){
			thread_local_port=port;
		}
		if(user!=null){
			thread_local_user=user;
		}
		if(password!=null){
			thread_local_password=password;
		}
	}

	/***
	 * 执行查询语句，处理每条查询结果
	 * @param database
	 * @param sql
	 * @param handler
	 */
	public  void ExecuteQuery(String database,String sql,QueryHandler handler){
		
		 ResultSet rs =null;
		 Statement stmt = null;
		 Connection conn =null;
		
		try{
			conn = thread_local_Connection.get();  
            // 1. 创建连接
            if(conn == null || conn.isClosed()) {  
                // 创建新的Connection赋值给conn 
                Class.forName("com.mysql.jdbc.Driver");
                String ip=thread_local_ip;
                String port=thread_local_port;
                String user=thread_local_user;
                String password=thread_local_password;
                String url="jdbc:mysql://"+ip+":"+port+"/"+database+"?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true";
                conn = DriverManager.getConnection(url,user,password);
                // 保存Connection  
            	thread_local_Connection.set(conn);  
            }
	        //2. 执行mysql
            stmt = conn.createStatement(); //创建Statement对象
            rs = stmt.executeQuery(sql);//创建数据对象
            while (rs!=null&&rs.next()){
            	handler.handler(rs);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
		
		try{
            //3. 关闭连接
           if(rs!=null){
        	   rs.close();
        	   rs=null;
           } 
		}catch(Exception e){}
		try{
            //3. 关闭连接
           if(stmt!=null){
        	   stmt.close();
        	   stmt=null;
           } 
		}catch(Exception e){}
		try{
            //3. 关闭连接
           if(conn!=null){
        	   conn.close();
        	   conn=null;
           } 
		}catch(Exception e){}
       
	}

	/***
	 * 向数据库中插入或者更新一条结果
	 * @param database
	 * @param sql
	 */
	public boolean ExecuteUpdate(String database,String sql){
		
		 Statement stmt = null;
		 Connection conn =null;
		 int rs =0;
		
		try{
			conn = thread_local_Connection.get();  
            // 1. 创建连接
            if(conn == null || conn.isClosed()) {  
                // 创建新的Connection赋值给conn 
                Class.forName("com.mysql.jdbc.Driver");
                String ip=thread_local_ip;
                String port=thread_local_port;
                String user=thread_local_user;
                String password=thread_local_password;
                String url="jdbc:mysql://"+ip+":"+port+"/"+database+"?autoReconnect=true";
                conn = DriverManager.getConnection(url,user,password);
                // 保存Connection  
            	thread_local_Connection.set(conn);  
            }
	        //2. 执行mysql
            stmt = conn.createStatement(); //创建Statement对象
            //System.out.println(sql);
            rs = stmt.executeUpdate(sql);//创建数据对象
           
        }catch(Exception e)
        {
            e.printStackTrace();
            
        }
		try{
            //3. 关闭连接
           if(stmt!=null){
        	   stmt.close();
        	   stmt=null;
           } 
		}catch(Exception e){}
		try{
            //3. 关闭连接
           if(conn!=null){
        	   conn.close();
        	   conn=null;
           } 
		}catch(Exception e){}
		 if(rs>0){
         	return true;
         }
         return false;
	}
	
	
	public static void main(String[] args){
		CCMySQL CCmysql = new CCMySQL();
		String database="dmcc_data";
		String sql="select * from bid_keyword";
		
		CCmysql.ExecuteQuery(database, sql, new QueryHandler(){

			@Override
			public void handler(ResultSet resultSet) {
				// TODO Auto-generated method stub
				try {
					System.out.print(resultSet.getString(1));
					System.out.print(resultSet.getString(2));
					System.out.print(resultSet.getString(3));
					System.out.println();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
    
		String updataSql = "insert into bid_keyword(keyword) value('[\"test\"]')";
		CCmysql.ExecuteUpdate(database, updataSql);
		
	}
}
