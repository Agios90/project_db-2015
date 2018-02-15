import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*idio me to arxeio update_tuple.java*/


public class delete_tuple extends JFrame{
	JTable table;
	ResultSet out;
	String tuple_for_delete[];
	String delete_table;
	int selectedRow;
	
	public delete_tuple(String delete_table){
		this.delete_table= delete_table;
		readTable();	
	}
	
	private void readTable(){
		//anaktw oloklhro ton pinaka table apo thn bash dedomenwn
		String query = new String();
        query = "Select * from "+delete_table + ";";
        try {                            
        	 Class.forName ("com.mysql.jdbc.Driver");
             Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");                             
             Statement stmt = conn.createStatement();
             System.out.println(query);
             out = stmt.executeQuery(query);
             //new display_result(out).setVisible(true);                        
         }      
         catch (SQLException excpt) {
              System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
         }
         catch (ClassNotFoundException exc){
            System.out.println("Driver not found :" +exc.getMessage());
         }
		
		result_table table_panel = new result_table(out);
		table = table_panel.get_table();
		table.getSelectionModel().addListSelectionListener(new RowListener());
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tuple_for_delete = new String [table.getColumnCount()];
				
        setLayout(new BorderLayout());
        setDefaultCloseOperation(HIDE_ON_CLOSE);    
        add(table_panel,BorderLayout.CENTER);   
        
        JButton update_button = new JButton("delete");
        update_button.addActionListener(new DeleteListener());
        add(update_button,BorderLayout.SOUTH);
        pack();        
	}
	
	
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            //selectedRow = table.getSelectionModel().getLeadSelectionIndex();
            selectedRow = table.getSelectedRow();
            if(selectedRow>=0){
            	System.out.println("ROW SELECTION EVENT. row= "+selectedRow);  
            	for(int i=0;i<tuple_for_delete.length;i++)
            		tuple_for_delete[i]= (String) table.getValueAt(selectedRow,i); 
            }
        }
    }
	
	private class DeleteListener implements ActionListener {       	   	
        public void actionPerformed(ActionEvent e) { 
        	StringBuffer query = new StringBuffer(); 
            query.append("Delete from "+delete_table+" where ");     
            //twra 8a grapsw tis times poy eixe prin h toypla sta primary keys
            
            boolean firstTime=true;
            ArrayList<Integer> list =HelpClass.find_primary_keys(delete_table);
            for(int i=0;i<list.size();i++){
            	if(firstTime==false)
            		query.append(" and ");
            	int temp = list.get(i);
            	query.append(table.getColumnName(temp-1)+"=\'"+tuple_for_delete[temp-1]+"\'");
            	firstTime=false;
            }
            query.append(";");
            System.out.println(query);
            try{
                Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password="); 
                Statement stmt = conn.createStatement();              
                stmt.executeUpdate(query.toString());
        	
                //readTable();
               // pullThePlug();
                new delete_tuple(delete_table).setVisible(true);
                dispose();
                
            }
            catch (SQLException excpt) {
                 System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"δεν μπορείς να σβησεις απο τον πινακα " +delete_table+" το στοιχειο αυτο γιατι χρησιμοποιείται από αλλους πίνακες","FK restraint", JOptionPane.INFORMATION_MESSAGE);
            }
        }	
	}
	
}
