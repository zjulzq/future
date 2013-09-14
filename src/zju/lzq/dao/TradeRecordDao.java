package zju.lzq.dao;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zju.lzq.entity.TradeRecord;
import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

public class TradeRecordDao {
	private String fileName;

	public TradeRecordDao(String fileName) {
		this.fileName = fileName;
	}

	public List<TradeRecord> readAll() {
		List<TradeRecord> list = new ArrayList<TradeRecord>();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(fileName), CSVParser.DEFAULT_SEPARATOR);
			reader.readNext();
			String[] record = null;
			while ((record = reader.readNext()) != null && record.length > 0) {
				TradeRecord tradeRecord = new TradeRecord();
				tradeRecord.setTimes(Integer.valueOf(record[0]).intValue());
				tradeRecord.setType(record[1]);
				tradeRecord.setCommodity(record[2]);
				tradeRecord.setStartTime(new Date(record[3]));
				tradeRecord.setStartPrice(Double.valueOf(record[4]).doubleValue());
				tradeRecord.setEndTime(new Date(record[5]));
				tradeRecord.setEndPrice(Double.valueOf(record[6]).doubleValue());
				tradeRecord.setNetProfit(Double.valueOf(record[7]).doubleValue());
				tradeRecord.setTotalNetProfit(Double.valueOf(record[8]).doubleValue());
				list.add(tradeRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
