package GUI;

import algorithms.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keymaps implements KeyListener {
   private RootPanel rootPanel;

   public Keymaps(RootPanel rootPanel) {
      this.rootPanel = rootPanel;
   }

   public void keyPressed(KeyEvent arg0) {
   }

   public void keyReleased(KeyEvent arg0) {
   }

   public void keyTyped(KeyEvent event) {
      Line l;
      Circle c;
      long t;
      switch(event.getKeyChar()) {
      case 'c':
         t = System.currentTimeMillis();
         c = (new DefaultTeam()).calculCercleMinNaïf(this.rootPanel.getPoints());
         t = System.currentTimeMillis() - t;
         c.setColor(Color.RED);
         this.rootPanel.addCircleAndT(c, t);
         break;
      case 'd':
         t = System.currentTimeMillis();
         l = (new DefaultTeam()).calculDiametre(this.rootPanel.getPoints());
         t = System.currentTimeMillis() - t;
         l.setColor(Color.YELLOW);
         this.rootPanel.addLineAndT(l, t);
         break;
      case 'f':
         try {
            t = System.currentTimeMillis();
            l = (new DefaultTeam()).calculDiametre(this.rootPanel.getPoints());
            t = System.currentTimeMillis() - t;
            l.setColor(Color.GREEN);
            this.rootPanel.addLineAndT(l, t);
            break;
         } catch (Exception var9) {
         }
      case 'e':
         try {
            t = System.currentTimeMillis();
            c = (new DefaultTeam()).calculCercleMinNaïf(this.rootPanel.getPoints());
            t = System.currentTimeMillis() - t;
            c.setColor(Color.GREEN);
            this.rootPanel.addCircleAndT(c, t);
            break;
         } catch (Exception var8) {
         }
      case 'r':
         try {
            RandomPointsGenerator.main((String[])null);
            DiamRace.readFile();
            this.rootPanel.refreshLine();
         } catch (Exception var7) {
         }
      case 'g':
         t = System.currentTimeMillis();
         c = (new DefaultTeam()).calculCercleMinWlzl(this.rootPanel.getPoints());
         t = System.currentTimeMillis() - t;
         c.setColor(Color.GREEN);
         this.rootPanel.addCircleAndT(c, t);
         break;
      case 'i':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      default:
         break;
      case 'h':
         this.rootPanel.shiftLeftAll();
         break;
      case 'j':
         this.rootPanel.shiftUpAll();
         break;
      case 'k':
         this.rootPanel.shiftDownAll();
         break;
      case 'l':
         this.rootPanel.shiftRightAll();
      }

   }
}
