public class Trappe {

   // les 2 points mis en correspondance

  private int x1,y1,x2,y2;

  public Trappe(int x1,int y1,int x2,int y2) { 

    this.x1=x1;
    this.y1=y1;
    this.x2=x2;
    this.y2=y2;

  }

  public int teleporteX(int x,int y) {
    
    if ((x==x1) && (y==y1))
      return x2;
    else
      return x;
  }

  public int teleporteY(int x,int y) {
    
    if ((x==x1) && (y==y1))
      return y2;
    else
      return y;
  }

}
