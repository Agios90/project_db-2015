/*http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableSelectionDemoProject/src/components/TableSelectionDemo.java   */
import java.awt.BorderLayout;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class display_result_interactive extends JFrame{
	JTable table;
	JTextField text_field;
	int primary_column;
	//primary_column einai to noumero ths sthlhs pou einai primary key
	public display_result_interactive(ResultSet set,int primary_column,JTextField text_field){
		result_table table_panel = new result_table(set);
		this.text_field = text_field; 
		this.primary_column = primary_column; 
		table = table_panel.get_table();
		table.getSelectionModel().addListSelectionListener(new RowListener());
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
				
        setLayout(new BorderLayout());
        setDefaultCloseOperation(HIDE_ON_CLOSE);    
        add(table_panel,BorderLayout.CENTER);            
        pack();        
	}
	
	
	//mas endiaferei mono h seira h opoia epilegei oxi h sthlh
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            int row_selected = table.getSelectionModel().getLeadSelectionIndex();
            System.out.println("ROW SELECTION EVENT. row= "+row_selected);
            //twra pou kserw thn seira paw se aythn kai pairnw apo thn stlhlh primary_column to pedio  
            //primary_column-1 dioti to primary_column to phrame symfwna me ta metadata pou arxizoyn thn ari8mhsh
            //apo to 1. omws h getValueAt pairnei thn timh apo ton pinaka kai ksekinaei apo to 0
            text_field.setText(""+table.getValueAt(row_selected,primary_column-1));
           
        }
    }
}
