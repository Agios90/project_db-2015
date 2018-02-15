import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.sql.*;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class result_table extends JPanel {

	JTable table;
        public result_table(ResultSet set) {            
            setLayout(new BorderLayout());
            QueryTableModel qtm = new QueryTableModel();
            table = new JTable(qtm);
            JScrollPane scrollpane = new JScrollPane(table);
            scrollpane.setPreferredSize(new Dimension(1050,400));
            
            qtm.table_values(set);
            
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(table);
            tca.adjustColumns();
           
            add(scrollpane,BorderLayout.CENTER);
                     
            //setBounds(500, 500, 800, 500);
            //setVisible(true);
                //setVisible(true);
        }


public JTable get_table(){
	return this.table;
}

class QueryTableModel extends AbstractTableModel implements TableModel {
	  Vector cache; // will hold String[] objects . . .
	  int colCount;
	  String[] headers;
	  
	  public QueryTableModel() {
	    cache = new Vector();
	  }
	  public String getColumnName(int i) {
	    return headers[i];
	  }
	  public int getColumnCount() {
	    return colCount;
	  }
	  public int getRowCount() {
	    return cache.size();
	  }
	  public Object getValueAt(int row, int col) {
	    return ((String[]) cache.elementAt(row))[col];
	  }
	 
	  public void table_values(ResultSet set){
	  try {
          // StringBuffer output = new StringBuffer();
       	ResultSetMetaData meta = set.getMetaData();
       	colCount = meta.getColumnCount();

           // Now we must rebuild the headers array with the new column names
           headers = new String[colCount];
           for (int h = 1; h <= colCount; h++) {
             headers[h - 1] = meta.getColumnName(h);
           }            
           
           while (set.next()) {
               String[] record = new String[colCount];
               for (int i = 0; i < colCount; i++) {
                 record[i] = set.getString(i + 1);
               }
               cache.addElement(record);
             }
           fireTableChanged(null); // notify everyone that we have a new table.
       } catch (Exception e) {
         cache = new Vector(); // blank it out and keep going.
         e.printStackTrace();
       }
	  
	  }
}
}

