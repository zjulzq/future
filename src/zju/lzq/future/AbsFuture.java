package zju.lzq.future;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.server.SeleniumServer;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVWriter;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public abstract class AbsFuture {
	private static final String HOST = "http://www.cffex.com.cn";
	private static final String SERVER_HOST = "localhost";
	private static final String BROWSER = "googlechrome";
	private static final String SPEED = "1";
	protected static final String TABLE_BASE_PATH = "//div[@id='textArea']";
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected Date today = roundDate(new Date());
	protected Selenium selenium;
	private SeleniumServer server;
	private boolean headerAdded = false;

	public void execute(String[] args) {
		try {
			server = new SeleniumServer();
			server.getConfiguration().setPort(getPort());
			server.start();
			selenium = new DefaultSelenium(SERVER_HOST, getPort(), BROWSER, HOST);
			selenium.start();
			selenium.setSpeed(SPEED);

			doBusiness(args);

			selenium.stop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (selenium != null) {
				selenium.stop();
			}
			if (server != null) {
				server.stop();
			}
		}
	}

	protected String[] captureTableTotal(CSVWriter csvWriter, int tableIndex) {
		String[] row = new String[getColumnNum()];
		String dateStr = "";
		if (isFirstTable(tableIndex)) {
			TableCoordinate coordinate = getDateCoordinate();
			while (!isTdPresent(coordinate.getTableIndex(), coordinate.getRowIndex(), coordinate.getColumnIndex())) {
			}

			dateStr = getTdText(coordinate.getTableIndex(), coordinate.getRowIndex(), coordinate.getColumnIndex());
			dateStr = dateStr.substring(dateStr.indexOf(':') + 1);
		}

		int rowCount = getTrCount(tableIndex);

		row[0] = dateStr;
		int[] columnIndexes = getColumnIndexes();
		for (int i = 1; i < row.length; i++) {
			row[i] = getTdText(tableIndex, rowCount, columnIndexes[i - 1]);
		}

		return row;
	}

	protected void captureTotal(CSVWriter csvWriter) {
		int tableCount = getTableCount();

		List<String[]> list = new ArrayList<String[]>();
		int[] tableIndexes = getTableIndexes();
		for (int i = 0; i < tableCount / 2 && tableIndexes[i] <= tableCount; i++) {
			list.add(captureTableTotal(csvWriter, tableIndexes[i]));
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

	protected CSVWriter getCsvWriter(String[] args) {
		String fileName = null;
		CSVWriter csvWriter = null;

		if (args.length <= 1) {
			fileName = sdf.format(today) + ".csv";
		} else {
			fileName = args[1] + ".csv";
		}
		try {
			csvWriter = new CSVWriter(new FileWriter(fileName), CSVParser.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return csvWriter;
	}

	protected void addHeader(CSVWriter csvWriter) {
		if (!headerAdded) {
			String[] header = new String[getColumnNum()];
			header[0] = "日期";
			int[] columnIndexes = getColumnIndexes();
			for (int i = 1; i < header.length; i++) {
				TableCoordinate coordinate = getHeaderCoordinate(columnIndexes[i - 1]);
				header[i] = getTdText(coordinate.getTableIndex(), coordinate.getRowIndex(), coordinate.getColumnIndex());
			}
			csvWriter.writeNext(header);
			headerAdded = true;
		}
	}

	protected abstract int getPort();

	protected abstract void doBusiness(String[] args);

	protected abstract int getColumnNum();

	protected abstract TableCoordinate getDateCoordinate();

	protected abstract int[] getColumnIndexes();

	protected abstract int[] getTableIndexes();

	protected abstract boolean isFirstTable(int tableIndex);

	protected abstract TableCoordinate getHeaderCoordinate(int columnIndex);

	protected abstract void capture(CSVWriter csvWriter, Date date);

	protected void capture(String[] args) {
		CSVWriter csvWriter = getCsvWriter(args);
		try {
			if (args.length <= 1) {
				capture(csvWriter, today);
			} else {
				Date beginDate = sdf.parse(args[1]);
				for (Date tmp = beginDate; tmp.compareTo(today) <= 0;) {
					capture(csvWriter, tmp);
					tmp = nextDate(tmp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	protected boolean isTdPresent(int tableIndex, int rowIndex, int columnIndex) {
		String locator = getTdPath(tableIndex, rowIndex, columnIndex);
		return selenium.isElementPresent(locator);
	}

	protected String getTdText(int tableIndex, int rowIndex, int columnIndex) {
		String locator = getTdPath(tableIndex, rowIndex, columnIndex);
		if (selenium.isElementPresent(locator)) {
			return selenium.getText(locator);
		} else {
			return "";
		}
	}

	protected int getTrCount(int tableIndex) {
		String locator = getTablePath(tableIndex) + "/tbody/tr";
		return selenium.getXpathCount(locator).intValue();
	}

	protected int getTableCount() {
		String locator = TABLE_BASE_PATH + "/table";
		return selenium.getXpathCount(locator).intValue();
	}

	protected String getTablePath(int tableIndex) {
		return TABLE_BASE_PATH + "/table[" + tableIndex + "]";
	}

	protected String getTdPath(int tableIndex, int rowIndex, int columnIndex) {
		return getTablePath(tableIndex) + "/tbody/tr[" + rowIndex + "]/td[" + columnIndex + "]";
	}

	protected Date roundDate(Date date) {
		Date result = null;
		try {
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			}
			result = sdf.parse(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	protected Date nextDate(Date date) {
		return roundDate(new Date(date.getTime() + 86400 * 1000));
	}
}
