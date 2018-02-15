import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class write_sql_query extends JFrame{
	JTextArea textArea = new JTextArea(5,50);
	public  write_sql_query() {
		super("write direct query");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(HIDE_ON_CLOSE);          
        JButton send = new JButton("send query");
        send.addActionListener(new MyListener());
        JPanel middle = new JPanel();
        JScrollPane scrollPane = new JScrollPane(textArea); 
        middle.add(scrollPane);
        //textArea.setSize(new Dimension(60,120));
        
        JPanel south = new JPanel();
        south.add(send);
        add(middle,BorderLayout.CENTER);
        add(south,BorderLayout.SOUTH);
        pack();
	}
	
	 protected class MyListener implements ActionListener{
         public void actionPerformed(ActionEvent e) {
        	 String query = new String();
        	 query = textArea.getText();
        	 try {                            
        		 Class.forName ("com.mysql.jdbc.Driver");
        		 Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");                             
        		 Statement stmt = conn.createStatement();
        		 System.out.println(query);
        		 ResultSet out;
        		 if((query.toLowerCase().contains("update")) || (query.toLowerCase().contains("delete"))  || (query.toLowerCase().contains("insert")))
        			 stmt.executeUpdate(query);
        		 else{
        			 out = stmt.executeQuery(query);
        			 new display_result(out).setVisible(true); 
        		 }
        	 }
        	 catch (ClassNotFoundException exc){
        		 System.out.println("Driver not found :" +exc.getMessage());
        	 }
        	 catch (SQLException excpt) {
        		  System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
        	 }
         }
	 }
	
}
