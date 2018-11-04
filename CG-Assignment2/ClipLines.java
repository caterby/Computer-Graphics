import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClipLines extends Frame {
    public static void main(String[] args) {
        new ClipLines();
    }

    ClipLines() {
        super("Click on two opposite corners of a rectangle");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(1000, 600);
        add("Center", new CvClipLine());
        setVisible(true);
    }
}

class CvClipLine extends Canvas {
    int xmin = 220, xmax = 420, ymin = 190, ymax = 290;
    float rWidth = 10.0F, rHeight = 7.5F,
            pixelSize;
    int maxX, maxY, centerX, centerY;

    void initgr() {
        Dimension d = getSize();
        maxX = d.width - 1;
        maxY = d.height - 1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
    }

    int clipCode(float x, float y) {
        return (x < xmin ? 8 : 0) | (x > xmax ? 4 : 0) |
                (y < ymin ? 2 : 0) | (y > ymax ? 1 : 0);
    }
    void drawDashedLine(Graphics2D g2d, int x1, int y1, int x2, int y2){
        //set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);
    }
    void clipLine(Graphics2D g, float xP, float yP, float xQ, float yQ,
                  float xmin, float ymin, float xmax, float ymax) {
        int cP = clipCode(xP, yP), cQ = clipCode(xQ, yQ);
        float dx, dy;
        float px = xP, py = yP, qx = xQ, qy = yQ;
        while ((cP | cQ) != 0) {
            if ((cP & cQ) != 0) return;
            dx = xQ - xP;
            dy = yQ - yP;
            if (cP != 0) {
                if ((cP & 8) == 8) {
                    yP += (xmin - xP) * dy / dx;
                    xP = xmin;
                } else if ((cP & 4) == 4) {
                    yP += (xmax - xP) * dy / dx;
                    xP = xmax;
                } else if ((cP & 2) == 2) {
                    xP += (ymin - yP) * dx / dy;
                    yP = ymin;
                } else if ((cP & 1) == 1) {
                    xP += (ymax - yP) * dx / dy;
                    yP = ymax;
                }
                cP = clipCode(xP, yP);
            } else if (cQ != 0) {
                if ((cQ & 8) == 8) {
                    yQ += (xmin - xQ) * dy / dx;
                    xQ = xmin;
                } else if ((cQ & 4) == 4) {
                    yQ += (xmax - xQ) * dy / dx;
                    xQ = xmax;
                } else if ((cQ & 2) == 2) {
                    xQ += (ymin - yQ) * dx / dy;
                    yQ = ymin;
                } else if ((cQ & 1) == 1) {
                    xQ += (ymax - yQ) * dx / dy;
                    yQ = ymax;
                }
                cQ = clipCode(xQ, yQ);
            }
        }

        g.drawLine((int) xP, (int) yP, (int) xQ, (int) yQ);

    }

    public void paint(Graphics g) {
        initgr();
        Graphics2D g2d = (Graphics2D) g;
        //Draw the rectangle
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(xmin, ymin, xmax, ymin);
        g2d.drawLine(xmax, ymin, xmax, ymax);
        g2d.drawLine(xmax, ymax, xmin, ymax);
        g2d.drawLine(xmin, ymax, xmin, ymin);

        //Draw all the lines in the file
        ArrayList<int[]> paras = new ArrayList<>();
        int num = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("Q2.txt"));
            num = Integer.valueOf(br.readLine());
            String line;
            while ((line = br.readLine()) != null) {
                int[] numbers = new int[4];
                String[] results = line.split(" ");
                for (int i = 0; i < results.length; i++) {
                    numbers[i] = Integer.valueOf(results[i]);
                }
                paras.add(numbers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < num; i++) {
            drawDashedLine(g2d, paras.get(i)[0], paras.get(i)[1], paras.get(i)[2], paras.get(i)[3]);
        }
        //Line Clipping
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < num; i++) {
            clipLine(g2d, paras.get(i)[0], paras.get(i)[1], paras.get(i)[2], paras.get(i)[3], xmin, ymin, xmax, ymax);
        }

    }
}
