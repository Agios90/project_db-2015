

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class display_part extends JFrame {
        private JPanel mainPanel = new JPanel();
        private JPanel bottomPanel = new JPanel();
        private int size;
        JCheckBox[] select;
        JTextField[] input;  
        JComboBox[] combo_boxes;
        JRadioButton and_button;
        JRadioButton or_button;
        JLabel[] type_null;			//pinakas apo string. (ena string gia ka8e sthlh toy pinaka ths bashs. grafei ton typo px int,enum,varchar kai an mporei na einai null. px null:yes  null:no)
        String s = new String();
        String[] relation = {"=","<",">"};
        String[][] enum_relation;	//pinakas apo pinakes apo string. gia thn ka8e sthlh exw enan pinaka.aytos o pinakas exei enan pinaka(ena plhthos) apo string pou einai oi diafores times tou enumeration
        Boolean[] bool_enum; 	//einai h oxi enum
        Boolean[] bool_auto_increment;	//einai h oxi auto increment
        Boolean[] bool_null;		//mporei na exei timh null nai h oxi
        
        public display_part(String table) {
           setLayout(new BorderLayout());
           s = table;          
           JLabel empty_label = new JLabel();
           try{
        	    Class.forName ("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");              
                DatabaseMetaData dbMeta = conn.getMetaData();
                ResultSet inter = dbMeta.getColumns(null,null,table,"%");
               
                inter.last();
                size = inter.getRow();		//briskw to plh8os twn columns
                
                input = new JTextField[size];
                select = new JCheckBox[size];
                combo_boxes = new JComboBox[size];
                bool_enum = new Boolean[size];
                bool_auto_increment = new Boolean[size];		//ayto kai to apokatw se ayto to programma den xreiazontai alla 8a xreiastoyn sto insert
                bool_null = new Boolean[size];
                type_null = new JLabel[size];			//exei to keimenaki pou bazw dipla apo to column name gia ton typo kai an einai null h oxi
                enum_relation = new String[size][];		//by_reference.java                 
               // str_array[2] = new String[2];
               // System.out.println(str_array[2].length);
                
                mainPanel.setLayout(new GridLayout(size,4,1,1));
                find_properties_of_columns(table,conn);
                int i=0;
                inter.beforeFirst();
                while (inter.next()){
                     input[i] = new JTextField();
                     select[i] = new JCheckBox(inter.getString("COLUMN_NAME"));
                     mainPanel.add(select[i]);                                     
                     mainPanel.add(type_null[i]);
                     if(bool_enum[i]==true){
                    	 combo_boxes[i] = new JComboBox(enum_relation[i]);
                    	 mainPanel.add(combo_boxes[i]);
                    	 empty_label= new JLabel();      		//pros8etoume ena keno label gia na kalhpsei to keno pou den bazoyme to Jtextfield             	
                    	 mainPanel.add(empty_label);
                     }
                     else{
                    	 combo_boxes[i] = new JComboBox(relation); 
                    	 mainPanel.add(combo_boxes[i]);
                    	 mainPanel.add(input[i]);
                     }
                     i++;
                }
                conn.close();
                add(mainPanel,BorderLayout.CENTER);
                JLabel top = new JLabel("Display part of table: "+table);
                add(top,BorderLayout.NORTH);
                JButton clear = new JButton();
                clear.setText("Clear entries");
                clear.addActionListener(new ClearListener());
                JButton display = new JButton();
                display.setText("Submit query");
                display.addActionListener(new DisplayListener());
                bottomPanel.setLayout(new GridLayout(1,2));
                bottomPanel.add(clear);
                bottomPanel.add(display);                                
                add(bottomPanel,BorderLayout.SOUTH);
                
                and_button = new JRadioButton("and");
                or_button = new JRadioButton("or");
                ButtonGroup radio_group = new ButtonGroup();
                radio_group.add(and_button);
                radio_group.add(or_button);
                JPanel for_and_or = new JPanel(new GridLayout(2,1));               
                for_and_or.add(and_button);
                for_and_or.add(or_button);
                and_button.setSelected(true);            
                add(for_and_or,BorderLayout.EAST);            
                pack();
            }
            catch (SQLException excpt) {
                  System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (ClassNotFoundException exc){
                        System.out.println("Driver not found :" +exc.getMessage());
            }
         }
       
        private void find_properties_of_columns(String table,Connection conn) {
        	try{
        		//select column_name,data_type,column_type,is_nullable,extra data from information_schema.columns where table_schema='my_project' and table_name='employees';
        		Statement statement =conn.createStatement ();
        		String str = "select column_name,data_type,column_type,is_nullable,extra data from information_schema.columns where table_schema=\'my_project\' and table_name=\'"+table+"\'; ";              
                ResultSet results = statement.executeQuery(str);
                int i=0;
                while(results.next()){
                	String data_type = results.getString(2);			//px int,varchar,enum
                	String can_be_null = results.getString(4);			//no,yes
                	String is_auto_increment =results.getString(5);  //an den einai null tote einai auto_increment
                	
                	type_null[i] = new JLabel(" "+data_type+", null:"+can_be_null);
                	bool_null[i]=false;
                	if(data_type.trim().equals("YES"))
                		bool_null[i]=true;              
                	bool_enum[i]=false;
                	if(data_type.trim().equals("enum"))
                		bool_enum[i]=true;                             
           
                	if(bool_enum[i]==true){	//kseroume hdh oti einia enum alliws den mpainoume ka8olou
                		String enum_string = results.getString(3);               		               		
                		Pattern p = Pattern.compile("\'(\\w+)\'");              
                		Matcher m = p.matcher(enum_string);
                		ArrayList<String> my_enums = new ArrayList<String>();
                		while (m.find()) 		//twra briskei oles tis pi8anes times kai tis bazei se mia lista
                			my_enums.add(m.group(1));             
                		enum_relation[i] = new String[my_enums.size()];
                		int j=0;
                		for ( String s : my_enums)
                			enum_relation[i][j++] = s;  
                	}			//telos tou if pou pairnei tis pi8anes times tou enum           
                	i++;
                }  //telos tou while loop
        	}
        	catch (SQLException excpt) {
                 System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
           }          
		}

		private class DisplayListener implements ActionListener {
			
            public void actionPerformed(ActionEvent e) {    
            	String and_or_str;
            	if(and_button.isSelected())
            		and_or_str = " and ";
            	else
            		and_or_str = " or ";
                StringBuffer query = new StringBuffer(); 
                query.append("Select * from "+s);
                boolean first_qritirio=true;              
                for (int i=0;i<size;i++){
                    if (select[i].isSelected()){
                    	//System.out.println("shmeio1");
                    	if(first_qritirio==true){
                    		query.append(" where ");
                    		first_qritirio=false;
                    		if(bool_enum[i]==true )	
                            	query.append(" "+select[i].getText()+" = \'"+combo_boxes[i].getSelectedItem().toString()+ "\' ");
                    		else
                    			query.append(" "+select[i].getText()+combo_boxes[i].getSelectedItem().toString()+ "\'"+input[i].getText()+"\' ");              
                    	}
                    	else if(bool_enum[i]==true )	//twra eimaste se 2 h parapanw kritirio ara prepei na baloume and or metaksy tous
                    		query.append(" "+and_or_str+select[i].getText()+" = \'"+combo_boxes[i].getSelectedItem().toString()+ "\' ");
                    	else /*den einai prwto kritirio kai den einai enum  */
                    		query.append(" "+and_or_str+select[i].getText()+combo_boxes[i].getSelectedItem().toString()+ "\'"+input[i].getText()+"\' ");
                    }
                }
                query.append(";");
                System.out.println(query);
                try{
                    Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password="); 
                    Statement stmt = conn.createStatement();
                    ResultSet out = stmt.executeQuery(query.toString());
                    new display_result(out).setVisible(true);
                }
                catch (SQLException excpt) {
                     System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                }               
                setVisible(false);
            }
        }
        
        private class ClearListener implements ActionListener {       	
        	
            public void actionPerformed(ActionEvent e) {
                     for (int i=0;i<size;i++){
                    display_part.this.input[i].setText("");
                }
            }
       }

}
