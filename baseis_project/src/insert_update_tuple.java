/*gia na exw kenes theseis sto grid bazw kena labels.Omws den mporw na exw ena keno Label kai na to pros8etw 
 * opou 8elw keno. Kathe fora prepei na dhmioyrgw kainourgio keno label*/


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.math.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class insert_update_tuple extends JFrame {
        private JPanel mainPanel = new JPanel();
        private JPanel bottomPanel = new JPanel();
        private JPanel topPanel = new JPanel();
        private int size;
 //     ButtonGroup group = new ButtonGroup();
 //     JRadioButton[] select;
        JLabel[] select;
        JTextField[] input;  
        JComboBox[] combo_boxes;
        JLabel[] type_null;			//pinakas apo string. (ena string gia ka8e sthlh toy pinaka ths bashs. grafei ton typo px int,enum,varchar kai an mporei na einai null. px null:yes  null:no)
        String s = new String();
        String[][] enum_relation;	//pinakas apo pinakes apo string. gia thn ka8e sthlh exw enan pinaka.aytos o pinakas exei enan pinaka(ena plhthos) apo string pou einai oi diafores times tou enumeration
        Boolean[] bool_enum; 	//einai h oxi enum
        Boolean[] bool_auto_increment;	//einai h oxi auto increment
        Boolean[] bool_null;		//mporei na exei timh null nai h oxi
        String[][] fkeys;
        JLabel picture;
        JButton[] show_values;
        JButton[] insert_new;
        JTextField parent_insert_field;
        String old_tuple[]; //null an einai insert kai diaforo gia update. sto udate exoyme enan pinaka apo string gia tis palies times.
        
        //to parent_insert einai null an einai kanoniko insert einai diaforo tou null an einai nesting insert kai deixnei ston patera 
        public insert_update_tuple(String table,JTextField parent_insert,String  old_tuple[]) {
           setLayout(new BorderLayout());
           s = table;        
           parent_insert_field = parent_insert;
           this.old_tuple = old_tuple;
           picture = new JLabel();
           picture.setIcon(new ImageIcon("./photos_db/"+table+".jpg"));
           try{
        	    Class.forName ("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");              
                DatabaseMetaData dbMeta = conn.getMetaData();
                ResultSet inter = dbMeta.getColumns(null,null,table,"%");
               
                inter.last();
                size = inter.getRow();		//briskw to plh8os twn columns
                
                input = new JTextField[size];
                select = new JLabel[size];
                combo_boxes = new JComboBox[size];
                bool_enum = new Boolean[size];
                bool_auto_increment = new Boolean[size];		//ayto kai to apokatw se ayto to programma den xreiazontai alla 8a xreiastoyn sto insert
                bool_null = new Boolean[size];
                type_null = new JLabel[size];			//exei to keimenaki pou bazw dipla apo to column name gia ton typo kai an einai null h oxi
                enum_relation = new String[size][];		//by_reference.java       
                JLabel empty_label = new JLabel();
                show_values = new JButton[size];       
                insert_new = new JButton[size];
                JButton invisible_button = new JButton();
               // str_array[2] = new String[2];
               // System.out.println(str_array[2].length);
                
                mainPanel.setLayout(new GridLayout(0,5,1,1));
                find_properties_of_columns(table,conn);
                                
             
                //foreign keys section
                ShowValuesListener list = new ShowValuesListener();
                
                ResultSet forKeys = dbMeta.getImportedKeys(null,"my_project",s);
                forKeys.last();
                int forKeysNum = forKeys.getRow();		//briskw to plh8os twn foreign keys
                fkeys = new String[forKeysNum][2];		//onoma_sthlhs_pou_einai_forKey - onoma_pinaka_pou_exei_to_authentiko_kleidi                
                
//              ResultSetMetaData forKeysMeta = forKeys.getMetaData();
                int count=0;
                forKeys.beforeFirst();
/*                for(int i=0;i<forKeysMeta.getColumnCount();i++)
            		System.out.printf(forKeysMeta.getColumnLabel(i+1) +"  ");
                System.out.println();
*/              while ( forKeys.next()) {
/*                	for(int i=0;i<forKeysMeta.getColumnCount();i++)
                		System.out.printf(forKeys.getString(i+1)+"  ");
*/                	System.out.println();
                	fkeys[count][0]=forKeys.getString(/*8*/ "FKCOLUMN_NAME");		//sthlh 0 onoma ths sthlhs sou poy einai foreign key
                	fkeys[count][1]=forKeys.getString(/*3*/ "PKTABLE_NAME");		//onoma tou allou pinaka		8,3
                	count++;
                }
                
                
                //end of foreign keys section
                
                int i=0;
                inter.beforeFirst();
                while (inter.next()){              
                	if(bool_auto_increment[i]==true){
                		input[i] = new JTextField();
                		select[i] = new JLabel(inter.getString("COLUMN_NAME"));
                		i++;	//an einai auto_increment tote ayto to pedio toy pinaka to paraleipw kai den to emfanizw katholoy
                		continue;
                	}
                     input[i] = new JTextField();
                     select[i] = new JLabel(inter.getString("COLUMN_NAME"));
                     mainPanel.add(select[i]);                                     
                     mainPanel.add(type_null[i]);
                     if(bool_enum[i]==true){
                    	 combo_boxes[i] = new JComboBox(enum_relation[i]);
                    	 mainPanel.add(combo_boxes[i]);                   	             	                   
                     }
                     else //eisagw ena JTextField
                    	 mainPanel.add(input[i]);
                     if(is_foreign_key(inter.getString("COLUMN_NAME"),count)){
                    	 show_values[i] = new JButton("show values");
                    	 insert_new[i] = new JButton("insert new");
                    	 show_values[i].addActionListener(list);
                    	 insert_new[i].addActionListener(list);
                    	 mainPanel.add(show_values[i]);
                    	 mainPanel.add(insert_new[i]);
                     }
                     else{
                    	 show_values[i] = invisible_button;
                    	 insert_new[i] = invisible_button;
                    	 mainPanel.add(/*empty_label*/new JLabel(""));		//an den einai foreign key tote bale dyo adeia labels anti gia koumpia.
                    	 mainPanel.add(/*empty_label*/new JLabel(""));
                     }                   	                     	
                     i++;
                }
                
                if(old_tuple != null) //an einai update tote bale sta pedia input na fainontai oi times poy exei hdh h toypla prin to update
                	show_values_before_update();
                
                
                conn.close();
                add(mainPanel,BorderLayout.CENTER);
                JLabel top = new JLabel("insert tuple in table: "+table);
                topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                topPanel.add(picture);
                topPanel.add(top);
                add(topPanel,BorderLayout.NORTH);
                JButton clear = new JButton();
                clear.setText("Clear entries");
                clear.addActionListener(new ClearListener());
                JButton display = new JButton();
                display.setText("Submit insert");
                display.addActionListener(new DisplayListener());
                bottomPanel.setLayout(new GridLayout(1,2));
                bottomPanel.add(clear);
                bottomPanel.add(display);                                
                add(bottomPanel,BorderLayout.SOUTH);               
                pack();
            }
            catch (SQLException excpt) {
                  System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (ClassNotFoundException exc){
                        System.out.println("Driver not found :" +exc.getMessage());
            }
         }
       
        private boolean is_foreign_key(String column_name,int number_of_foreign_keys) {
			for(int i=0;i<number_of_foreign_keys;i++)
				if(column_name.equals(fkeys[i][0]))
					return true;
			return false;
		}

		private void find_properties_of_columns(String table,Connection conn) {
        	try{
        		//select column_name,data_type,column_type,is_nullable,extra data from information_schema.columns where table_schema='my_project' and table_name='employees';
        		Statement statement =conn.createStatement ();
        		String str = "select column_name,data_type,column_type,is_nullable,extra data from information_schema.columns where table_schema=\'my_project\' and table_name=\'"+table+"\'; ";              
                ResultSet results = statement.executeQuery(str);
                int i=0;
                while(results.next()){
                	String data_type = results.getString(2);
                	String can_be_null = results.getString(4);
                	String is_auto_increment =results.getString(5);  //an den einai null tote einai auto_increment
                	
                	type_null[i] = new JLabel(" "+data_type+", null:"+can_be_null);
                	bool_null[i]=false;
                	if(data_type.trim().equals("YES"))
                		bool_null[i]=true;              
                	bool_enum[i]=false;
                	if(data_type.trim().equals("enum"))
                		bool_enum[i]=true;   
                	bool_auto_increment[i]=true;
                	if(is_auto_increment.trim().equals(""))
                		bool_auto_increment[i]=false;
           
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
		}	//telos ths synarthshs
		
		
		void show_values_before_update(){
			for(int i=0;i<size;i++){							             
				 if(bool_auto_increment[i]==true){
					//an einai auto_increment tote ayto to pedio toy pinaka to paraleipw kai den to emfanizw katholoy ara den to tropopoiw kiolas
				 }                             
                 else if(bool_enum[i]==true){		//an einai enumerator tote 8elw na exw san preselected thn timh pou eixe h palia toupla
                	 int preselected=0;
                	 while(preselected<combo_boxes[i].getItemCount()){
                		 if(old_tuple[i].equals(combo_boxes[i].getItemAt(preselected)))
                			 break;
                		 preselected++;
                	 }
                	 combo_boxes[i].setSelectedIndex(preselected);                	                	             	                   
                 }
                 else{ //einai aplo JTextField 
                	 input[i].setText(old_tuple[i]);
                 }                	                     	             
            }			
		}

		private class DisplayListener implements ActionListener {
			
            public void actionPerformed(ActionEvent e) {  
            	if(old_tuple != null){
            		update_tuple();
            		return;
            	}
                StringBuffer query = new StringBuffer(); 
                query.append("Insert into "+s+" values (");             
                if (bool_auto_increment[0]==true)
                	query.append("null");
                else if(bool_enum[0]==true )	
                	query.append("\'"+combo_boxes[0].getSelectedItem().toString()+ "\'");
        		else{
        			if (input[0].getText().trim().equals(""))
        				query.append("null");
        			else
        				query.append("\'"+input[0].getText()+"\'");              
        			
        		}
                for (int i=1;i<size;i++){
                	if (bool_auto_increment[i]==true)
                    	query.append(",null");
                    else if(bool_enum[i]==true )	
                    	query.append(",\'"+combo_boxes[i].getSelectedItem().toString()+ "\'");
            		else{
            			if (input[i].getText().trim().equals(""))
            				query.append(",null");
            			else
            				query.append(",\'"+input[i].getText()+"\'");                 
            		     }
            	}
                query.append(");");
                System.out.println(query);
                if(!type_validation())
                	return;
                try{
                    Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password="); 
                    Statement stmt = conn.createStatement();
                    if(parent_insert_field==null)
                    	stmt.executeUpdate(query.toString());
                    else{
                    	stmt.executeUpdate(query.toString(),Statement.RETURN_GENERATED_KEYS);
                    	ResultSet rs = stmt.getGeneratedKeys();
                    	if (rs.next()){
                            parent_insert_field.setText(rs.getString(1));
                        }
                        rs.close();

                        stmt.close();
                    }
                    	
                }
                catch (SQLException excpt) {
                	
                     System.out.println("SQLException :" +excpt);
                     
                     String exception=excpt.getMessage();
                     if (excpt.getMessage().contains("FOREIGN KEY")){
                    	 
                    	String search="FOREIGN KEY";
                     	String message =new String();
                     	message=exception.substring(exception.indexOf(search)+search.length());
                     	message=message.substring(3,message.indexOf("`)"));
                     	System.out.println(message);      
                     	JOptionPane.showMessageDialog(null,"o " +message+" δεν είναι έγκυρος","error", JOptionPane.INFORMATION_MESSAGE);
                     }
                     else
                     {
                    	 JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                     }
                     return;
                    	 
                }               
                setVisible(false);
            }

			private void update_tuple() {
				StringBuffer query = new StringBuffer(); 
                query.append("Update "+s+" set "); 
                int i=0;
                while(bool_auto_increment[i]==true)
                		i++; //mhn metaballeis ta auto_increment pedia ara ta prospernaw mexri na brw kapoio poy den einai ayto_increment
                if(bool_enum[i]==true )	
                	query.append(select[i].getText()+"=\'"+combo_boxes[i].getSelectedItem().toString()+ "\'");    	
        		else{
        			if (input[i].getText().trim().equals(""))
        				query.append(select[i].getText()+"=null");
        			else
        				query.append(select[i].getText()+"=\'"+input[i].getText()+"\'");  
        		    }       
                i++;
                for (;i<size;i++){
                	if (bool_auto_increment[i]==true)
                	{} //ta auto_increment den ta metaballeis
                    else if(bool_enum[i]==true )	
                    	query.append(", "+ select[i].getText()+"=\'"+combo_boxes[i].getSelectedItem().toString()+ "\'");
            		else{
            			if (input[i].getText().trim().equals(""))
            				query.append(", "+ select[i].getText()+"=null");
            			else
            				query.append(", "+ select[i].getText()+"=\'"+input[i].getText()+"\'");                 
            		     }
            			               
                }
                query.append(" where ");
                //twra 8a grapsw tis times poy eixe prin h toypla sta primary keys
                
                boolean firstTime=true;
                ArrayList<Integer> list =HelpClass.find_primary_keys(s);
                for(int i1=0;i1<list.size();i1++){
                	if(firstTime==false)
                		query.append(" and ");
                	int temp = list.get(i1);
                	query.append(select[temp-1].getText()+"=\'"+old_tuple[temp-1]+"\'");
                	firstTime=false;
                }
                query.append(";");
                System.out.println(query);
                if(!type_validation())
                	return;
                try{
                    Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password="); 
                    Statement stmt = conn.createStatement();
                    if(parent_insert_field==null)
                    	stmt.executeUpdate(query.toString());
                    else{
                    	stmt.executeUpdate(query.toString(),Statement.RETURN_GENERATED_KEYS);
                    	ResultSet rs = stmt.getGeneratedKeys();
                    	if (rs.next()){
                            parent_insert_field.setText(rs.getString(1));
                        }
                        rs.close();

                        stmt.close();
                    }
                    	
                }
                catch (SQLException excpt) {
                     System.out.println("SQLException :" +excpt);
                     
                     String exception=excpt.getMessage();
                     if (excpt.getMessage().contains("FOREIGN KEY")){
                    	 
                     	String search="FOREIGN KEY";
                      	String message =new String();
                      	message=exception.substring(exception.indexOf(search)+search.length());
                      	message=message.substring(3,message.indexOf("`)"));
                      	System.out.println(message);      
                      	JOptionPane.showMessageDialog(null,"o " +message+" δεν είναι έγκυρος","error", JOptionPane.INFORMATION_MESSAGE);
                      }
                      else
                      {
                     	 JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                      }
                     return;
                
                }               
                setVisible(false);       				
			}//telos ths synarthshs  update tuple
			
		private boolean type_validation(){
			
			if(s.equals("employees")){

				float salary;
				int phone;
                int postalcode;
				
				try {
					
				     salary = Float.parseFloat(input[6].getText());
				     phone = Integer.parseInt(input[7].getText());
				     postalcode = Integer.parseInt(input[5].getText());
				     
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"Error on table employees: salary, phone and postal code must be numbers","error", JOptionPane.ERROR_MESSAGE);	
				    return false; 
					}
				    
               // elaxistos misthos
                if (salary < 680){ 
                	 JOptionPane.showMessageDialog(null,"Error on table Employees : Salary is lower than minimum wage","error", JOptionPane.ERROR_MESSAGE); 
                	 return false;
                }	
                if ((phone < 100000)|| (postalcode > 99999)){ 
               	 JOptionPane.showMessageDialog(null,"Error on table Employees : Phone number or postal code are incorrect","error", JOptionPane.ERROR_MESSAGE); 
               	 return false;
                }
                return true;
            }
			else if(s.equals("private_owners")){
				
				int phone;
                
				try {
					
				     phone = Integer.parseInt(input[1].getText());
				     
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"Error on table private owners: phone must be a number","error", JOptionPane.ERROR_MESSAGE);	
				    return false; 
					}
				    	
                if (phone < 100000){ 
               	 JOptionPane.showMessageDialog(null,"Error on table private owners : Phone number is incorrect","error", JOptionPane.ERROR_MESSAGE); 
               	 return false;
                }
                return true;
            }
			else if(s.equals("owners")){
				
				int postalcode;
                
				try {
					
				     postalcode = Integer.parseInt(input[3].getText());
				     
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"Error on table private owners: postal code must be a number","error", JOptionPane.ERROR_MESSAGE);	
				    return false; 
					}
				    	
                if (postalcode > 99999){ 
               	 JOptionPane.showMessageDialog(null,"Error on table private owners : Postal code is incorrect","error", JOptionPane.ERROR_MESSAGE); 
               	 return false;
                }
                return true;
            }
			else if(s.equals("business_owners")){
				
				int phone;
                
				try {
					
				     phone = Integer.parseInt(input[3].getText());
				     
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"Error on table business owners: phone must be a number","error", JOptionPane.ERROR_MESSAGE);	
				    return false; 
					}
				    	
                if (phone < 100000){ 
               	 JOptionPane.showMessageDialog(null,"Error on table business owners : Phone number is incorrect","error", JOptionPane.ERROR_MESSAGE); 
               	 return false;
                }
                return true;
            }
			
			return true;
			
		}

			
        }
        
        private class ClearListener implements ActionListener {       	
        	
            public void actionPerformed(ActionEvent e) {
                     for (int i=0;i<size;i++){
                    insert_update_tuple.this.input[i].setText("");
                }
            }
       }
        
        private class ShowValuesListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	int i,fk;
            	fk=0;
            	int flag=5;
            	for (i=0;i<size;i++)
            		if (e.getSource() == show_values[i] ) {
            			fk=i;
            			flag=0;
            		}	
            	for (i=0;i<size;i++)
            		if (e.getSource() == insert_new[i] ) {
            			fk=i;
            			flag=1;
            		}
            	String table_name = null;
            	String column_name=select[fk].getText();  //to onoma ths sthlhs pou einai forreign key
            	for(i=0;i<fkeys.length;i++)
            		if(column_name.equals(fkeys[i][0]))
            			table_name = fkeys[i][1];
            	if (flag==0){           		
            		String query = "Select * from "+table_name+ ";";              		            		
            		
            		try {                   
            			Class.forName ("com.mysql.jdbc.Driver");
                        Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");                     
            			Statement stmt = conn.createStatement();
            			System.out.println(query);
            			ResultSet out = stmt.executeQuery(query);
            			int primary_column = find_primary_key_of_table(table_name,conn);
            			new display_result_interactive(out,primary_column,input[fk]).setVisible(true);                        
            		}
            		catch (ClassNotFoundException exc){
                      System.out.println("Driver not found :" +exc.getMessage());
            		}
            		catch (SQLException excpt) {
            			 System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
            		}			
            	}
            	else
            	new insert_update_tuple(table_name,input[fk],null).setVisible(true);
				
            
            
            
            }

			private int find_primary_key_of_table(String table_name,Connection conn) {
				int count=0;
				 //find which colomn of the table_name table is the primary key
				try{
				DatabaseMetaData dbMeta = conn.getMetaData();
        		ResultSet prKey = dbMeta.getPrimaryKeys(null,"my_project",table_name);
        		//ypothetoume oti exoyme ena primary key ston pinaka pou koitame tis times       		
        		String primary_name=null;
        		while(prKey.next()){
        			primary_name = prKey.getString(/*4*/"COLUMN_NAME");  //to onoma ths sthlhs pou einai primary key
        			count++;
				}
        		if(count!=1){
        			System.out.println("find_primary_key_of_table  must have 1 primary key. count="+count);
        			System.exit(-1);
        		}       
        		//twra 8a brw to onoma ths sthlhs ayths se poio noumero sthlhs antistoixei
        		ResultSet inter = dbMeta.getColumns(null,null,table_name,"%");
        		count=1;
        		while(inter.next()){
        			if(inter.getString("COLUMN_NAME").equals(primary_name))
        				break;
        			count++;
        		}
        		System.out.println("column_number = "+count);
				}
				catch (SQLException excpt) {
                     System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                } 
				return count;
			}	//telos ths synarthshs
       }
        

}
