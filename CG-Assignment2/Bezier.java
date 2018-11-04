/**
 * After the first segment has been drawn, the second one is based on these points Q0, Q1, Q2 and Q3
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Bezier extends Frame {
    public static void main(String[] args) {
        new Bezier();
    }

    Bezier() {
        super("Define endpoints and control points of curve segment");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(1280, 720);
        add("Center", new CvBezier());
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
    }
}

class CvBezier extends Canvas {
    ArrayList<Point2D> points = new ArrayList<>();
    int centerX, centerY;
    float rWidth = 10.0F, rHeight = 7.5F, pixelSize;

    CvBezier() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                float x = fx(evt.getX()), y = fy(evt.getY());
                Point2D np;
                if (points.size() > 1 && (points.size() - 1) % 3 == 0) {
                    Point2D last = points.get(points.size() - 1);
                    if (x == last.x && y == last.y) {
                        Point2D last2 = points.get(points.size() - 2);
                        np = new Point2D(2 * x - last2.x, 2 * y - last2.y);
                    } else {
                        points.clear();
                        np = new Point2D(x, y);
                    }
                } else {
                    np = new Point2D(x, y);
                }
                points.add(np);
                repaint();
            }
        });
    }

    void initgr() {
        Dimension d = getSize();
        int maxX = d.width - 1, maxY = d.height - 1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
    }

    int iX(float x) {
        return Math.round(centerX + x / pixelSize);
    }

    int iY(float y) {
        return Math.round(centerY - y / pixelSize);
    }

    float fx(int x) {
        return (x - centerX) * pixelSize;
    }

    float fy(int y) {
        return (centerY - y) * pixelSize;
    }

    Point2D middle(Point2D a, Point2D b) {
        return new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    void bezier(Graphics g, Point2D p0, Point2D p1, Point2D p2,
                Point2D p3) {
        int x0 = iX(p0.x), y0 = iY(p0.y),
                x3 = iX(p3.x), y3 = iY(p3.y);
        if (Math.abs(x0 - x3) <= 1 && Math.abs(y0 - y3) <= 1)
            g.drawLine(x0, y0, x3, y3);
        else {
            Point2D
                    a = middle(p0, p1), b = middle(p3, p2), c = middle(p1, p2),
                    a1 = middle(a, c), b1 = middle(b, c), c1 = middle(a1, b1);
            bezier(g, p0, a, a1, c1);
            bezier(g, c1, b1, b, p3);
        }
    }

    public void paint(Graphics g) {
        initgr();
        int numOfPoints = points.size();
        int left = iX(-rWidth / 2), right = iX(rWidth / 2),
                bottom = iY(-rHeight / 2), top = iY(rHeight / 2);
        g.drawRect(left, top, right - left, bottom - top);

        for (int i = 0; i < numOfPoints; i++) {
            // Show tiny rectangle around point:
            g.drawRect(iX(points.get(i).x) - 2, iY(points.get(i).y) - 2, 4, 4);
            if (i > 0)
                // Draw line points[i-1]points[i]:
                g.drawLine(iX(points.get(i - 1).x), iY(points.get(i - 1).y),
                        iX(points.get(i).x), iY(points.get(i).y));
        }

        int sg = (numOfPoints - 1) / 3;
        for (int i = 0; i < sg; i++) {
            bezier(g, points.get(3 * i), points.get(3 * i + 1), points.get(3 * i + 2), points.get(3 * i + 3));
        }
    }
}

class Point2D {
    float x, y;

    Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
