package zju.lzq.entity;

import java.util.Date;

public class DailyProfit implements Comparable<DailyProfit> {
	private Date date;
	private double profit;

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

	@Override
	public int compareTo(DailyProfit o) {
		return getDate().compareTo(o.getDate());
	}

}
