import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
// import Labyrinthe;
// import Bebete;
// import Souris;
// import Loup;
// import Fromage;
// import Trappe;
// import Joueur;


    
public class Trap extends Applet implements Runnable,ActionListener {
  Thread threadDeApplet;
  Vector listeSouris = new Vector(); 
  
  int largeur, hauteur;
  boolean fc=true ;
  static boolean dnd=false; // do not disturb (the players)
  public static int dx;
  public static int dy;
  public static int px;
  public static int py;
  public static Labyrinthe carte;
  static boolean accel=false; // au debut on dessine tout donc pas d'acceleration
  Loup ahouuu;
  public static Fromage Reblochon[] = new Fromage[5];
  public static Trappe Panneau[] = new Trappe[2];
  static boolean inv=false; // labyrinthe invisible?
  Joueur player1,player2;
  public final static int sx=1;
  public final static int sy=22;
  static boolean gen=false; // generation  du laby automatique
    
    public Trap() {
      
       
  // Classe anonyme interne pour gerer l'evenement de la frappe d'une touche
        KeyListener keyCallback = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

              int t=e.getKeyCode();
	      //System.out.println( "key code = "+t); 
              switch (t) {
              case 38:
                player1.setDirection(0,-1);
                break;
              case 40:
                player1.setDirection(0,1);
                break; 
              case 37:
                player1.setDirection(-1,0);
                break;
              case 39:
                player1.setDirection(1,0);
                break;
              case 127:
                player1.setDig();
                break;
              case 90:
                player2.setDirection(0,-1);
                break;
              case 83:
                player2.setDirection(0,1);
                break; 
              case 81:
                player2.setDirection(-1,0);
                break;
              case 68:
                player2.setDirection(1,0);
                break;
              case 65:
                player2.setDig();
                break;
              default:
                break;
              }
              //  System.out.println("touche enfoncee !" + e.getKeyCode());

            }
            
        }; // Fin de la classe anonyme

     // Classe anonyme interne pour gerer l'evenement du relachement d'une touche
        KeyListener keyCallback2 = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

              int t=e.getKeyCode();
              switch (t) {
              case 37:
                player1.setLastDirection();
                player1.setDirection(0,0);
                break;
              case 38:
                player1.setLastDirection();
                player1.setDirection(0,0);
                break;
              case 39:
                player1.setLastDirection();
                player1.setDirection(0,0);
                break;
              case 40:
                player1.setLastDirection();
                player1.setDirection(0,0);
                break;
              case 127:
                player1.unSetDig();
                break;
              case 81:
                player2.setLastDirection();
                player2.setDirection(0,0);
                break;
              case 83:
                player2.setLastDirection();
                player2.setDirection(0,0);
                break;
              case 68:
                player2.setLastDirection();
                player2.setDirection(0,0);
                break;
              case 90:
                player2.setLastDirection();
                player2.setDirection(0,0);
                break;
              case 65:
                player2.unSetDig();
                break;
              default:
                break;

              }
              //  System.out.println("touche relachee !" + e.getKeyCode());

            }
            
        }; // Fin de la classe anonyme
   
// On indique qu'en cas d'enfoncement de touche, c'est l'objet keyCallback
        // qui va traiter l'evenement
        addKeyListener(keyCallback);
// On indique qu'en cas de relachement de touche, c'est l'objet keyCallback2
        // qui va traiter l'evenement
        addKeyListener(keyCallback2);
       
    }

  // public void dessine() {
    
//     accel=true; // on ne redessine que ce qui a change pour accelerer
//     repaint();
//     // accel=false; // on redessinera normalement (tout)
//   }

    public void init() {

        // Creation des elements du jeu
      
      carte=new Labyrinthe("level0",inv,gen);
      Reblochon[0]=new Fromage(32,17);
      Reblochon[1]=new Fromage(35,7);
      Reblochon[2]=new Fromage(15,9);
      Reblochon[3]=new Fromage(5,12);
      Reblochon[4]=new Fromage(13,16);
      Panneau[0]=new Trappe(9,6,5,21);
      Panneau[1]=new Trappe(28,18,38,23);
       // On "dort" quelques millisecondes
          try {
            threadDeApplet.sleep(100);
          } catch(InterruptedException e) {
            System.out.println("Trap : init : Erreur dans le sleep();");
            e.printStackTrace();
          }
      listeSouris.addElement(new Souris(9, 9,carte,9,8,Panneau,Reblochon));
      listeSouris.addElement(new Souris(13, 14,carte,12,14,Panneau,Reblochon));
      listeSouris.addElement(new Souris(15, 12,carte,14,12,Panneau,Reblochon));
      ahouuu=new Loup(25, 12,carte,24,12,Panneau);
      player1=new Joueur(14,16,carte,15,16,Panneau,Reblochon,1);  
      player2=new Joueur(14,16,carte,15,16,Panneau,Reblochon,2);
       
       
}

    public void start(){
        // Si le thread de l'applet vaut null
        if(threadDeApplet == null) {
             // On cree d'un Thread a partir de l'applet et on le lance
             threadDeApplet = new Thread(this);
             threadDeApplet.start();
        }
        else {
            // Le thread existe deja, on le reveille
            threadDeApplet.resume();
        }

        // On reveille les Bebetes...
        Bebete.resume();
        
    }

    public void stop() {
        // On suspend tous les threads : celui de l'applet et les Bebetes
        threadDeApplet.suspend();
        Bebete.suspend();
    }

    /** Tache de fond executee par le Thread. Effectue l'animation du Bebete en
        modifiant ses coordonnees et en invoquant repaint(), ce qui provoque l'execution
        de paint(). Notez que run() doit imperativement contenir une boucle afin de ne pas
        rendre la main tout de suite.
    */
    public void run() {
    
      int cpt=0;
      float d[];
      float d2[];
      float d1[];
      accel=false;
      repaint(); // c'est paint lui-meme qui met accel a true

        while(true) {
            // On demande a redessiner le Bebete
          if (!dnd)
            {
              if ((cpt==300) || (carte.isAskedRepaint())) { // environ toutes les 3 secondes on redessine tout ou si demande avant 
                accel=false;
                repaint();
                cpt=0;
                carte.unSetAskedRepaint();
              }
              else {
                cpt++;
                repaint();
              }
            }
          else
            if (carte.isAskedRepaint()) {
              accel=false;
              repaint();
              carte.unSetAskedRepaint();
            }
            else
              repaint();
          d=ahouuu.getG();
          d1=player1.getG();
          d2=player2.getG();
          if (((java.lang.Math.abs(d[0]-d2[0])<0.5) && (java.lang.Math.abs(d[1]-d2[1])<0.5)) || ((java.lang.Math.abs(d[0]-d1[0])<0.5) && (java.lang.Math.abs(d[1]-d1[1])<0.5)))
            {
              if (((java.lang.Math.abs(d[0]-d2[0])<0.5) && (java.lang.Math.abs(d[1]-d2[1])<0.5)) && ((java.lang.Math.abs(d[0]-d1[0])<0.5) && (java.lang.Math.abs(d[1]-d1[1])<0.5)))
                {
                  System.out.println("all the players loose\n");
                  System.exit(0);
                }
              if ((java.lang.Math.abs(d[0]-d2[0])<0.5) && (java.lang.Math.abs(d[1]-d2[1])<0.5))
                {
                  System.out.println("player 1 win!\n");
                  System.exit(0);
                } 
              else 
                {
                  System.out.println("player 2 win!\n");
                  System.exit(0);
                } 
            }
          if (((java.lang.Math.abs(sx-d2[0])<0.5) && (java.lang.Math.abs(sy-d2[1])<0.5)) || ((java.lang.Math.abs(sx-d1[0])<0.5) && (java.lang.Math.abs(sy-d1[1])<0.5)))
            {
              if ((java.lang.Math.abs(sx-d1[0])<0.5) && (java.lang.Math.abs(sy-d1[1])<0.5))
                {
                  System.out.println("player 1 win!\n");
                  System.exit(0);
                } 
              else {
                System.out.println("player 2 win!\n");
                System.exit(0);
              }
            } 
         
          // On "dort" quelques millisecondes
          try {
            threadDeApplet.sleep(5);
          } catch(InterruptedException e) {
            System.out.println("Erreur dans le sleep();");
            e.printStackTrace();
          }
          
        }
    }
  public static void main(String args[]) 
    {
      if (args.length!=0) 
        {
          if ((args[0].compareTo("-h")==0) || (args[0].compareTo("-help")==0))
            {
              System.out.println("options:-i pour invisible");
              System.out.println("options:-d pour ne jamais reconstruire la fenetre");
              System.out.println("options:-g pour generer le labyrinthe par l'algorithme DFS (beta)");
              System.exit(0);
            }
          if (args[0].compareTo("-g")==0)
            gen=true;
          if (args[0].compareTo("-i")==0)
            inv=true;
          if (args[0].compareTo("-id")==0)
            {
              inv=true;
              dnd=true; // do not disturb mode
            }
          if (args[0].compareTo("-d")==0)
            dnd=true;
        }
      Frame fenetre=new Frame("Trap");
      Trap applet=new Trap();
      applet.init();
      applet.start();
      fenetre.add("Center",applet);
      dx=carte.getDX();
      dy=carte.getDY();
      px=carte.getPX();
      py=carte.getPY();
      //fenetre.resize((dx+2)*px,(dy+4)*py);
      fenetre.resize((dx+1)*px,(dy+4)*py);
      fenetre.show();
    }

    /** Appele lors d'un click sur un des boutons de l'applet */
    public void actionPerformed(ActionEvent evt) {
        String arg = evt.getActionCommand();
    }
    
  public synchronized void cartoon(Graphics g,Bebete b/*,boolean a*/) {
    float vr[];
    float gx,gy;
    float agx,agy;
   
    //  if (a) {
      // on efface l'ancienne position
      vr=b.getAG();
      agx=vr[0];
      agy=vr[1]; 
      g.setColor(carte.getCouleur((int)agx,(int)agy));
      g.fillRect((int)(agx*px),(int)(agy*py),px,py);
      // System.out.println((int)(agx*px)+" ag "+(int)(agy*py)+"\n");
      //  }
    
    // On dessine un Bebete
    g.setColor(b.getCouleur());
    vr=b.getG();
    gx=vr[0];
    gy=vr[1]; 
    for(int i=0;(i<10);i++)
      for(int j=0;(j<10);j++)
        if (b.getImageValue(i,j)==1)
          g.fillRect((int) ((gx*px)+j),(int)((gy*py)+i),1,1);
    //System.out.println((int)(gx*px)+" g "+(int)(gy*py)+"\n");
    b.setAG(gx,gy);
   
  }


    public void update(Graphics g) {          
        paint(g);
    }
    
    public void paint(Graphics gc) {
        // appele automatiquement par l'AWT, ou sur demande par repaint()
        Enumeration liste = listeSouris.elements();
        Souris Souris;
        Fromage Fromage;
   
        
       Color cs=gc.getColor();
       // 2 methodes d'affichage au cas ou la fenetre a ete recouverte et une accelleree
       if (!accel) // on redessine tout ==> ca scintille :((
          {
            // dessine le labyrinthe
           
            for(int x=0;(x<=dx);x++)
              for(int y=0;(y<=dy);y++)
                {
                  gc.setColor(carte.getCouleur(x,y));
                  gc.fillRect(x*px,y*py,px,py);
                }
             // dessin des Fromages
              for (int i=0;i<Reblochon.length;i++)
                {
                  // On dessine un Fromage
                  gc.setColor(Reblochon[i].getCouleur());
                  //  gc.fillRect(Reblochon[i].getX()*px,Reblochon[i].getY()*py,px,(int) Math.rint(py*Reblochon[i].getPoids()/3));
                  gc.fillArc(Reblochon[i].getX()*px,Reblochon[i].getY()*py,px,py,0,(int) Math.rint(360*Reblochon[i].getPoids()/3));
                }
            // dessin des souris
            while(liste.hasMoreElements()) {
              Souris = (Souris) liste.nextElement();
              
              // On dessine un Bebete
              cartoon(gc,Souris/*, accel */);
                        
              // dessin des joueurs
              cartoon(gc,player1/*, accel */);
              cartoon(gc,player2/*, accel */);
            }
            
            // On dessine le Loup
            cartoon(gc,ahouuu/*, accel */);
            accel=true;
          }
        else // no flashing system (C)
          {
            // dessin des Fromages
            for (int i=0;i<Reblochon.length;i++)
           {
             // On dessine un Fromage
             gc.setColor(Reblochon[i].getCouleur());
             gc.fillArc(Reblochon[i].getX()*px,Reblochon[i].getY()*py,px,py,0,(int) Math.rint(360*Reblochon[i].getPoids()/3));
           }
            // dessin des souris
            while(liste.hasMoreElements()) {
              Souris = (Souris) liste.nextElement();
                      
              // On dessine un Bebete
              cartoon(gc,Souris/*, accel */);
             
            }
              // Le Loup en personne!
             // On dessine le Loup

            cartoon(gc,ahouuu/*, accel */);
                     
         // dessin des joueurs
         
             cartoon(gc,player1/*, accel */);
             cartoon(gc,player2/*, accel */);
          }
       // dessin de la sortie
       gc.setColor(Color.orange);
       gc.fillRect(sx*px,sy*py,px,py);
        gc.setColor(cs);  
    }
  
}


