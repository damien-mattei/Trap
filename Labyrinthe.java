import java.awt.Color;
import java.io.*;
import java.util.Stack;
//import Cell;

public class Labyrinthe {
  // taille des sprites 
  private char px=10; 
  private char py=10;
  // dimension du labyrinthe 
  private int dx=40;
  private int dy=24;
  private int lab[][]=new int[dx+1][dy+1];
  private boolean inv;
  private boolean ar=false;

  public Labyrinthe(String nom,boolean inv,boolean gen) {
    
    this.lab=new int[dx+1][dy+1];
    if (!gen)
      try {
        chargeLabyrinthe(nom);
      } catch (FileNotFoundException e)
        {
          error("I/O Exception: " + e);
        }
    else
      genereParDFS();
    this.dumpText();
    this.inv=inv;
  }

  public int getPX() {
    return this.px;
  }

  public int getPY() {
    return this.py;
  }

  public int getDX() {
    return this.dx;
  }

  public int getDY() {
    return this.dy;
  }
  
  public void genereParDFS() { // Depth First Search algorithm 
    /* http://www.mazeworks.com/mazetut/howto.htm */

  Stack lc= new Stack();
  int tc=((int)((dx*dy)/4)); // nombre total de cellules
  int cx,cy;
  do
    {
      cx=(int) Math.floor(Math.random()*dx);
      cy=(int) Math.floor(Math.random()*dy);
    }
  while (((cx % 2)==0) || ((cy % 2)==0));
  Cell cc=new Cell(cx,cy); // on en choisit une au hazard comme point de depart
  int vc=1; // nombre de cellules visitees
  
  // construction de la grille
  for(int j=0;(j<=dy);j+=2)
    for(int i=0;(i<=dx);i++)
      this.lab[i][j]='1';
  for(int i=0;(i<=dx);i+=2)
    for(int j=0;(j<=dy);j++)
      this.lab[i][j]='1';
  this.dumpText();
  
  while (vc<tc) {
    int ni=0;
    Cell ci;
    cx=cc.getX();
    cy=cc.getY();
    System.out.println(cx+" c "+cy);
    if ((cx<=(dx-3)) && (murIntacts(ci=new Cell(cx+2,cy))))
      ni++;
    if ((cx>=3) && (murIntacts(ci=new Cell(cx-2,cy))))
      ni++;
    if ((cy<=(dy-3)) && (murIntacts(ci=new Cell(cx,cy+2))))
      ni++;
    if ((cy>=3) && (murIntacts(ci=new Cell(cx,cy-2))))
      ni++;
    if (ni>0)
      {
        int nx,ny;
        int mx=0; // pour pas que javac rale que c'est peut etre pas initialise
        int my=0;
        do
          {
            int hz=(int) Math.floor(Math.random()*4); // exploration aleatoire
            switch (hz) {
            case 0:
              nx=cx+2;ny=cy;
              mx=cx+1;my=cy;
              break;
            case 1:
              nx=cx;ny=cy+2;
              mx=cx;my=cy+1;
              break;
            case 2:
              nx=cx-2;ny=cy;
              mx=cx-1;my=cy;
              break;
            default:
              nx=cx;ny=cy-2;
              mx=cx;my=cy-1;
              break;
            }
          } while ((nx<0) || (ny<0) || (nx>dx) || (ny>dy) || (!(murIntacts(ci=new Cell(nx,ny)))));
         System.out.println(mx+" m "+my);
        this.setVal(mx,my,'0');
        lc.push(cc);
        cc=ci;
        vc++;
      }
    else
     cc=(Cell) lc.pop();    
  }
  
  } // finalement cet algo marche meme si les murs sont aussi epais que le passage... 
  
  public boolean murIntacts(Cell c) {
    int x=c.getX();
    int y=c.getY();
    System.out.println(x+" x y "+y);
    if ((this.lab[x+1][y]=='1') && (this.lab[x-1][y]=='1') && (this.lab[x][y+1]=='1') && (this.lab[x][y-1]=='1'))
      {System.out.println("ok");
      return true;
      }
    else {System.out.println("ok");return false;}
  }
  
  public void dumpText() {
    for(int j=0;(j<=dy);j++)
      {
        for(int i=0;(i<=dx);i++)
          if (this.lab[i][j]=='1')
            System.out.print("%");
          else System.out.print(" ");
        System.out.print("\n");
      }
  }
    
 // on charge le labyrinthe a partir d'un fichier
public void chargeLabyrinthe(String niveau) throws FileNotFoundException
 
    {
      InputStream in=new FileInputStream(niveau);
      int ch;
      int x=0;
      int y=0;
      
      try {
        while ((ch=in.read())!=-1) {
          if ((ch=='0') || (ch=='1'))
            {
              this.lab[x][y]=ch;
              if (x==this.dx)
                    {
                      x=0;
                      y++;
                    }
              else x++;
            }
        }
          }
      catch (IOException e) {
        error("I/O Exception: " + e);
      }
    }

  public Color getCouleur(int x,int y) {
   
    switch (this.lab[x][y]) {
    case '0' : // plancher
      if (this.inv)
        return Color.green;
      else
        return Color.blue;
    case '1' : // mur
      return Color.green;
    default : // plancher parcouru deja ou faux passage
      return Color.blue;
    }
  }

  public int getVal(int x,int y) {
    if ((this.lab[x][y]=='1') || (this.lab[x][y]=='2'))
      return '1';
    else 
      return '0';
  }

  public synchronized void setVal(int x,int y,int val) {
    this.lab[x][y]=val;
    if (!(this.inv) && (val=='3')) return;
    setAskedRepaint();
  }
  
  private static void error(String err) {
    System.err.print("Translate: " + err);
    System.exit(1); // indique un probleme
  }
  public boolean isAskedRepaint() {
    return this.ar;
  }
  public void setAskedRepaint() {
    this.ar=true;
  }
  public void unSetAskedRepaint() {
    this.ar=false;
  }
    
    
}
