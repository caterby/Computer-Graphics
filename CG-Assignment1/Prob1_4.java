/*@author - Jie Liu
 * Program to Write a program that draws a pattern of hexagons.
 * The vertices of a (regular) hexagon lie on its so-called circumscribed circle. 
 * */
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Prob1_4 extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;
    int maxX, maxY;
    static JFrame f ;
    float pixelWidth, pixelHeight,
        rWidth = 10.0F,
        rHeight = 7.5F,
        xP = -1, yP;
  
    FontMetrics metrics;

    public Prob1_4() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        addMouseListener(new MouseAdapter()
        { public void mousePressed(MouseEvent evt)
          { xP = evt.getX(); yP = evt.getY();

            f.repaint();
          }
        });
    }
    void initgr()
    { Dimension d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      pixelWidth = rWidth/maxX; pixelHeight = rHeight/maxY;
    }

int iX(float x){return Math.round(x/pixelWidth);}
int iY(float y){return maxY - Math.round(y/pixelHeight);}
float fx(int x){return x * pixelWidth;}
float fy(int y){return (maxY - y) * pixelHeight;}
public void paint(Graphics g)
{
	initgr();
	Graphics2D g2d = (Graphics2D) g;
	 
	Point origin = new Point(WIDTH / 2, HEIGHT / 2);
   
    metrics = g.getFontMetrics();

    double value1 = Math.pow(xP-origin.x,2);
    double value2 =Math.pow(yP- origin.y,2);
    int radius = (int)Math.sqrt(value1+value2);

     
    g.drawString(
   	       "Click anywhere on screen to generate hexagons of apprpriate radius! "
   	       , 40, 100);
    double size = (radius/253.0)*7.0;
    
    double rad = (radius/253.0)*30.0;
     
    drawHexGridLoop(g2d, origin, (int)size-2, (int)rad+5, 0);
     
	}
   
   //Method to generate the hexagonal loop
    private void drawHexGridLoop(Graphics g, Point origin, int size, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        
        
        int half = size / 2;
        

        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) {
                int xLbl = row < half ? col - row : col - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);


                drawHex(g, xLbl, yLbl, x, y, radius);
            }
        }
    }

    //Method to draw regular hexagon
    private void drawHex(Graphics g, int posX, int posY, int x, int y, int r) {
        Graphics2D g2d = (Graphics2D) g;

        hexagon hex = new hexagon(x, y, r);
      
        

        hex.draw(g2d, x, y, 0, 0x008844, false);
        hex.draw(g2d, x, y, 4, 000000, false);

      
    }

    public static void main(String[] args) {
         f = new JFrame();
        Prob1_4 p = new Prob1_4();

        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}