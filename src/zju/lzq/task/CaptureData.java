package zju.lzq.task;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.server.SeleniumServer;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class CaptureData {
	private static final String HOST = "http://www.cffex.com.cn";
	private static Selenium selenium;
	private static SeleniumServer server;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final Date today = roundDate(new Date());

	public static void main(String[] args) {
		try {
			server = new SeleniumServer();
			server.getConfiguration().setPort(4444);
			server.start();
			selenium = new DefaultSelenium("localhost", 4444, "*googlechrome", HOST);
			selenium.start();
			selenium.setSpeed("50");

			selenium.open("/fzjy/ccpm/");
			selenium.waitForPageToLoad("1000");

			if (args.length <= 1) {
				capture(today);
			} else {
				Date beginDate = sdf.parse(args[1]);
				for (Date tmp = beginDate; tmp.compareTo(today) <= 0;) {
					capture(tmp);
					tmp = roundDate(new Date(tmp.getTime() + 86400 * 1000));
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
	private static void capture(Date date) throws IOException {
		if (date.getDay() == 0 || date.getDay() == 6 || date.compareTo(today) > 0) {
			return;
		}

		selenium.type("//input[@id='actualDate']", sdf.format(date));
		selenium.click("//input[@id='actualDate']/../../span[3]/img");
		if (selenium.getText("//div[@id='textArea']").contains("当前交易日没有数据")) {
			return;
		}

		long start = System.currentTimeMillis();
		while (!selenium.isElementPresent("//div[@id='textArea']/table[1]/tbody/tr[1]/td[1]")) {
			if (System.currentTimeMillis() - start > 2000) {
				return;
			}
		}

		int tableCount = selenium.getXpathCount("//div[@id='textArea']/table").intValue();
		for (int index = 1; index <= tableCount; index++) {
			captureTable(index);
		}

	}

	private static void captureTable(int index) throws IOException {
		String nameStr = selenium.getText("//div[@id='textArea']/table[" + index + "]/tbody/tr[1]/td[1]");
		nameStr = nameStr.substring(nameStr.indexOf(':') + 1);
		String dateStr = selenium.getText("//div[@id='textArea']/table[" + index + "]/tbody/tr[1]/td[2]");
		dateStr = dateStr.substring(dateStr.indexOf(':') + 1);
		String fileName = nameStr + "-" + dateStr + ".csv";

		CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);

		int rowCount = selenium.getXpathCount("//div[@id='textArea']/table[" + index + "]/tbody/tr").intValue();
		for (int i = 3; i <= rowCount; i++) {
			int columnCount = selenium.getXpathCount("//div[@id='textArea']/table[" + index + "]/tbody/tr[" + i + "]/td").intValue();
			String[] row = new String[columnCount];
			for (int j = 1; j <= columnCount; j++) {
				row[j - 1] = selenium.getText("//div[@id='textArea']/table[" + index + "]/tbody/tr[" + i + "]/td[" + j + "]");
			}
			csvWriter.writeNext(row);
		}
		csvWriter.close();
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
}
