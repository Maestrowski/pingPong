import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.*;

public class Game extends JPanel implements ActionListener, KeyListener {

    static int width = 600;
    static int height = 400;

    //Game control
    Timer t = new Timer(20,this);
    boolean isGameOn = true;

    int ballSize = 20;
    int ballSpeed = 6;
    int ballSpeedX = ballSpeed;
    int ballSpeedY = ballSpeed;
    int ballX = (width-ballSize)/2;
    int ballY = (width-ballSize)/2;
    int ballSpeedIncrease = 2;

    //Paddle Variables
    int paddleHeight = 100;
    int paddleWidth = 20;
    int paddleSpeed = 7;

    //Player 1
    int p1Score = 0;
    int p1X = 0;
    int p1Y = (height - paddleHeight) / 2;
    int p1Dir = 0;

    //AI
    int AIScore = 0;
    int AIX = width - paddleWidth;
    int AIY = (height - paddleHeight) / 2;
    int AIDir = 0;

    Game() {
        //Key Listener
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        //Action Listener - Time based events
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Print the background
        g2.setColor(Color.blue);
        g2.fill(getBounds());

        g2.setColor(Color.white);
        //Paint the ball
        g2.fillOval(ballX, ballY, ballSize, ballSize);
        
        //Paint the paddles 
        //Player 1
        g2.fillRect(p1X, p1Y, paddleWidth, paddleHeight);

        //AI
        g2.fillRect(AIX, AIY, paddleWidth, paddleHeight);

        //Paint the scores
        g2.setFont(new Font("Times New Roman", Font.BOLD, 40));
        //Player 1
        g2.drawString(p1Score + "", 100, 50);

        g2.drawString(AIScore + "", 500, 50);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        Game game = new Game();
        game.setPreferredSize(new Dimension(width,height));
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getExtendedKeyCode();

        
        if (code == KeyEvent.VK_W) {
            p1Dir = -1;
        }
        if (code == KeyEvent.VK_S) {
            p1Dir = 1;
        }
        if (code == KeyEvent.VK_D) {
            p1Dir = 0;
        }

        if(!isGameOn && code == KeyEvent.VK_R) {
            reset();
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if(isGameOn) {

        //AI wins
        if (ballX < 0) {
            AIScore++;
            isGameOn = false;
        }

        //Player 1 wins
        if(ballX + ballSize>width){
            p1Score++;
            isGameOn = false;
        }

        //Collision from top and bottom edges
        if ( (ballY <= 0 && ballSpeedY < 0)|| ballY + ballSize >= height && ballSpeedY > 0) {
            ballSpeedY = -ballSpeedY; 
        }

        //Collision from AI paddle
        if (ballX + ballSize > AIX && ballSpeedX > 0) {
            if (ballY + ballSize > AIY && ballY < AIY + paddleHeight) {
                //Calculate the contact point
                int contactPoint = ballY + ballSize / 2 - AIY;

                //Adjust the angle of the ball based on the contact point
                double anglePoint = (double) contactPoint / (double) paddleHeight;
                ballSpeedY += (int)(anglePoint)*ballSpeed + ballSpeedIncrease; 

                ballSpeedX = -1*(ballSpeedX + (ballSpeedIncrease / 2));
                ballSpeedY = -ballSpeedY;
                System.out.println("AI: " + "X: " + ballSpeedX + " Y: " + ballSpeedY);
            }
        }

        //Collision from player 1 paddle
        if (ballX < p1X + paddleWidth && ballSpeedX < 0) {
            if (ballY + ballSize > p1Y && ballY < p1Y + paddleHeight) {
                //Calculate the contact point
                int contactPoint = ballY + ballSize / 2 - p1Y;

                //Adjust the angle of the ball based on the contact point
                double anglePoint = (double) contactPoint / (double) paddleHeight;
                ballSpeedY += (int)(anglePoint)*ballSpeed + ballSpeedIncrease; 

                ballSpeedX = -1*(ballSpeedX - (ballSpeedIncrease / 2)); //Increase the speed of the ball
                ballSpeedY = -ballSpeedY;
                System.out.println("Player 1: " + "X: " + ballSpeedX + " Y: " + ballSpeedY);
            }
        }

        //Update AI paddle position based on the ball's position
        if (ballY + ballSize / 2 < AIY + paddleHeight / 2) {
            AIY -= paddleSpeed;
        } 
        else {
            AIY += paddleSpeed;
        }
        
        //Update paddle locations
        //Paddle 1
        p1Y += p1Dir*paddleSpeed;

        //Paddle 2
        AIY += AIDir*paddleSpeed;

        //Updating the ball location

        ballX += ballSpeedX;
        ballY += ballSpeedY;

        
        repaint();
        }
    }
    void reset() {
        isGameOn = true;
        ballX = (width-ballSize) / 2;
        ballY = (height-ballSize) / 2;
        ballSpeedX = ballSpeed;
        ballSpeedY = ballSpeed;
        ballSpeedX = Math.random()>0.5?ballSpeedX: -ballSpeedX;
        ballSpeedY = Math.random()>0.5?ballSpeedY: -ballSpeedY;
        p1Y = (height - paddleHeight) / 2;
        AIY = (height - paddleHeight) / 2; 
        p1Dir = 0;
        AIDir = 0;
    }
}