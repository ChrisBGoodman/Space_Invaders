package program7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
/*
|*@author ChristianGoodman
|*@date 11/21/14
|*@SpaceInvaders
|*@Dr. Smith / Java Programming
*/
public class SpaceInvaders extends JFrame
{
    private int score = -1;             //Vars for labels and creation of labels 
    private int targetsHit = 0;
    private int shots = 0;
    private JLabel timerLabel = new JLabel("Time: ");
    private JLabel shotCounter = new JLabel("Shots: " + shots);
    private JLabel scoreLabel = new JLabel("Score: " + score + " - Targets Hit: " + targetsHit);
    private JLabel updateLabel = new JLabel("Press Down to Start");
    
    private Image backgroundImage;      //Image vars to hold all required images 
    private Image cannon;
    private Image cannonBall;
    private Image target;
    private gamePanel gp;
    
    private int cannonXAxis = 300;      //Vars for control of movement on screen 
    private int cannonYAxis = 418; 
    private int cBallXAxis = 316;
    private int cBallYAxis = 416;
    private int cBallYHome = 416;
    private int x_offset;
    private int x_offsetCBALL;
    private boolean[] targetHit = new boolean[10];
    private int[][] targetCoord = new int [10][2];
    private int targetsMissed = 0;
    
    private Timer timer1;
    private TimerTask tf;
    boolean pic_touched = false; 
   
    private long tStartMilli;           //Vars for time control
    private long tEndMilli;
    private long tElapsedMilli;
    private long finalTime;
    

    public SpaceInvaders()
    {
        try{                                                                   //Read in images used 
            backgroundImage = ImageIO.read(new File("src/program7/bg2.png"));
            cannon = ImageIO.read(new File("src/program7/cannon1.png"));
            cannonBall = ImageIO.read(new File("src/program7/cannonBall.png"));
            target = ImageIO.read(new File("src/program7/target.png"));
        } catch (IOException e){e.printStackTrace();}
        
        for (int i = 0; i < 10; i++)                                        //Initialize all targets hit to not show
            targetHit[i] = true; 
        
        keylistener keyListener = new keylistener();                        //Initial KeyListener
        MouseHandler mh = new MouseHandler();                               //Initial MouseListener
        MouseClickedHandler mch = new MouseClickedHandler();                //Initial MouseClick
        gp = new gamePanel();                                               //Initial panel
        
        gp.addMouseMotionListener(mh);                                      //Add MouseMotionListener
        gp.addMouseListener(mch);                                           //Add MouseListener
        gp.addKeyListener(keyListener);                                     //Add KeyListener
        gp.setFocusable(true);                                              //Request Focus for Events
        
        timer1 = new java.util.Timer();
        tf = new TimerFired();
        timer1.scheduleAtFixedRate( tf, 0, 65);

        gp.setLayout(new FlowLayout()); 
        gp.add(timerLabel);
        gp.add(scoreLabel);
        gp.add(shotCounter);
        gp.add(updateLabel);
        
        this.add(gp);
        this.setResizable(false);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(640,480);
        this.setVisible(true);       
    }
  
//=============================================================
    class gamePanel extends JPanel implements Runnable
    {
        public void run()
        {
            try {
              
                repaint();  
            } catch(Exception e){
                e.printStackTrace();}
        }
        
        public void paintComponent(Graphics g)
        {
            super.paintComponents(g);
            g.drawImage(backgroundImage, 0, 0, this);
            g.drawImage(cannon,cannonXAxis, cannonYAxis, this);
            g.drawImage(cannonBall, cBallXAxis, cBallYAxis, this);

            
            for (int r = 0; r < 10; r++)
            {
                if (targetHit[r] == false) 
                 g.drawImage(target, targetCoord[r][0] , targetCoord[r][1], this);
            }
              
            tEndMilli = System.currentTimeMillis();
          
            tElapsedMilli = (tEndMilli - tStartMilli);
            
            timerLabel.setText("Time: " + tElapsedMilli/1000.0);

            if (score == 10)
            {
                finalTime = (long) (tElapsedMilli/1e3);
                timerLabel.setText("Time: " + finalTime);
            }
            
            if (score == -1)
            {
                timerLabel.setText("Time: " + 0);
            }
        }
    }
    
    
//=============================================================
    public static void main(String[] args)
    {
        SpaceInvaders si = new SpaceInvaders();
    }
  
//=============================================================
    class keylistener implements KeyListener 
    {
        @Override
           public void keyTyped(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {            
            //38 UP //40 //37 //39 RIGHT
            if (e.getKeyCode() == 37) //Left Arrow key, move Cannon                             // -- LEFT --
            {
                cannonXAxis = cannonXAxis - 5;  //Move Cannon left 5 pixels
                
                if (cBallYAxis == 416)          //IF cBall is home move with cannon. Otherwise let it stay on path 
                {
                    cBallXAxis = cBallXAxis - 5;//Move cBall left 5 pixels
                }
            }

            if (e.getKeyCode() == 39) //Right Arrow key, move Cannon                            // -- RIGHT --
            {
                cannonXAxis = cannonXAxis + 5;  //Move Cannon Right 5 pixels

                if (cBallYAxis == 416)          //If Cball is home move with cannon. 
                {
                    cBallXAxis = cBallXAxis + 5;//Move cBall right 5 pixels
                }
            }

            if (e.getKeyCode() == 38 && cBallYAxis == 416) //Up arrow, shoot cBall              // -- UP --
            {
                cBallYAxis = 415;
                shots = shots + 1;
                shotCounter.setText("Shots: " + shots);  
            }
            
            if (e.getKeyCode() == 40 && cBallYAxis == 416) //Key ID for Arrow Down              // -- DOWN --
            {
                Random ran = new Random();
                for (int r = 0; r < 10; r++)                //Random X Coord generator 
                {            
                        targetCoord[r][0] = ran.nextInt(610);   
                        targetHit[r] = false;
                }
                
                for (int c = 0; c < 10; c++)                //Random Y Coord generator
                {            
                        targetCoord[c][1] = ran.nextInt(250);   
                }
                    
                cannonXAxis = 300;                       //Reset's EVERYTHING on Down Arrow Press
                cannonYAxis = 418; 
                cBallXAxis = 316;
                cBallYAxis = cBallYHome;
                score = 0;
                shots = 0;
                targetsMissed = 0;
                targetsHit = 0;
                shotCounter.setText("Shots: " + shots);
                scoreLabel.setText("Score: " + score + " - Targets Hit: " + targetsHit);
                timerLabel.setText(" Time: " + 0 + "");
                updateLabel.setText("");
                tStartMilli = System.currentTimeMillis();
                
                for (int i = 0; i < 10; i++)
                    targetHit[i] = false;               //All targets not Hit
            }
            repaint();
        }

//=============================================================
        @Override
        public void keyReleased(KeyEvent e){}
        };
       
       public class TimerFired extends TimerTask implements Runnable
       {

        @Override
        public void run()
        {
            if (cBallYAxis <= 415)              //If CBall is at home and fired
            {
                cBallYAxis = cBallYAxis - 5;    //Move upwards 5 pixels every TimerFired
            }

            if ( cBallYAxis < 2)                //If off screen, reset to resting position
            {
                cBallYAxis = 416;
                cBallXAxis = cannonXAxis + 16;
            }
            
            for (int i = 0; i < 10; i++)                    //Loop through all targets
            {
                targetCoord[i][1] = targetCoord[i][1] + 1;  //Move Target down 1 pixel
                
                if (targetCoord[i][1] > 480)                //If Dropped below screen
                {
                    targetsMissed = targetsMissed + 1;      
                    
                    if (targetsMissed >= 2)             //If 2 targets have been missed
                    {                                   //Runs down Arrow Code to reset 
                        cannonXAxis = 300; 
                        cannonYAxis = 418; 
                        cBallXAxis = 316;
                        cBallYAxis = cBallYHome;
                        score = 0;
                        shots = 0;
                        shotCounter.setText("Shots: " + shots);
                        scoreLabel.setText("Score: " + score + " - Targets Hit: " + targetsHit);
                        timerLabel.setText(" Time: " + 0 + "");
                        updateLabel.setText("GAME LOST. You missed too many targets");
                        
                        for (int w = 0; w < 10; w++)
                            targetHit[w] = false;
                    }
                }
                
                if (cBallXAxis < targetCoord[i][0] + 40 && cBallXAxis > targetCoord[i][0]) //Checking for Collision
                {                                               //Checks for X Coord
                    if (cBallYAxis - 30 <  targetCoord[i][1])   //Checks for Y Coord
                    {
                        targetHit[i] = true;
                        targetCoord[i][0] = 0;                  //move Target off screen so it may not be hit again.
                        targetCoord[i][1] = -2000;          
                        score = score + 1;                      //Update everything else
                        targetsHit = targetsHit + 1;
                        scoreLabel.setText("Score: " + score + " - Targets Hit: " + targetsHit);
                        cBallXAxis = cannonXAxis + 16;        
                        cBallYAxis = cBallYHome;
                    }
                }
            }
            repaint();
            
            if (score == 10)
            {
                updateLabel.setText("CONGRATULATIONS YOU WON! Down Arrow to Restart");
            }
        }
       }

//=============================================================
       class MouseClickedHandler extends MouseAdapter
       {
           public void mousePressed(MouseEvent me)
           {
               if ( (me.getX() > cannonXAxis) && (me.getX() < cannonXAxis + 40) && (me.getY() > cannonYAxis) && (me.getY() < cannonYAxis + 40))
               {
                   pic_touched = true;
                   x_offset = me.getX() - cannonXAxis;
                   x_offsetCBALL = me.getX() - cBallXAxis;
               }
               else
               {
                   pic_touched = false;
               }
           }
       }

//=============================================================
       class MouseHandler extends MouseMotionAdapter
       {         
           public void mouseDragged(MouseEvent e)
           {             
             if (pic_touched)
             {
                 cannonXAxis = e.getX() - x_offset;
                 
                 if (cBallYAxis == 416)
                 {
                 cBallXAxis = e.getX() - x_offsetCBALL;                 
                 }
             }
             repaint();
         }
       }
}

