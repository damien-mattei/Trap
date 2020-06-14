import  java.awt.Color;
import java.io.*;
import java.awt.Event;

class Joueur extends Bebete { // d'apres Darwin

private int dx,dy;
private boolean dig=false;
private int ldx,ldy;
private int number;
Fromage plateauDeFromages[];
int score=0;
public static int[][] image={{0,0,0,1,1,1,1,0,0,0},
                             {0,1,1,1,1,1,1,1,1,0},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {0,1,1,1,1,1,1,1,1,0},
                             {0,0,0,1,1,1,1,0,0,0},
                             {0,0,1,1,0,0,1,1,0,0},
                             {0,1,1,0,0,0,0,1,1,0}};

  Joueur(int x, int y,Labyrinthe plan,int ax ,int ay,Trappe Panneau[],Fromage plateauDeFromages[],int number) {
    super(x,y,plan,ax,ay,Panneau);
    this.plateauDeFromages=plateauDeFromages;
    this.number=number;
  }

  public void run() {
    
    int tx,ty;
    int nx,ny;
    float ftx,fty;
    int d[];

    while(true) {
      if (isGm()) {
        float fp=0;
        if (this.step<10)
          {
            this.step+=ic;
            fp=(float)this.step/(float)10;
            ftx=(float)this.ax+((float)(this.x-this.ax))*fp;
            fty=(float)this.ay+((float)(this.y-this.ay))*fp;
            setG(ftx,fty);
          }
        else
          {
            unSetGm();
            this.step=0;
          }
      }
      else
        {
          if (isDig())
            {
              tx=this.x;ty=this.y;
              d=getLastDirection();
              d=getDirection();
              tx+=d[0];
              ty+=d[1];
              if (plan.getVal(tx,ty)=='1')
                plan.setVal(tx,ty,'2');
            }
          else
            {
              tx=this.x;ty=this.y;
              for (int i=0;i<carteDesTrappes.length;i++) {
                tx=carteDesTrappes[i].teleporteX(this.x,this.y);
                ty=carteDesTrappes[i].teleporteY(this.x,this.y);
                if ((tx!=this.x) || (ty!=this.y)) break;
              } 
              if ((tx!=this.x) || (ty!=this.y)) 
                {
                  BougeEtChouffe(tx,ty);
                  setG(tx,ty); // a cause des trappes
                }
              else
                {
                  nx=this.x;
                  ny=this.y;
                  d=getDirection();
                  nx+=d[0];
                  ny+=d[1];
                  
                  if ((plan.getVal(nx,ny)=='0') && ((nx!=this.x) || (ny!=this.y)))
                    {
                      BougeEtChouffe(nx,ny);
                      setGm();
                    }
                }
            }
        }
          // On "dort"
         try {
            Thread.currentThread().sleep(20);
          } catch(InterruptedException e) {
            System.out.println("Erreur dans le sleep");
            e.printStackTrace(); 
            }
        }
    }
    
  public int[] getDirection() {
    int rv[]={0,0};
    rv[0]=getDirectionX();
    rv[1]=getDirectionY();
    //  setDirection(0,0);
    return rv;
  }
  public int[] getLastDirection() {
    int rv[]={0,0};
    rv[0]=this.ldx;
    rv[1]=this.ldy;
    return rv;
  }
  public synchronized  void setLastDirection() {
    this.ldx=getDirectionX();
    this.ldy=getDirectionY();
    return;
  }

  public synchronized  void setDirection(int x,int y) {
    this.dx=x;
    this.dy=y;
    return;
  }

  public int getDirectionX() {
    return this.dx;
  }

  public int getDirectionY() {
    return this.dy;
  }

  public Color getCouleur() {
    if (number==1)
      return Color.pink;
    else return Color.cyan;
  }
  
  public void setDig() {
    this.dig=true;
  }
  
  public void unSetDig() {
    this.dig=false;
  }
  
  public boolean isDig() {
    return this.dig;
  }
  
  void asTonUnFromage() { 
    // cette fonction differe un peu de celle pour la Souris....
    for (int i=0;(i<plateauDeFromages.length);i++) {
      if ((plateauDeFromages[i].getX()==getX()) && (plateauDeFromages[i].getY()==getY()))
        {    
          this.score+=plateauDeFromages[i].getPoids();
          while (plateauDeFromages[i].getPoids()!=0) {
            plateauDeFromages[i].grignoter();
          }
          break;
        }
    }
  }
  void BougeEtChouffe(int nx,int ny) { 
    BougeDeLa(nx,ny);
    asTonUnFromage();
    plan.setVal(nx,ny,'3'); // on le paint !
  }
  public int getImageValue(int x,int y) {
    return this.image[x][y];
  }
}





