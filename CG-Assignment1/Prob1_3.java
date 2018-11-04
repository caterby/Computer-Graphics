/*@author - Jie Liu
 * Program to draw a set of concentric pairs of squares,
 *  each consisting of a square with horizontal and vertical edges and one rotated through 45бу.
 * 
 * */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Prob1_3 {
	 public static void main(String[] args) {
	        JFrame frame = new JFrame();
	        frame.setSize(400, 400);
	        frame.setVisible(true);

	        frame.add(new JPanel() {

	      
				private static final long serialVersionUID = 1L;

				@Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D g2d = (Graphics2D) g;

	                double alpha = Math.toRadians(45);
	                double factor = 1 / (Math.sin(alpha) + Math.cos(alpha));
	                double size = 200;

	                g2d.translate(size, size);

	                for (int i = 0; i < 20; i++) {
	                    g2d.setColor(i % 2 == 0 ? Color.black : Color.black);

	                    int intSize = (int) Math.round(size);

	                    g2d.drawRect(-intSize / 2, -intSize / 2, intSize, intSize);

	                    size = size * factor;

	                    g2d.rotate(alpha);
	                }
	            }
	        });
	    }

}