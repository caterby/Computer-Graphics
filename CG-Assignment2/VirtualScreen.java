import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VirtualScreen extends Frame {
    public static void main(String[] args) {
        new VirtualScreen();
    }

    VirtualScreen() {
        super("VirtualScreen");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(1280, 720);
        add("Center", new myCanvas());
        setVisible(true);
    }
}

class myCanvas extends Canvas{
    private int maxX,maxY;

    public void paint(Graphics g) {
        Dimension d = getSize();
        maxX = d.width - 1;
        maxY = d.height - 1;
        drawDotCoordinate(g,Color.gray);
        //Read parameters
        ArrayList<int[]> paras = new ArrayList<>();
        int num =0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("Q1.txt"));
            num = Integer.valueOf(br.readLine());
            String line;
            while ((line=br.readLine())!=null){
                int[] numbers = new int[4];
                String[] results = line.split(" ");
                for(int i=0;i<results.length;i++){
                    numbers[i]=Integer.valueOf(results[i]);
                }
                paras.add(numbers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0;i<num;i++){

            Bresenham.drawLine(g, paras.get(i)[0], paras.get(i)[1], paras.get(i)[2], paras.get(i)[3]);
        }
        Bresenham.drawCircle(g, paras.get(num)[0], paras.get(num)[1], paras.get(num)[2]);
    }

    private void drawDotCoordinate(Graphics g, Color color){
        g.setColor(color);
        int interval = 20;
        for(int i = interval; i<maxX; i+= interval){
            for(int j = interval; j<maxY; j+= interval){
                int diameter = 5;
                g.fillOval(i,j, diameter, diameter);
            }
        }
    }

}

class Bresenham{
    static void drawLine(Graphics g, int xP, int yP, int xQ, int yQ) {
        int x = xP, y = yP, D = 0, HX = xQ - xP, HY = yQ - yP, c, M,
                xInc = 1, yInc = 1;
        if (HX < 0) {xInc = -1; HX = -HX;}
        if (HY < 0) {yInc = -1; HY = -HY;}
        int gray =255 - (int)(Math.abs(Math.abs((float) (yP-yQ)/(float)(xP-xQ))-1)*128);
        Color color = new Color(gray,gray,gray);
        if (HY <= HX) {
            c = 2 * HX; M = 2 * HY;
            for (;;) {
                putPixel(g, x, y,color);
                if (x == xQ) break;
                x += xInc;
                D += M;
                if (D > HX) {y += yInc; D -= c;}
            }
        } else {
            c = 2 * HY; M = 2 * HX;
            for (;;) {
                putPixel(g, x, y,color);
                if (y == yQ) break;
                y += yInc;
                D += M;
                if (D > HY) {x += xInc; D -= c;}
            }
        }
    }

    static void drawCircle(Graphics g, int xC, int yC, int r) {
        int x = 0, y = r, u = 1, v = 2 * r - 1, E = 0;
        Color color = new Color(0,0,0);
        while (x < y) {
            putPixel(g, xC + x, yC + y,color); // NNE
            putPixel(g, xC + y, yC - x,color); // ESE
            putPixel(g, xC - x, yC - y,color); // SSW
            putPixel(g, xC - y, yC + x,color); // WNW
            x++; E += u; u += 2;
            if (v < 2 * E) {y--; E -= v; v -= 2;}
            if (x > y) break;
            putPixel(g, xC + y, yC + x,color); // ENE
            putPixel(g, xC + x, yC - y,color); // SSE
            putPixel(g, xC - y, yC - x,color); // WSW
            putPixel(g, xC - x, yC + y,color); // NNW
        }
    }


    static void putPixel(Graphics g, int x, int y,Color color){
        g.setColor(color);
        g.drawOval(x*20-8,y*20-8,20,20);
    }
}
