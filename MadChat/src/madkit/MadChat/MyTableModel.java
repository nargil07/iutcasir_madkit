/*
 * Created on Mar 29, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import javax.swing.table.AbstractTableModel;


public class MyTableModel extends AbstractTableModel{

	String[] names;
	Object[][] data;
	
	public MyTableModel(String[] name, Object[][] datas){
		super();
		this.names=name;
		this.data=datas;
	}
	
	public int getColumnCount() {
		return names.length;
	}
	public int getRowCount() {
		return data.length;
	}
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	public void setData(Object[][] o){
		data=o;
		fireTableDataChanged();
	}
	// The default implementations of these methods in
	// AbstractTableModel would work, but we can refine them.
	public String getColumnName(int column) {
		return names[column];
	}
	
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
	
	public boolean isCellEditable(int row, int col) {
		//return (col==0 && (getValueAt(row,col) instanceof Boolean));
		return (col==0);
	}
	
	public void setValueAt(Object aValue, int row, int column) {
		data[row][column] = aValue;
		fireTableCellUpdated(row, column);
   	}

	
	
	
}
