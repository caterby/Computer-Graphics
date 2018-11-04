/*@author - Jie Liu
 * Program for a class Lines containing a static method dashedLine to draw dashed lines, in such a way that we can write
Lines.dashedLine(g, xA, yA, xB, yB, dashLength);
* */
import java.awt.BasicStroke;
import java.awt.Canvas;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Scanner;


public class Prob1_5 extends Frame{

	private static final long serialVersionUID = 1L;
	public static void main(String[] args){new Prob1_5();}
	Prob1_5()
	{
		super("Dashed lines generation");
	     addWindowListener(new WindowAdapter()
	       {public void windowClosing(WindowEvent e){System.exit(0);}});
	     setSize (800, 800);
	     add("Center", new Lines());
	    
	     setVisible(true);
	}
	

}
class Lines extends Canvas{

	private static final long serialVersionUID = 1L;
	int xA1 = 200;
	int yA1 = 100;
	int xB1 = 500;
	int yB1 = 400;
	int dashlength1 =40;
	Lines()
	{
		
	}
	public void paint(Graphics g){
	//Lines.dashedLine(g,200,100,600,500,40);
		Lines.dashedLine(g,xA1,yA1,xB1,yB1,dashlength1);
		Scanner sn = new Scanner(System.in);
		System.out.println("Enter values of xA,yA,xB,yB and dashedlength consecutively,using enter after each entry");
		xA1 = sn.nextInt();
		System.out.println("xa1"+xA1);
		yA1 = sn.nextInt();
		xB1 = sn.nextInt();
		yB1 = sn.nextInt();
		dashlength1 = sn.nextInt();
		sn.close();
		
		dashedLine(g,xA1,yA1,xB1,yB1,dashlength1);
	
	}
	
	private static Point2D getEndPoint(Point2D p, double length, int angle)
	{
	    
	    Point2D retVal = p; 
	    double x = p.getX() + length*Math.cos(Math.toRadians(angle));
	    double y = p.getY() +length*Math.sin(Math.toRadians(angle));
	    
	    retVal.setLocation(x,y);
	    return retVal;
	}
	
	private static void dashedLine(Graphics g, int xA, int yA, int xB, int yB,
			int dashedLength2) {
		
		g.drawRect(xA,yA,xB,yB);
	
	Point2D current = new Point(xA,yA);
	Point2D end = getEndPoint(current, ((dashedLength2*100.0)/20.0),45);
	Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {dashedLength2,(float) ((dashedLength2/20.0)*8.0)}, 0);
	Graphics2D g2d = (Graphics2D) g;
	int startx = xA;
	int starty =yA;
	int endX =(int)end.getX();
	int endY =(int)end.getY();
	Line2D line = new Line2D.Double(startx,starty,endX,endY);
    
	Point2D current2 = new Point(xA+xB,yA);
    Point2D end2 = getEndPoint(current2,(int) ((dashedLength2*100.0)/20.0),135);
    Line2D line2 = new Line2D.Double(xA+xB,yA,end2.getX(),end2.getY());
    
    
    Point2D current3 = new Point(xA,yA+yB);
    Point2D end3 = getEndPoint(current3,(int) ((dashedLength2*100.0)/20.0),-45);
    Line2D line3 = new Line2D.Double(xA,yA+yB,end3.getX(),end3.getY());
    
    Point2D current4 = new Point(xA+xB,yA+yB);
    Point2D end4 = getEndPoint(current4,(int) ((dashedLength2*100.0)/20.0),-135);
    Line2D line4 = new Line2D.Double(xA+xB,yA+yB,end4.getX(),end4.getY());
    
	g2d.setStroke(dotted);
	g2d.draw(line);
	g2d.draw(line2);
	g2d.draw(line3);
	g2d.draw(line4);
	g.drawRect(endX,endY,(int)end4.getX()-endX,(int)end4.getY()-endY);
	
	
		
	}
	
}