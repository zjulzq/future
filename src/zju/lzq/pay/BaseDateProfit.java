package zju.lzq.pay;

import java.util.Date;

public class BaseDateProfit {
	public static final double RATE_BASE = 0.01;
	private Date date;
	private double profit;
	private double rate;

	public BaseDateProfit(Date date, double profit, double rate) {
		super();
		this.date = date;
		this.profit = profit;
		this.rate = rate;
	}

	public BaseDateProfit() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
