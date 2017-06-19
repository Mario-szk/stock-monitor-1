package com.ccmm.stock.stock_monitor_announcement.mysql;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import com.ccmm.stock.stock_monitor_announcement.pdf.GetDataFromSinaUtil;
import com.ccmm.stock.stock_monitor_announcement.pdf.StockAnnounceInfo;
import com.ccmm.stock.stock_monitor_announcement.pdf.StockData;

public class AnnounceInfoMySQLTools {
	
	static CCMySQL mysql = new CCMySQL();
	
	/***
	 * 将股票分红信息保存到数据库中
	 * @param info
	 */
	public static void saveInfoToMysql(StockAnnounceInfo info){
		if(info==null) return ;
		String mysql_table="StockAnnounceInfo";
		String updataSql_1="insert  into "+mysql_table+"(";
		String updataSql_2=") value(";
		updataSql_1+="code,";
		updataSql_2+=("'"+info.getCode()+"',");
		
		updataSql_1+="name,";
		updataSql_2+=("'"+info.getName()+"',");
		
		updataSql_1+="publicDate,";
		updataSql_2+=("'"+info.getPublicDate()+"',");

		updataSql_1+="diveDate,";
		updataSql_2+=("'"+info.getDiveDate()+"',");

		updataSql_1+="registerDate,";
		updataSql_2+=("'"+info.getRegisterDate()+"',");
		
		updataSql_1+="giveMoneyDate,";
		updataSql_2+=("'"+info.getGiveMoneyDate()+"',");
		
		updataSql_1+="DiviedndsMoney,";
		updataSql_2+=("'"+info.getDividendsMoney()+"',");
		
		updataSql_1+="stockDate,";
		updataSql_2+=("'"+info.getStockDate()+"',");
		
		updataSql_1+="stockMoney,";
		updataSql_2+=("'"+info.getStockMoney()+"',");
		
		updataSql_1+="dividendRate,";
		if(info.getDividendRate()==null || info.getDividendRate().isEmpty()|| info.getStockMoney().equals("0.0")){info.setDividendRate(""+1);}
		updataSql_2+=(""+info.getDividendRate()+",");
		
		updataSql_1=updataSql_1.substring(0,updataSql_1.length()-1);
		updataSql_2=updataSql_2.substring(0,updataSql_2.length()-1);
		String updataSql=updataSql_1+updataSql_2+")";
		mysql.ExecuteUpdate("stock_data_1", updataSql);
		
	}
	
	/***
	 * 将股票分红信息保存到数据库中
	 * @param info
	 */
	public static void saveInfoToMysql(StockAnnounceInfo info,int id){
		if(info==null) return ;
		String mysql_table="StockAnnounceInfo";
		String updataSql_1="replace into "+mysql_table+"(";
		String updataSql_2=") value(";
		
		updataSql_1+="id,";
		updataSql_2+=(""+info.getId()+",");
		
		updataSql_1+="code,";
		updataSql_2+=("'"+info.getCode()+"',");
		
		updataSql_1+="name,";
		updataSql_2+=("'"+info.getName()+"',");
		
		updataSql_1+="publicDate,";
		updataSql_2+=("'"+info.getPublicDate()+"',");

		updataSql_1+="diveDate,";
		updataSql_2+=("'"+info.getDiveDate()+"',");

		updataSql_1+="registerDate,";
		updataSql_2+=("'"+info.getRegisterDate()+"',");
		
		updataSql_1+="giveMoneyDate,";
		updataSql_2+=("'"+info.getGiveMoneyDate()+"',");
		
		updataSql_1+="DiviedndsMoney,";
		updataSql_2+=("'"+info.getDividendsMoney()+"',");
		
		updataSql_1+="stockDate,";
		updataSql_2+=("'"+info.getStockDate()+"',");
		
		updataSql_1+="stockMoney,";
		updataSql_2+=("'"+info.getStockMoney()+"',");
		
		updataSql_1+="dividendRate,";
		if(info.getDividendRate()==null || info.getDividendRate().isEmpty()|| info.getDividendRate().equals("0")){info.setDividendRate(""+0);}
		updataSql_2+=(""+info.getDividendRate()+",");
		
		updataSql_1=updataSql_1.substring(0,updataSql_1.length()-1);
		updataSql_2=updataSql_2.substring(0,updataSql_2.length()-1);
		String updataSql=updataSql_1+updataSql_2+")";
		mysql.ExecuteUpdate("stock_data_1", updataSql);
		
	}
	
	/***
	 * 获得数据库里，分红率最高的股票
	 * @param date
	 * @return
	 */
	public static List<StockAnnounceInfo> selectTop10InfoFromMysql(String date){
		updateMysql(date);
		//SELECT *,count(DISTINCT code) FROM StockAnnounceInfo where registerDate>'2017-06-12' group by code ORDER BY dividendRate DESC LIMIT 10
		List<StockAnnounceInfo> list = new LinkedList<StockAnnounceInfo>();
		String sql ="SELECT * ,count(DISTINCT code) FROM StockAnnounceInfo where registerDate>'"+date+"' group by code ORDER BY dividendRate DESC LIMIT 10";
		mysql.ExecuteQuery("stock_data_1", sql, new CCMySQL.QueryHandler() {
			
			@Override
			public void handler(ResultSet resultSet) {
				// TODO Auto-generated method stub
				StockAnnounceInfo info = new StockAnnounceInfo();
				try{
					info.setCode(resultSet.getString("code"));
				}catch(Exception e){}
				try{
					info.setName(resultSet.getString("name"));
				}catch(Exception e){}
				try{
					info.setPublicDate(resultSet.getString("publicDate"));
				}catch(Exception e){}
				try{
					info.setDiveDate(resultSet.getString("diveDate"));
				}catch(Exception e){}
				try{
					info.setRegisterDate(resultSet.getString("registerDate"));
				}catch(Exception e){}
				try{
					info.setGiveMoneyDate(resultSet.getString("giveMoneyDate"));
				}catch(Exception e){}
				try{
					info.setDividendsMoney(resultSet.getString("DiviedndsMoney"));
				}catch(Exception e){}
				try{
					info.setStockDate(resultSet.getString("stockDate"));
				}catch(Exception e){}
				try{
					info.setStockMoney(resultSet.getString("stockMoney"));
				}catch(Exception e){}
				try{
					info.setDividendRate(resultSet.getString("dividendRate"));
				}catch(Exception e){}
				list.add(info);
			}
		});
		return list;
	}
	
	
	/***
	 * 更新数据库
	 * @param date
	 */
	public static void updateMysql(String date){
		//1.获得所有在有效期内的股票
		List<StockAnnounceInfo> list = new LinkedList<StockAnnounceInfo>();
		String sql ="SELECT * FROM StockAnnounceInfo where registerDate>'"+date+"'";
		mysql.ExecuteQuery("stock_data_1", sql, new CCMySQL.QueryHandler() {
			
			@Override
			public void handler(ResultSet resultSet) {
				// TODO Auto-generated method stub
				StockAnnounceInfo info = new StockAnnounceInfo();
				try{
					info.setId(resultSet.getInt("id"));
				}catch(Exception e){}
				try{
					info.setCode(resultSet.getString("code"));
				}catch(Exception e){}
				try{
					info.setName(resultSet.getString("name"));
				}catch(Exception e){}
				try{
					info.setPublicDate(resultSet.getString("publicDate"));
				}catch(Exception e){}
				try{
					info.setDiveDate(resultSet.getString("diveDate"));
				}catch(Exception e){}
				try{
					info.setRegisterDate(resultSet.getString("registerDate"));
				}catch(Exception e){}
				try{
					info.setGiveMoneyDate(resultSet.getString("giveMoneyDate"));
				}catch(Exception e){}
				try{
					info.setDividendsMoney(resultSet.getString("DiviedndsMoney"));
				}catch(Exception e){}
				try{
					info.setStockDate(resultSet.getString("stockDate"));
				}catch(Exception e){}
				try{
					info.setStockMoney(resultSet.getString("stockMoney"));
				}catch(Exception e){}
				try{
					info.setDividendRate(resultSet.getString("dividendRate"));
				}catch(Exception e){}
				list.add(info);
			}
		});
		
		//2.更新分红率
		for(StockAnnounceInfo info:list){
			try{
				StockData data = GetDataFromSinaUtil.getStockCsvData(info.getCode());
				double closePrice=data.getClose();
				info.setStockMoney(closePrice+"");
				info.setStockDate(data.getDate());
				
				
				double mon = Double.parseDouble(info.getStockMoney());
				if (Double.compare(mon, new Double(0.0)) == 0) {
					info.setDividendRate("0");
				}else{
					double Rate=(Double.parseDouble(info.getDividendsMoney()))/(Double.parseDouble(info.getStockMoney()));
					info.setDividendRate((int)(Rate*100*100)+"");
				}
			}catch(Exception e){}	
		}
		//3.保存到mysql中
		for(StockAnnounceInfo info:list){
			saveInfoToMysql(info,info.getId());
		}
	}
	
	public static void main(String[] argvs){
		List<StockAnnounceInfo> list = selectTop10InfoFromMysql("2017-05-31");
		for(StockAnnounceInfo info :list){
			System.out.println(info);
		}
		
	}

}
