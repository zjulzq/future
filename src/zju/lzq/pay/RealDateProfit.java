package zju.lzq.pay;

import java.util.Date;

public class RealDateProfit {
	private Date date;
	private double capital;
	private double profit;
	private double total;

	public RealDateProfit() {
		super();
	}

	public RealDateProfit(Date date, double capital, double profit, double total) {
		super();
		this.date = date;
		this.capital = capital;
		this.profit = profit;
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}
