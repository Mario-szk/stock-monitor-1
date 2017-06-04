package com.ccmm.stock.stock_monitor_announcement.pdf;

public class StockAnnounceInfo {
	
	private int id;
	private String code; //股票代码
	private String name;//股票简称
	private String publicDate; //股票发布日期
	private String diveDate; //除权除息日
	private String registerDate; //股权登记日
	private String giveMoneyDate; //现金红利发放日

	private String DividendsMoney; //现金分红的钱数
	private String stockDate; //查询股价的日期
	private String stockMoney; //stockDate那天股票价格
	private String dividendRate; //分红率
	
	public String toString(){
		String str="";
		str+="[";
		str+="code:"+code+",";
		str+="name:"+name+",";
		str+="publicDate:"+publicDate+",";
		str+="diveDate:"+diveDate+",";
		str+="registerDate:"+registerDate+",";
		str+="giveMoneyDate:"+giveMoneyDate+",";
		str+="DividendsMoney:"+DividendsMoney+",";
		str+="stockDate:"+stockDate+",";
		str+="stockMoney:"+stockMoney+",";
		str+="dividendRate:"+dividendRate;
		str+="]";
		return str;
	}
	
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getGiveMoneyDate() {
		return giveMoneyDate;
	}
	public void setGiveMoneyDate(String giveMoneyDate) {
		this.giveMoneyDate = giveMoneyDate;
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
	public String getPublicDate() {
		return publicDate;
	}
	public void setPublicDate(String publicDate) {
		this.publicDate = publicDate;
	}
	public String getDiveDate() {
		return diveDate;
	}
	public void setDiveDate(String diveDate) {
		this.diveDate = diveDate;
	}
	public String getDividendsMoney() {
		return DividendsMoney;
	}
	public void setDividendsMoney(String dividendsMoney) {
		DividendsMoney = dividendsMoney;
	}
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	public String getStockMoney() {
		return stockMoney;
	}
	public void setStockMoney(String stockMoney) {
		this.stockMoney = stockMoney;
	}
	public String getDividendRate() {
		return dividendRate;
	}
	public void setDividendRate(String dividendRate) {
		this.dividendRate = dividendRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
