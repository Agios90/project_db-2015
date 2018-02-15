
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;

public class execute_query extends JFrame {
	private JButton choice[] = new JButton[12];
	private JButton choiceexpl[] = new JButton[12];
	private JPanel mainPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	

	private String queriesexpl[] = new String[12];
	private String queries[] = new String[12];
	JLabel picture;

	
	public execute_query() {
                super("Queries and Views");                 
                setLayout(new BorderLayout());
		        setDefaultCloseOperation(HIDE_ON_CLOSE);
                mainPanel.setLayout(new GridLayout(12,1,1,1));
                
                
                int i=0;
                

                queriesexpl[i]=("Τα ονόματα και οι διευθύνσεις των ιδιωτών που έχουν καταχωρήσει το σπίτι τους για ενοικίαση");
                queries[i++]= 
                		"select FirstName, LastName, Addr_StreetName as StreetName, Addr_StreetNo as StreetNumber	\n" +
                		"from private_owners natural join properties;\n";
                    
                
                queriesexpl[i]="Τα ονόματα ιδιωτών ΚΑΙ επιχειρήσεων που έχουν καταχωρήσει το σπίτι τους για ενοικίαση ";
                queries[i++]=
                		"(select FirstName, LastName, Addr_StreetName as StreetName, Addr_StreetNo as StreetNumber	\n"+
                		"from private_owners natural join properties) \n"+
                		"union	\n"+
                		"(select ContactFirstName, ContactLastName, Addr_StreetName as StreetName, Addr_StreetNo as StreetNumber	\n"+
                		"from business_owners natural join properties); 	\n";  
                
               
                queriesexpl[i]="Εμφάνιση του ελάχιστου, του μέγιστου και του μέσου όρου μισθού για τους προιστάμενους και τους υφιστάμενους";
                queries[i++]=
                		"(select min(salary), avg(salary), max(salary)	\n"+
                		"from Employees where SupervisorAFM is null)	\n"+
                		"union		\n"+
                		"(select min(salary), avg(salary), max(salary)			\n"+
                		"from Employees where SupervisorAFM is not null);	\n";
                		
                		
                
                
                
                queriesexpl[i]="Εμφάνιση της διεύθυνσης όλων των ακινήτων και του είδους τους";
                queries[i++]=
                		"select addr_Streetname as StreetName, addr_StreetNo as StreetNo, Description\n"+
                		"from Property_types natural join properties;			\n";
                		

                
                
                queriesexpl[i]="Εμφάνιση του μέσου όρου ενοικίου ανά είδος ακινήτου";
                queries[i++]=
                		"select Description, avg(rent) as Rent	\n"+
                		"from property_types natural join properties				\n"+
                		"group by PropertyTypeId;			\n";

                
                
                
                queriesexpl[i]="Εμφάνιση των διευθύνσεων των ακινήτων που βρίσκονται υπό συμβόλαιο, με αύξουσα σειρά ενοικίου";
                queries[i++]=
                		"select addr_streetname as StreetName, addr_streetno as StreetNumber, contracts.rent as Rent	\n"+
                		"from contracts, properties											\n"+
                		"where contracts.PropertyRegistrationNo = properties.PropertyRegistrationNo									\n"+
                		"order by contracts.rent;					\n";

                		
                		
                		
                queriesexpl[i]="Εμφάνιση των συμβολαίων με ενοίκιο μεγαλύτερο από αυτό που δηλώθηκε για το ακίνητο κατά την καταχώρηση στη βάση δεδομένων";
                queries[i++]=
                		"select addr_streetname as StreetName, addr_StreetNo as StreetNumber, contracts.rent as ContractRent, properties.rent as PropertyRent \n"+
                		"from contracts,properties			\n"+
                		"where contracts.PropertyRegistrationNo = properties.PropertyRegistrationNo \n"+
                		"having contracts.rent > properties.rent;	\n";

                
                
                queriesexpl[i]="Εμφάνιση του συνολικού αριθμού συμβολαίων και του συνολικού αριθμού καταχωρημένων ακινήτων";
                queries[i++]=
                		"select count(distinct(contracts.PropertyRegistrationNo)) as TotalContracts, count(distinct(properties.PropertyRegistrationNo)) as TotalProperties \n"+
                		"from contracts,properties; \n";
                		
                		
                queriesexpl[i]="Εμφάνιση του αριθμού των διαφημίσεων των οποίων το διαφημιζόμενο ακίνητο τελικά ενοικιάστηκε";
                queries[i++]=
                		"select count(*) as SuccessfulAdvertisments	\n"+
                		"from advertisments natural join	\n"+
                		"	(select contracts.propertyregistrationno	\n"+
                		"	from properties,contracts	\n"+
                		"	where contracts.PropertyRegistrationNo = properties.PropertyRegistrationNo) as temp;	\n";
                		
                queriesexpl[i]="Εμφάνιση των επισκέψεων που τελικά ο ίδιος πελάτης κατέληξε να αγοράσει το σπίτι";
       /*10*/   queries[i++]=
    		   			"select temp.FirstName, temp.LastName, temp.addr_Streetname as StreetName, temp.addr_StreetNo as StreetNumber, visits.date as Date		\n"+
    		   			"from visits,	\n"+
    		   			"(select contracts.clientregistrationno,contracts.propertyregistrationno, clients.FirstName, clients.LastName, properties.addr_Streetname, properties.addr_StreetNo	\n"+
    		   			"from contracts,properties,clients	\n"+
    		   			"where contracts.clientregistrationno = clients.clientregistrationno and contracts.propertyregistrationno = properties.propertyregistrationno) as temp	\n"+
    		   			"where visits.propertyregistrationno = temp.propertyregistrationno AND visits.clientregistrationno = temp.clientregistrationno;		\n";
    		   
                ///views
       
       			queriesexpl[i]="μη-ενημερώσιμο view: Εμφάνιση της ζήτησης για το κάθε είδος property";
                queries[i++]="select * from view1\n";
                  
                queriesexpl[i]="ενημερώσιμο view: Εμφάνιση των πελατών που είναι ενεργοί";
                queries[i]="select * from view2\n";
                
                
                
                
                
                
                
             //   mainPanel.add(queriesl); 
             //   mainPanel.add(viewsl);
             //   mainPanel.add(queriesl); 
              //  mainPanel.add(viewsl);
               // mainPanel.add(viewsl);
		for ( i=0;i<12;i++){
					//mainPanel.add(queriesl);
			       
                    choice[i] = new JButton();
                    choiceexpl[i] = new JButton(); 
                    if (i<10){
                    	choice[i].setText("Query "+(i+1));
                       choiceexpl[i].setText("Περιγραφή query ");
                    }
                    else{
                    	choice[i].setText("View "+(i-10+1));
                    	choiceexpl[i].setText("Περιγραφή view ");
                    	choice[i].setBackground(Color.lightGray);
                        choiceexpl[i].setBackground(Color.lightGray);
                    }
                    choice[i].addActionListener(new MyListener());
                    choiceexpl[i].addActionListener(new MyListener());
                    
                    
                    
            		mainPanel.add(choice[i]);
            	 	   
            		mainPanel.add(choiceexpl[i]);   
			        
                }
		
				
              
       
		picture = new JLabel();
        picture.setIcon(new ImageIcon("./photos_db/query.png"));
        //topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		//topPanel.add(msg1);
		topPanel.add(picture);
		        add(topPanel,BorderLayout.NORTH);  
                add(mainPanel,BorderLayout.CENTER);
		pack();
	}
        
	private class MyListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
    		int which=100;
    	
    		for (int i=0;i<12;i++)
    	    	if (e.getSource()==choiceexpl[i]){ 
    	    			which=i;
    				    JOptionPane.showMessageDialog(null,queriesexpl[which],"usage", JOptionPane.INFORMATION_MESSAGE);
    	    	}
    			
    		
    		if (which==100){
    			for (int i=0;i<12;i++)
    				if (e.getSource()==choice[i]) which=i;
                	try{
                		Class.forName ("com.mysql.jdbc.Driver");
                		Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/my_project?user=root&password="); 
                		Statement stmt = conn.createStatement();
                		System.out.println(queries[which]);
                		ResultSet out = stmt.executeQuery(queries[which]);
                		new display_result(out).setVisible(true);                    
                	}
                	catch (SQLException excpt) {
                		 System.out.println("SQLException :" +excpt);JOptionPane.showMessageDialog(null,"SQLException :" +excpt,"sql exception", JOptionPane.INFORMATION_MESSAGE);
                	}
                	catch (ClassNotFoundException exc){
                		System.out.println("Driver not found :" +exc.getMessage());
                	}
            	}
            }
            }
    
}    