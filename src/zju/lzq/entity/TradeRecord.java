package zju.lzq.entity;

import java.util.Date;

public class TradeRecord implements Comparable<TradeRecord> {
	public static final String DUO = "多头";
	public static final String KOND = "空头";
	private int times;
	private String type;
	private String commodity;
	private Date startTime;
	private Date endTime;
	private double startPrice;
	private double endPrice;
	private double netProfit;
	private double totalNetProfit;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public double getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(double endPrice) {
		this.endPrice = endPrice;
	}

	public double getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(double netProfit) {
		this.netProfit = netProfit;
	}

	public double getTotalNetProfit() {
		return totalNetProfit;
	}

	public void setTotalNetProfit(double totalNetProfit) {
		this.totalNetProfit = totalNetProfit;
	}

	@Override
	public int compareTo(TradeRecord o) {
		return this.getStartTime().compareTo(o.getStartTime());
	}

}
