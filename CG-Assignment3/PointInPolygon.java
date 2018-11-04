/****
 * Author: Jie Liu
 * Created on July 18, 2017
 * This file includes the main function
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PointInPolygon extends Frame {
    static int opt;

    public static void main(String[] args) {
        new PointInPolygon();
    }

    PointInPolygon() {
        super("Point-in-polygon test algorithm");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(1280, 720);
        add("Center", new CvDefPoly());
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
        Object[] options = {"Lower End",
                "Upper End"};
        opt = JOptionPane.showOptionDialog(this,
                "Count lower end points only or upper end points only?",
                "Choose Condition",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     
                options,  
                options[0]); 
    }
}
