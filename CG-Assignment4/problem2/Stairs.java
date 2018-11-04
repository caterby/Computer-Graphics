/**
 * Author: Jie Liu
 * Date: August 2st 2017
 */

import java.io.FileWriter;
import java.io.IOException;

public class Stairs {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println(
                    "Please input n (> 0), alpha (in degrees)\n" +
                            "and a filename as program arguments.\n");
            System.exit(1);
        }
        int n = 0;
        double a = 6.0, alphaDeg = 0;
        try {
            n = Integer.valueOf(args[0]).intValue();
            //a = Double.valueOf(args[1]).doubleValue();
            alphaDeg = Double.valueOf(args[1]).doubleValue();
            if (n <= 0 || a < 0.5) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("n must be an integer > 0");
            // System.out.println("a must be a real number >= 0.5");
            System.out.println("alpha must be a real number");
            System.exit(1);
        }
        new StairsGen(n, a, alphaDeg * Math.PI / 180, args[2]);
    }
}

//Point3D.java: Representation of a point in 3D space.
class Point3D {
    float x, y, z;

    Point3D(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }
}

//Actual class containing the methods required for stairs generation.
class StairsGen {
    FileWriter fwtr;
    int n2 = 2 * 20, n3 = 3 * 20, n4 = 4 * 20, m, temp = 0;
    float cache = 0;

    StairsGen(int n, double a, double alpha, String fileName)
            throws IOException {
        fwtr = new FileWriter(fileName);
        Point3D[] P = new Point3D[9];
        double b = a - 2.8;
        P[1] = new Point3D(a, -a, 0);
        P[2] = new Point3D(a, a, 0);
        P[3] = new Point3D(b, a, 0);
        P[4] = new Point3D(b, -a, 0);
        P[5] = new Point3D(a, -a, 0.2);
        P[6] = new Point3D(a, a, 0.2);
        P[7] = new Point3D(b, a, 0.2);
        P[8] = new Point3D(b, -a, 0.2);

        //generate vertices for the Beam
        for (int k = 0; k < n; k++) {
            double phi = k * alpha,
                    cosPhi = Math.cos(phi), sinPhi = Math.sin(phi), x, y;
            float x1 = 0, y1 = 0, z1 = 0, newx = 0, newy = 0, newz = 0;

            for (int i = 1; i <= 8; i++) {
                x = P[i].x;
                y = P[i].y;

                x1 = (float) (x * cosPhi - y * sinPhi);
                y1 = (float) (x * sinPhi + y * cosPhi);
                z1 = (float) ((P[i].z + k));
                if (i == 1 || i == 2) {
                    newx = x1 + newx;
                    newy = y1 + newy;
                    newz = (z1 + newz);
                }
                temp++;
                fwtr.write((temp) + " " + x1 + " " + y1 + " " + z1 +
                        "\r\n");
            }
            //generate vertices for the railing
            temp++;
            fwtr.write((temp) + " " + newx / 2 + " " + newy / 2 + " " + newz / 2 +
                    "\r\n");
            temp++;
            if (temp == 10) {
                cache = (newx / 2);
                cache += 2;
                fwtr.write((temp) + " " + newx / 2 + " " + newy / 2 + " " + (cache) +
                        "\r\n");
            } else {
                cache += 1;
                fwtr.write((temp) + " " + newx / 2 + " " + newy / 2 + " " + (cache) +
                        "\r\n");
            }
        }
        temp++;
        //generate vertices for cylinder
        genCylinder(n, Float.valueOf((float) (0.2 / 2)), 0);
        fwtr.write("Faces:\r\n");
        //generate faces for cylinder
        drawFaces(n, n2, n3, n4, 0, Float.valueOf((float) (0.2 / 2)));

        //generate faces for the beam
        for (int k = 0; k < n; k++) {  // Beam k again:
            int m = 10 * k;
            face(m, 1, 2, 6, 5);
            face(m, 4, 8, 7, 3);
            face(m, 5, 6, 7, 8);
            face(m, 1, 4, 3, 2);
            face(m, 2, 3, 7, 6);
            face(m, 1, 5, 8, 4);
        }
        //generate faces for railing
        int railx = 9, raily = 10;
        fwtr.write(railx + " " +
                +(raily) + ".\r\n");
        for (int f = 1; f < n; f++) {
            railx++;
            raily += 10;
            fwtr.write(railx + " " +
                    +(raily) + ".\r\n");
            railx += 9;
            fwtr.write(railx + " " +
                    +(raily) + ".\r\n");
        }
        fwtr.close();
    }

    void face(int m, int a, int b, int c, int d) throws IOException {
        a += m;
        b += m;
        c += m;
        d += m;
        fwtr.write(a + " " + b + " " + c + " " + d + ".\r\n");
    }

    //Method to generate vertices for cylinder
    void genCylinder(int n, float rOuter, float rInner)
            throws IOException {
        double delta = 2 * Math.PI / n;
        double heightpm = (n / 25) * 15;
        for (int i = 1; i <= n; i++) {
            double alpha = i * delta,
                    cosa = Math.cos(alpha), sina = Math.sin(alpha);

            double r = rOuter;
            if (r > 0)
                for (int bottom = 0; bottom < 2; bottom++) {
                    int k = (2 * 0 + bottom) * n + i;
                    // Vertex numbers for i = 1:
                    // Top:      1 (outer) 2n+1 (inner)
                    // Bottom: n+1 (outer) 3n+1 (inner)
                    wi(k + temp); // w = write, i = int, r = real
                    wr(r * cosa * 10);
                    wr(r * sina * 10); // x and y
                    if ((k + temp > temp + 25) && (k + temp < temp + 51))
                        wi(0);
                    else
                        wi((int) ((2 - bottom) * heightpm)); // bottom: z = 0; top: z = 1
                    fwtr.write("\r\n");
                }
        }
    }

    //Method to generate faces for cylinder
    private void drawFaces(int n, int n2, int n3, int n4, float rInner, float rOuter) {
        try {
            for (int i = 1; i <= n; i++)
                wi(i + temp);
            fwtr.write(".\r\n");
            // Bottom boundary face:
            for (int i = n2; i >= n + 1; i--) wi(i + temp);
            fwtr.write(".\r\n");
            // Vertical, rectangular faces:
            for (int i = 1; i <= n; i++) {
                int j = i % n + 1;
                // Outer rectangle:
                wi(j + temp);
                wi(i + temp);
                wi(i + n + temp);
                wi(j + n + temp);
                fwtr.write(".\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void wi(int x)
            throws IOException {
        fwtr.write(" " + String.valueOf(x));
    }

    void wr(double x)
            throws IOException {
        if (Math.abs(x) < 1e-9) x = 0;
        fwtr.write(" " + String.valueOf((float) x));
    }
}


