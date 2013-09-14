package zju.lzq.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zju.lzq.dao.DailyProfitDao;
import zju.lzq.dao.TradeRecordDao;
import zju.lzq.entity.DailyProfit;
import zju.lzq.entity.TradeRecord;

public class DailyProfitMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sourceFileName = "数据分析.csv";
		String targetFileName = "每日收益.csv";
		TradeRecordDao tradeRecordDao = new TradeRecordDao(sourceFileName);
		DailyProfitDao dailyProfitDao = new DailyProfitDao(targetFileName);
		List<TradeRecord> tradeRecords = tradeRecordDao.readAll();
		Collections.sort(tradeRecords);

		Map<String, DailyProfit> map = new HashMap<String, DailyProfit>();
		for (TradeRecord tradeRecord : tradeRecords) {
			String date = String.format("%tD", tradeRecord.getStartTime());
			DailyProfit dailyProfit = map.get(date);
			if (dailyProfit == null) {
				dailyProfit = new DailyProfit();
				dailyProfit.setDate(new Date(date));
				map.put(date, dailyProfit);
			}
			dailyProfit.setProfit(dailyProfit.getProfit() + tradeRecord.getNetProfit());
		}

		List<DailyProfit> list = new ArrayList<DailyProfit>(map.values());
		Collections.sort(list);

		for (DailyProfit dailyProfit : list) {
			dailyProfitDao.save(dailyProfit);
		}
		dailyProfitDao.close();
	}

}
