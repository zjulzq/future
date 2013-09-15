package zju.lzq.pay;

import java.io.FileWriter;

import org.openqa.selenium.server.SeleniumServer;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class CaptureProfit {
	private static final String HOST = "http://www.thfund.com.cn/";
	private static final String SERVER_HOST = "localhost";
	private static final String BROWSER = "googlechrome";
	private static final int PORT = 4444;
	private static final int COLUMN_NUM = 3;
	private static final String SPEED = "1";
	private static final String WAIT_LONG = "50000";
	private Selenium selenium;
	private SeleniumServer server;
	private String fileName = "payProfit.csv";

	public static void main(String[] args) {
		CaptureProfit captureProfit = new CaptureProfit();
		captureProfit.doBusiness();
	}

	public void doBusiness() {
		try {
			server = new SeleniumServer();
			server.getConfiguration().setPort(4444);
			server.start();
			selenium = new DefaultSelenium(SERVER_HOST, PORT, BROWSER, HOST);
			selenium.start();
			selenium.setSpeed(SPEED);

			selenium.open(HOST);
			selenium.waitForPageToLoad(WAIT_LONG);
			selenium.click("//a[text()='旗下产品']");
			selenium.waitForPageToLoad(WAIT_LONG);
			selenium.click("//ul[@class='left_nav']/li/a");
			selenium.waitForPageToLoad(WAIT_LONG);
			selenium.click("//li[@id='s2']/../../a[2]");
			selenium.waitForPageToLoad(WAIT_LONG);
			selenium.type("//input[@id='startdate']", "2013-6-1");
			selenium.click("//input[@id='startdate']/../../td[4]/input");
			selenium.waitForPageToLoad(WAIT_LONG);

			capture();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (selenium != null) {
				selenium.close();
			}
			if (server != null) {
				server.stop();
			}
		}
	}

	private void capture() {
		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			String[] header = new String[COLUMN_NUM];
			for (int i = 0; i < header.length; i++) {
				header[i] = getTdText(2, 2, i + 1);
			}
			csvWriter.writeNext(header);

			int rowCount = selenium.getElementIndex("//td[text()='2013-06-02']/../").intValue() + 1;
			for (int i = rowCount; i >= 3; i--) {
				String[] row = new String[COLUMN_NUM];
				for (int j = 0; j < row.length; j++) {
					row[j] = getTdText(2, i, j + 1);
				}
				csvWriter.writeNext(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	private String getTdText(int tableIndex, int rowIndex, int columnIndex) {
		String locator = getTdPath(tableIndex, rowIndex, columnIndex);
		if (selenium.isElementPresent(locator)) {
			return selenium.getText(locator);
		} else {
			return "";
		}
	}

	private String getTablePath(int tableIndex) {
		return "//h4[text()='净值走势']/../table[" + tableIndex + "]";
	}

	private String getTdPath(int tableIndex, int rowIndex, int columnIndex) {
		return getTablePath(tableIndex) + "/tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]";
	}

}
