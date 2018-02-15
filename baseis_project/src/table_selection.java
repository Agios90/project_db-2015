import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.sql.*;

public class table_selection extends JFrame {
        private JButton choice[];
        private JPanel mainPanel = new JPanel();
        private int sel;
        private int size;
        
        public table_selection(int selection) {
                super("Select a Table");
                sel = selection;
                setLayout(new BorderLayout());
                setDefaultCloseOperation(HIDE_ON_CLOSE);
                try {
                    Class.forName ("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");
                    DatabaseMetaData dbMeta = conn.getMetaData();
                    String[] types = {"TABLE"};
                    ResultSet inter = dbMeta.getTables(null,null,"%",types);
                    inter.last();			//pame sto teleytaio kai blepoyme thn seira toy ara prosdiorizoyme to plhthos twn pinakwn
                    size = inter.getRow();
                    choice = new JButton[size];
                    mainPanel.setLayout(new GridLayout(size,1));

                    
                    int i=0;
                    inter.beforeFirst();
                    MyListener myListener = new MyListener();
                    while (inter.next()){
                    	
                        choice[i] = new JButton();
                        choice[i].setText(inter.getString("TABLE_NAME"));
                        choice[i].addActionListener(myListener);
                        mainPanel.add(choice[i++]);
                    }
                    conn.close();
                    add(mainPanel,BorderLayout.CENTER);
                    pack();
                }
                catch (SQLException excpt) {
                    System.out.print("SQL Exception"+ excpt);
                }
                catch (ClassNotFoundException exc) {
                    System.out.print("Driver not found :"+ exc);
                };
        }
//an einai private tote h table_selection_diagrafh poy epekteinei  thn table selectionklash den mporei na thn dei 
        protected class MyListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                int which=0;
                for (int i=0;i<size;i++)
                    if (e.getSource()==choice[i]) which=i;
                switch (sel) {
                    case 0:
                        String query = new String();
                        query = "Select * from "+choice[which].getText() + ";";
                        try {                            
                        	 Class.forName ("com.mysql.jdbc.Driver");
                             Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");                             
                             Statement stmt = conn.createStatement();
                             System.out.println(query);
                             ResultSet out = stmt.executeQuery(query);
                             new display_result(out).setVisible(true);                        
                         }
                         catch (ClassNotFoundException exc){
                             System.out.println("Driver not found :" +exc.getMessage());
                         }
                         catch (SQLException excpt) {
                              System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                         }break;
                     case 1:new display_part(choice[which].getText()).setVisible(true);break;
                     //to prwto orisma einai o pinakas poy 8a doylepsw,to deytero einai to JTextField toy father an eimai nested insert kai to trito
                     //einai 0 gia eisagwgh kai 1 gia update
                     case 2:new insert_update_tuple(choice[which].getText(),null,null).setVisible(true);break;
                     case 3:new delete_tuple(choice[which].getText()).setVisible(true);break;
                     case 4:new update_tuple(choice[which].getText()).setVisible(true);break;
                 }
                 setVisible(false);
             }
     }
 }
                    
                                                                                                                                                                        