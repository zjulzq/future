package zju.lzq.future;

import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;

public class IndexFuture extends AbsFuture {
	private static final String PAGE = "/fzjy/ccpm/";
	private static int[] columnIndexes = new int[] { 3, 4, 7, 8, 11, 12 };
	private static int[] tableIndexes = new int[] { 2, 4, 6, 8 };

	public static void main(String[] args) {
		AbsFuture future = new IndexFuture();
		future.execute(args);
	}

	@Override
	protected int getPort() {
		return 4444;
	}

	/**
	 * args[0]=类名, args[1]=日期, args[2]=速度
	 * 
	 * @param args
	 */
	@Override
	public void doBusiness(String[] args) {
		if (args.length > 2) {
			selenium.setSpeed(args[2]);
		}

		selenium.open(PAGE);
		selenium.waitForPageToLoad("1000");

		capture(args);
	}

	@SuppressWarnings("deprecation")
	protected void capture(CSVWriter csvWriter, Date date) {
		if (date.getDay() == 0 || date.getDay() == 6 || date.compareTo(today) > 0) {
			return;
		}

		selenium.select("//select[@id='product']", "label=regexp:^IF.*");
		selenium.type("//input[@id='actualDate']", sdf.format(date));
		selenium.click("//input[@id='actualDate']/../../span[3]/img");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().jQuery.active == 0", "50000");
		if (selenium.getText(TABLE_BASE_PATH).contains("当前交易日没有数据")) {
			return;
		}

		addHeader(csvWriter);
		captureTotal(csvWriter);
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
	protected boolean isFirstTable(int tableIndex) {
		return tableIndexes[0] == tableIndex;
	}

	@Override
	protected int[] getColumnIndexes() {
		return columnIndexes;
	}

	@Override
	protected int[] getTableIndexes() {
		return tableIndexes;
	}

	@Override
	protected TableCoordinate getHeaderCoordinate(int columnIndex) {
		return new TableCoordinate(2, 2, columnIndex);
	}

}
