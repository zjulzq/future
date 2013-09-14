package zju.lzq.task;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.server.SeleniumServer;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class CaptureTotal {
	private static final String HOST = "http://www.cffex.com.cn";
	private static Selenium selenium;
	private static SeleniumServer server;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final Date today = roundDate(new Date());
	private static boolean headerAdded = false;
	private static int[] columnIndexes = new int[] { 3, 4, 7, 8, 11, 12 };

	/**
	 * args[0]=类名, args[1]=日期, args[2]=速度
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			server = new SeleniumServer();
			server.getConfiguration().setPort(4444);
			server.start();
			selenium = new DefaultSelenium("localhost", 4444, "*googlechrome", HOST);
			selenium.start();
			selenium.setSpeed("1");
			if (args.length > 2) {
				selenium.setSpeed(args[2]);
			}

			selenium.open("/fzjy/ccpm/");
			selenium.waitForPageToLoad("1000");

			String fileName = null;
			CSVWriter csvWriter = null;
			try {
				if (args.length <= 1) {
					fileName = sdf.format(today) + ".csv";
					csvWriter = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
					capture(csvWriter, today);
				} else {
					fileName = args[1] + ".csv";
					csvWriter = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
					Date beginDate = sdf.parse(args[1]);
					for (Date tmp = beginDate; tmp.compareTo(today) <= 0;) {
						capture(csvWriter, tmp);
						tmp = roundDate(new Date(tmp.getTime() + 86400 * 1000));
					}
				}
			} finally {
				if (csvWriter != null) {
					csvWriter.close();
				}
			}

			selenium.stop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void capture(CSVWriter csvWriter, Date date) throws IOException, InterruptedException {
		if (date.getDay() == 0 || date.getDay() == 6 || date.compareTo(today) > 0) {
			return;
		}

		selenium.select("//select[@id='product']", "label=regexp:^IF.*");
		selenium.type("//input[@id='actualDate']", sdf.format(date));
		selenium.click("//input[@id='actualDate']/../../span[3]/img");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0", "50000");
		if (selenium.getText("//div[@id='textArea']").contains("当前交易日没有数据")) {
			return;
		}

		long start = System.currentTimeMillis();
		while (!selenium.isElementPresent("//div[@id='textArea']/table[1]/tbody/tr[1]/td[1]")) {
			if (System.currentTimeMillis() - start > 100) {
				return;
			}
		}

		if (!headerAdded) {
			String[] header = new String[7];
			header[0] = "日期";
			for (int i = 1; i < header.length; i++) {
				header[i] = getText(2, 2, columnIndexes[i - 1]);
			}
			csvWriter.writeNext(header);
			headerAdded = true;
		}

		int tableCount = selenium.getXpathCount("//div[@id='textArea']/table").intValue();
		if (tableCount > 1) {
			for (int i = 0; i < 3; i++) {
				tableCount = selenium.getXpathCount("//div[@id='textArea']/table").intValue();
			}
		}
		List<String[]> list = new ArrayList<String[]>();
		for (int index = 2; index <= tableCount; index = index + 2) {
			list.add(captureTable(csvWriter, index));
		}
		if (list.size() > 0) {
			String[] row = new String[list.get(0).length];

			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					for (int j = 0; j < list.get(i).length; j++) {
						row[j] = list.get(i)[j];
					}
				} else {
					for (int j = 1; j < list.get(i).length; j++) {
						int total = Integer.parseInt(row[j]) + Integer.parseInt(list.get(i)[j]);
						row[j] = "" + total;
					}
				}
			}
			csvWriter.writeNext(row);
		}

	}

	private static String[] captureTable(CSVWriter csvWriter, int index) throws IOException {
		String[] row = new String[7];
		String dateStr = "";
		if (index == 2) {
			while (!selenium.isElementPresent("//div[@id='textArea']/table[" + 1 + "]/tbody/tr[1]/td[2]")) {
			}
			dateStr = selenium.getText("//div[@id='textArea']/table[" + 1 + "]/tbody/tr[1]/td[2]");
			dateStr = dateStr.substring(dateStr.indexOf(':') + 1);
		}

		int rowCount = selenium.getXpathCount("//div[@id='textArea']/table[" + index + "]/tbody/tr").intValue();
		if (rowCount == 0) {
			for (int i = 0; i < 3; i++) {
				rowCount = selenium.getXpathCount("//div[@id='textArea']/table[" + index + "]/tbody/tr").intValue();
			}
		}
		if (rowCount == 0) {
			return row;
		}

		row[0] = dateStr;
		for (int i = 1; i < row.length; i++) {
			row[i] = getText(index, rowCount, columnIndexes[i - 1]);
		}

		return row;
	}

	public static Date roundDate(Date date) {
		Date result = null;
		try {
			result = sdf.parse(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String getText(int tableIndex, int rowIndex, int columnIndex) {
		String locator = "//div[@id='textArea']/table[" + tableIndex + "]/tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]";
		if (selenium.isElementPresent(locator)) {
			return selenium.getText(locator);
		} else {
			return "";
		}

	}
}
