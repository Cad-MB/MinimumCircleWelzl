package GUI;

import java.awt.Color;
import java.awt.Point;

public class Circle {
   private Point center;
   private int radius;
   private Color c;

   protected Circle(Point center, int r, Color c) {
      this.center = center;
      this.radius = r;
      this.c = c;
   }

   public Circle(Point center, int r) {
      this.center = center;
      this.radius = r;
      this.c = Color.RED;
   }

   public Point getCenter() {
      return this.center;
   }

   public int getRadius() {
      return this.radius;
   }

   protected Color getColor() {
      return this.c;
   }

   protected void setColor(Color c) {
      this.c = c;
   }
}
