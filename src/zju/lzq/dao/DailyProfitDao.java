package zju.lzq.dao;

import java.io.FileWriter;
import java.io.IOException;

import zju.lzq.entity.DailyProfit;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

public class DailyProfitDao {
	private CSVWriter writer;
	public static final String DATE = "日期";
	public static final String PROFIT = "利润";

	public DailyProfitDao(String fileName) {
		try {
			writer = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(new String[] { DATE, PROFIT });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(DailyProfit dailyProfit) {
		writer.writeNext(new String[] { dailyProfit.getDate().toLocaleString(), "" + dailyProfit.getProfit() });
	}

	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
