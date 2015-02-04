/**
 * @author Created By Christian Goodman 
 * 11/13/2014
 * Program 7 - Dr. Smith
 */
package program7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ButtonBounce extends JFrame
{ 
    private BallPanel bp;
    private JPanel scrollPanel = new JPanel(); //Used to hold the 4 scroll bars and their labels
    private JScrollBar xInc;
    private JScrollBar yInc;
    private JScrollBar xScrollSize;
    private JScrollBar yScrollSize;
    private JLabel xLabel;
    private JLabel yLabel;
    private JLabel wLabel;
    private JLabel hLabel;
    private int speedX = 1;
    private int speedY = 1;
    private int sizeX = 10;
    private int sizeY = 10;
    
    private static int x_val = 1;
    private static int y_val = 1;
   
    
    JPanel paintPanel = new JPanel();
    JPanel bottumPanel = new JPanel();
    JButton jb;
    
    public ButtonBounce()
    {
        bp = new BallPanel();
        JPanel jp = new JPanel();
        this.add(bp, BorderLayout.CENTER);
   
        scrollPanel.setLayout(new GridLayout(8,1,1,1));
        
        xInc = new JScrollBar(0,1,1,0,50);              //Code for X speed scroll bar
        adjX adjustmentX = new adjX();
        xInc.addAdjustmentListener(adjustmentX);
        xLabel = new JLabel ("X Speed: " + speedX);
        scrollPanel.add(xInc);
        scrollPanel.add(xLabel);
        
        yInc = new JScrollBar(0,1,1,0,50);              //Code for Y speed scroll bar
        adjY adjustmentY = new adjY();
        yInc.addAdjustmentListener(adjustmentY);
        yLabel = new JLabel ("Y Speed: " + speedY);
        scrollPanel.add(yInc);
        scrollPanel.add(yLabel);
        
        xScrollSize = new JScrollBar(0,1,1,0,100);       //Code for X Size scroll bar
        AdjxScrollSize adjustmentSizeX = new AdjxScrollSize();
        xScrollSize.addAdjustmentListener(adjustmentSizeX);
        wLabel = new JLabel ("X Size: " + sizeX);
        scrollPanel.add(xScrollSize);
        scrollPanel.add(wLabel);
        
        yScrollSize = new JScrollBar(0,1,1,0,100);       //Code for Y Size scroll bar
        AdjyScrollSize adjustmentSizeY = new AdjyScrollSize();
        yScrollSize.addAdjustmentListener(adjustmentSizeY);
        hLabel = new JLabel ("Y Size: " + sizeY);
        scrollPanel.add(yScrollSize);
        scrollPanel.add(hLabel);
       
        this.add(scrollPanel, BorderLayout.EAST); 
       
        //JButtons Start and Stop on the South panel
        //------------------------------------------------
        jb = new JButton ("Start");
        jb.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)         //Action for Add Button
            {                                                   //calls Run() function to start animation(repainting)
                xInc.setValue(1);
                yInc.setValue(1);
                speedX = 1;
                speedY = 1; 
                bp.run();
            }
        });
        bottumPanel.add(jb);
        //------------------------------------------------

        jb = new JButton ("Stop");
        jb.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)         //Action for Stop Button
            {                                                   //calls stop() function to freeze Oval in current position
                bp.stop();
            }
        });
        bottumPanel.add(jb);
        
        this.add(bottumPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        ButtonBounce bb = new ButtonBounce();
    }
//----------------------------------------------------------
    
    class BallPanel extends JPanel implements Runnable
    {  
        public void run()
        {
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

            double width = bp.getBounds().getWidth();
            double height = bp.getBounds().getHeight();

            try {
                x_val = x_val + speedX;
                y_val = y_val + speedY;

                if ((x_val + sizeX) > width || x_val < 0)
                {
                    speedX = speedX * -1;
                }

                if ((y_val + sizeY) > height || y_val < 0)
                {
                    speedY = speedY * -1;
                }

                repaint();  
            } catch(Exception e){
                e.printStackTrace();}
        }   

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(Color.blue);
            g.fillOval(x_val, y_val, sizeX, sizeY);
            run();
        }
        public void stop()
        {
            xInc.setValue(0);
            yInc.setValue(0);
            speedX = 0;
            speedY = 0; 
        }
    }
    
// The below class are for changing/updating the scroll bars along with xSize, ySize, xSpeed, and ySpeed
//-----------------------------------------------------------
    class adjX implements AdjustmentListener     //Action class for Xscroll bar to change X speed
    {
        public void adjustmentValueChanged(AdjustmentEvent ae)
        {
            speedX = xInc.getValue();
            System.out.println("Inside AdjustmentListener Event for X speeds" + speedX);
            xLabel.setText("X Speed: " + xInc.getValue());   
        }
    }
//-----------------------------------------------------------
    class adjY implements AdjustmentListener     //Action class for YScroll bar to change Y speed
    {
        public void adjustmentValueChanged(AdjustmentEvent ae)
        {
            speedY = yInc.getValue();
            System.out.println("Inside AdjustmentListener Event for Y");
            yLabel.setText("Y Speed: " + yInc.getValue());
        }
    }
//-----------------------------------------------------------    
    class AdjxScrollSize implements AdjustmentListener      //Action class for XSize scroll bar, changes Width
    {
        public void adjustmentValueChanged(AdjustmentEvent ae)
        {
            sizeX = xScrollSize.getValue();
            wLabel.setText("X Size: " + xScrollSize.getValue());
        }
    }
//-----------------------------------------------------------
    class AdjyScrollSize implements AdjustmentListener      //Action class for YSize scroll bar, changes Height 
    {
        public void adjustmentValueChanged(AdjustmentEvent ae)
        {
            sizeY = yScrollSize.getValue();
            hLabel.setText("Y Size: " + yScrollSize.getValue());
        }    
    }
}
