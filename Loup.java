import  java.awt.Color;

                       

class Loup extends Bebete {
public static int[][] image={{1,0,0,0,0,0,0,0,0,1},
                             {1,1,0,0,0,0,0,0,1,1},
                             {1,1,1,1,0,0,1,1,1,1},
                             {1,1,1,1,1,1,1,1,1,1},
                             {1,1,0,1,1,1,1,0,1,1},
                             {1,1,0,0,1,1,0,0,1,1},
                             {1,1,1,0,1,1,0,1,1,1},
                             {0,1,1,1,1,1,1,1,1,0},
                             {0,0,1,1,1,1,1,1,0,0},
                             {0,0,0,0,1,1,0,0,0,0}};
  Loup (int x, int y,Labyrinthe plan,int ax ,int ay,Trappe Panneau[] ) {
    super(x,y,plan,ax,ay,Panneau);
   
  }

  Color getCouleur() {
    return Color.red;
  }
public int getImageValue(int x,int y) {
    return this.image[x][y];
  }
}
