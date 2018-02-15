import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.event.*;

public class display_result extends JFrame {

        public display_result(ResultSet set) {
            super("RESULT");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            
            add(new result_table(set),BorderLayout.CENTER);            
            pack();           
        }
}



