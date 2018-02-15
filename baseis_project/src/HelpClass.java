import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class HelpClass {
	public HelpClass(){}
	
	/*  Aυτό επιστρέφει το ερωτημα για τα primary keys.Απο εδώ θα φτιάξω την λίστα
	 TABLE_CAT  TABLE_SCHEM  TABLE_NAME  COLUMN_NAME  KEY_SEQ  PK_NAME  
	my_project  null  employees  AFM  1  PRIMARY 	

	στο visits που έχω 2 κλειδια τυπώνει το εξής
	TABLE_CAT  TABLE_SCHEM  TABLE_NAME  COLUMN_NAME  KEY_SEQ  PK_NAME    
	my_project  null  visits  ClientRegistrationNo  2  PRIMARY  
	my_project  null  visits  PropertyRegistrationNo  1  PRIMARY  		
	 */		
				
				//epistrefw mia lista me int arithmous poy deixnoyn poies columns einai sto primary key
				public static ArrayList<Integer>  find_primary_keys(String s){
					ArrayList<Integer> list = new ArrayList<Integer>();
				try{	
					Class.forName ("com.mysql.jdbc.Driver");
	                Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password=");   
	                DatabaseMetaData dbMeta = conn.getMetaData();
	        		ResultSet prKeys = dbMeta.getPrimaryKeys(null,"my_project",s);		//s einai to onoma toy pinaka
	        		ResultSetMetaData prKeysMeta = prKeys.getMetaData();
	        		//typwnw ton pinaka me ta primary keys
	        		prKeys.beforeFirst();
	        /*		for(int i=0;i<prKeysMeta.getColumnCount();i++)
	            		System.out.printf(prKeysMeta.getColumnLabel(i+1) +"  ");
	                System.out.println();
	        */      while ( prKeys.next()) {
	                /*	for(int i=0;i<prKeysMeta.getColumnCount();i++)
	                		System.out.printf(prKeys.getString(i+1)+"  ");
	                	System.out.println();
	                */
	        			list.add(prKeys.getInt(5)); 
	                }
				} catch (ClassNotFoundException exc){
	                System.out.println("Driver not found :" +exc.getMessage());
	      		}
	      		catch (SQLException excpt) {
	      			 System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
	      		}	    
			//	System.out.println(""+list);  typwnei thn lista me ta primary keys
				return list;       		       		
				}
}
