// Tools2D.java: Class to be used in other program files.
// Uses: Point2D (Section 1.5).

class Tools2D {
   static float area2(Point2D a, Point2D b, Point2D c) {
      return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
   }
   
   static float distance2(Point2D p, Point2D q) {
      float dx = p.x - q.x, dy = p.y - q.y;
      return dx * dx + dy * dy;
   }

   static boolean insideTriangle(Point2D a, Point2D b, Point2D c,
         Point2D p){ 
      return area2(a, b, p) >= 0 && 
             area2(b, c, p) >= 0 && 
             area2(c, a, p) >= 0;
   }
   
   static Point2D circumcenter(Point2D a, Point2D b, Point2D c) {
      float u1 = b.x - a.x, u2 = b.y - a.y, 
            v1 = -u2, v2 = u1, 
            w1 = c.x - b.x, w2 = c.y - b.y, 
            t1 = -w2, t2 = w1;
     
      float lambda = (t2 * (b.x - a.x) - t1 * (b.y - a.y) + 0.5F * t2 * (w1 - u1) - 0.5F * t1 * (w2 - u2)) / (t2 * v1 - t1 * v2);
      // Substituting this in the left-hand sides of the above two equations
      return new Point2D(a.x + 0.5F * u1 + v1 * lambda,
                         a.y + 0.5F * u2 + v2 * lambda);   
   }  

}

