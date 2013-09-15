package zju.lzq.future;

import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;

public class TreasuryFuture extends AbsFuture {
	private static final String PAGE = "/fzjy/ccpm/";
	private int[] columnIndexes = new int[] { 3, 4, 7, 8, 11, 12 };
	private int[] tableIndexes = new int[] { 3 };

	@Override
	protected int getPort() {
		return 4444;
	}

	@Override
	public void doBusiness(String[] args) {
		if (args.length >= 4) {
			selenium.setSpeed(args[3]);
		}

		selenium.open(PAGE);
		selenium.waitForPageToLoad("1000");

		capture(args);
	}

	@Override
	protected int getColumnNum() {
		return columnIndexes.length + 1;
	}

	@Override
	protected TableCoordinate getDateCoordinate() {
		return new TableCoordinate(1, 1, 2);
	}

	@Override
	protected int[] getColumnIndexes() {
		return columnIndexes;
	}

	@Override
	protected boolean isFirstTable(int tableIndex) {
		return tableIndexes[0] == tableIndex;
	}

	@Override
	protected int[] getTableIndexes() {
		return tableIndexes;
	}

	@Override
	protected TableCoordinate getHeaderCoordinate(int columnIndex) {
		return new TableCoordinate(3, 2, columnIndex);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void capture(CSVWriter csvWriter, Date date) {
		if (date.getDay() == 0 || date.getDay() == 6 || date.compareTo(today) > 0) {
			return;
		}

		selenium.select("//select[@id='product']", "label=regexp:^TF.*");
		selenium.type("//input[@id='actualDate']", sdf.format(date));
		selenium.click("//input[@id='actualDate']/../../span[3]/img");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0", "50000");
		if (selenium.getText(TABLE_BASE_PATH).contains("当前交易日没有数据")) {
			return;
		}

		if (getTableCount() > 2) {
			addHeader(csvWriter);
			captureTotal(csvWriter);
		}
	}

	@Override
	protected String getPrefix() {
		return "TF";
	}

	@Override
	protected int getDivisor() {
		return 3;
	}

}
