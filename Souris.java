import  java.awt.Color;

class Souris extends Bebete {
public static int[][] image={{0,1,1,1,0,0,1,1,1,0},
                             {1,1,1,1,0,0,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,0,0,1,1,0,0,1,1},
                             {1,1,0,0,1,1,0,0,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {0,1,1,1,1,1,1,1,1,0},
                             {0,1,1,1,1,1,1,1,1,0},
                             {0,0,0,1,1,1,1,0,0,0}};
Fromage plateauDeFromages[];

  Souris (int x, int y,Labyrinthe plan,int ax ,int ay ,Trappe Panneau[],Fromage plateauDeFromages[]) {
    
    super(x,y,plan,ax,ay,Panneau);
    this.plateauDeFromages=plateauDeFromages;
  }

  Color getCouleur() {
    return Color.gray;
  }

  void BougeEtChouffe(int nx,int ny) { 
    BougeDeLa(nx,ny);
    asTonUnFromage();
  }

  void asTonUnFromage() {
    for (int i=0;(i<plateauDeFromages.length);i++) {
      if ((plateauDeFromages[i].getX()==getX()) && (plateauDeFromages[i].getY()==getY()))
        { 
          plateauDeFromages[i].grignoter();
          break;
        }
    }
  }
public int getImageValue(int x,int y) {
    return this.image[x][y];
  }
}
