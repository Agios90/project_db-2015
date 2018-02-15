import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;

public class ekkinhsh extends JFrame {
	private JButton choice[] = new JButton[7];
	private JLabel msg1 = new JLabel("<html>ΙΔΑΝΙΚΟ ΣΠΙΤΙ <br>Σύστημα βάσης δεδομένων</html>");
	
	JLabel picture;
	
	public ekkinhsh() {
                super("Central Screen");
                JPanel mainPanel = new JPanel();
                JPanel topPanel = new JPanel();
                picture = new JLabel();
                picture.setIcon(new ImageIcon("./photos_db/"+"idanikospiti"+".jpg"));
                setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
                for( int i=0;i<7;i++)
                    choice[i] = new JButton();
		choice[0].setText("Δείξε ολόκληρο ένα table");
		choice[1].setText("Δείξε ένα μέρος ενός table");
		choice[2].setText("Εισαγωγή μιας καινούργιας εγγραφής σε table");
		choice[3].setText("Διαγραφή μιας εγγραφής από ένα table");
		choice[4].setText("Μετατροπή (update) μιας εγγραφής από ένα table");
		choice[5].setText("Εκτέλεση ενός έτοιμου query");
		choice[6].setText("Για προχωρημένους χρήστες: γράψτε ένα query κατευθείαν σε SQL");
                

		MyListener myListener = new MyListener();
		for (int i=0;i<7;i++)
                  //choice[i].addActionListener(new MyListener());   
				choice[i].addActionListener(myListener); 
                topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
				topPanel.add(msg1);
				topPanel.add(picture);
                add(topPanel,BorderLayout.NORTH);
                mainPanel.setLayout(new GridLayout(7,1,3,3));
                for(int i=0;i<7;i++)
                    mainPanel.add(choice[i]);
                mainPanel.setVisible(true);
                add(mainPanel,BorderLayout.CENTER);
                pack();
        }
    
        public static void main(String args[]){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ekkinhsh().setVisible(true);
                }
            });
        }
    
    private class MyListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		int which=0;
    		for (int i=0;i<7;i++)
    		if (e.getSource()==choice[i]) which=i;
                    switch(which) {
                    case 0:new table_selection(0).setVisible(true);break;	//oloklhros pinakas
                    case 1:new table_selection(1).setVisible(true);break;	//meros pinaka
                    case 2:new table_selection(2).setVisible(true);break;	//eisagwgh
                    case 3:new table_selection(3).setVisible(true);break;	//diagrafh
                    case 4:new table_selection(4).setVisible(true);break;	//update                 
                    case 5:new execute_query().setVisible(true);break;	//etoimo query
                    case 6:new write_sql_query().setVisible(true);break;	//sql apeytheias query

                    }   			
    		}
    }
}