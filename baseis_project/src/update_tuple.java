import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class update_tuple extends JFrame{
	JTable table;
	ResultSet out;
	String tuple_for_update[];
	String update_table;
	
	public update_tuple(String update_table){
		//anaktw oloklhro ton pinaka table apo thn bash dedomenwn
		String query = new String();
		this.update_table= update_table;
        query = "Select * from "+update_table + ";";
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
		
		tuple_for_update = new String [table.getColumnCount()];
				
        setLayout(new BorderLayout());
        setDefaultCloseOperation(HIDE_ON_CLOSE);    
        add(table_panel,BorderLayout.CENTER);   
        
        JButton update_button = new JButton("update");
        update_button.addActionListener(new UpdateListener());
        add(update_button,BorderLayout.SOUTH);
        pack();        
	}
	
	
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            int row_selected = table.getSelectionModel().getLeadSelectionIndex();
            System.out.println("ROW SELECTION EVENT. row= "+row_selected);
            //twra poy exw epileksei mia seira phgainw kai antigrafw ola ta pedia ths seiras toy pinaka ston tuple_for_update pinaka
            for(int i=0;i<tuple_for_update.length;i++)
            	tuple_for_update[i]= (String) table.getValueAt(row_selected,i);                  
        }
    }
	
	private class UpdateListener implements ActionListener {       	   	
        public void actionPerformed(ActionEvent e) { 
        	//System.out.println(" "+update_tuple.this.update_table+" "+ update_tuple.this.tuple_for_update);
                new insert_update_tuple(update_tuple.this.update_table,null,update_tuple.this.tuple_for_update).setVisible(true); 
        }
   }
	
}
