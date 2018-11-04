// CvDefPoly.java: To be used in other program files.
// Copied from Section 1.5 of
// Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
// Chichester: John Wiley.
// A class that enables the user to define
// a polygon by clicking the mouse.
// Uses: Point2D (discussed below).

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

class CvDefPoly extends Canvas {
    Vector<Point2D> v = new Vector<>();
    Point2D P;
    float x0, y0, rWidth = 10.0F, rHeight = 7.5F, pixelSize;
    boolean polyFinished = true, pAdded = true;
    int centerX, centerY;

    CvDefPoly() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                float xA = fx(e.getX() / 10 * 10 + 5), yA = fy(e.getY() / 10 * 10 + 5);
                if (P != null && xA == P.x && yA == P.y) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else if (getCursor() == Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR))
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (pAdded) {
                    P.x = fx(e.getX() / 10 * 10 + 5);
                    P.y = fy(e.getY() / 10 * 10 + 5);
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                if (getCursor() == Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)) return;
                float xA = fx(evt.getX() / 10 * 10 + 5), yA = fy(evt.getY() / 10 * 10 + 5);
                if (polyFinished) {
                    if (pAdded) {
                        v.removeAllElements();
                        x0 = xA;
                        y0 = yA;
                        polyFinished = false;
                        pAdded = false;
                    } else {
                        P = new Point2D(xA, yA);
                        pAdded = true;
                        repaint();
                        return;
                    }
                }
                float dx = xA - x0, dy = yA - y0;
                if (v.size() > 0 &&
                        dx * dx + dy * dy < 20 * pixelSize * pixelSize)
                    // Previously 4 
                    polyFinished = true;
                else
                    v.addElement(new Point2D(xA, yA));

                // 
                if (evt.getModifiers() == InputEvent.BUTTON3_MASK) {
                    polyFinished = true;
                }

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

    public void paint(Graphics g) {
        initgr();
        int left = iX(-rWidth / 2), right = iX(rWidth / 2),
                bottom = iY(-rHeight / 2), top = iY(rHeight / 2);
        g.drawRect(left, top, right - left, bottom - top);
        //draw squares
        g.setColor(new Color(220, 220, 220));
        for (int i = left; i < right; i += 10) g.drawLine(i, top, i, bottom);
        for (int i = top; i < bottom; i += 10) g.drawLine(left, i, right, i);
        g.setColor(Color.BLACK);

        int n = v.size();
        if (n == 0)
            return;
        Point2D a = v.elementAt(0);
        // Show tiny rectangle around first vertex:
        g.drawRect(iX(a.x) - 2, iY(a.y) - 2, 4, 4);
        for (int i = 1; i <= n; i++) {
            if (i == n && !polyFinished)
                break;
            Point2D b = v.elementAt(i % n);
            g.drawLine(iX(a.x), iY(a.y), iX(b.x), iY(b.y));
            g.drawRect(iX(b.x) - 2, iY(b.y) - 2, 4, 4); // Tiny rectangle; added
            a = b;
            g.drawString("" + (i % n), iX(b.x), iY(b.y));// to test.......
        }
        if (polyFinished && pAdded) {
            g.setColor(Color.RED);
            g.drawLine(iX(P.x), iY(P.y), right, iY(P.y));
            g.drawString("P", iX(P.x), iY(P.y));

            g.setColor(Color.GREEN);
            int num = numOfIntersection(P, v, g);
            g.drawString("Number of Intersection : " + num, right + 7, top + 20);
            g.drawString(num % 2 == 0 ? "P Outside the Polygon" : "P Inside the Polygon", right + 7, top + 40);
            g.drawString("Count " + (PointInPolygon.opt == 0 ? "Lower" : "Upper") + " End Points Only", right + 7, top + 60);
            g.drawString("Use Mouse to Drag P",right+7,bottom/2);
        }
    }

    int numOfIntersection(Point2D p, Vector<Point2D> pol, Graphics g) {
        int n = pol.size(), j = n - 1;
        int b = 0;
        float x = p.x, y = p.y;
        for (int i = 0; i < n; i++) {
            boolean condition = PointInPolygon.opt == 0 ?
                    (pol.get(j).y <= y && y < pol.get(i).y && Tools2D.area2(pol.get(j), pol.get(i), p) > 0 || pol.get(i).y <= y && y < pol.get(j).y && Tools2D.area2(pol.get(i), pol.get(j), p) > 0)
                    : (pol.get(j).y < y && y <= pol.get(i).y && Tools2D.area2(pol.get(j), pol.get(i), p) > 0 || pol.get(i).y < y && y <= pol.get(j).y && Tools2D.area2(pol.get(i), pol.get(j), p) > 0);

            if (condition) {
                b++;
                g.setColor(Color.BLUE);
                g.drawLine(iX(pol.get(i).x), iY(pol.get(i).y), iX(pol.get(j).x), iY(pol.get(j).y));
            }
            j = i;
        }
        return b;
    }

}
