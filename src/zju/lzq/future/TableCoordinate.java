package zju.lzq.future;

public class TableCoordinate {
	private int tableIndex;
	private int rowIndex;
	private int columnIndex;

	public TableCoordinate() {
		super();
	}

	public TableCoordinate(int tableIndex, int rowIndex, int columnIndex) {
		super();
		this.tableIndex = tableIndex;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

}
