package zju.lzq.future;

import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;

public class TreasuryFuture extends AbsFuture {
	private static final String PAGE = "/fzjy/ccpm/";
	private static boolean headerAdded = false;
	private static final int COLUMN_NUM = 7;
	private static int[] columnIndexes = new int[] { 3, 4, 7, 8, 11, 12 };

	@Override
	protected int getPort() {
		return 4445;
	}

	@Override
	public void doBusiness(String[] args) {

	}

	@Override
	protected int getColumnNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected TableCoordinate getDateCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int[] getColumnIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isFirstTable(int tableIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int[] getTableIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TableCoordinate getHeaderCoordinate(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void capture(CSVWriter csvWriter, Date date) {
		// TODO Auto-generated method stub
		
	}

}
