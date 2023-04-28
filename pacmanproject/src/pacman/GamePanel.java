package pacman;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import pacman.SoundTrack;

public class GamePanel extends JPanel implements ActionListener {
	Ghost g = new Ghost(this);
    public String playerName;
	private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    public boolean inGame = false;
    public boolean dying = false;
    public boolean isPaused = false;
    
    public final int BLOCK_SIZE = 24;
    public final int N_BLOCKS = 21;
    public final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE ;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;
    private final int DELAY = 100;
    int menuX,menuY;
    int selectedOption;
    boolean mousePressed;

    public int N_GHOSTS = 6;
    private int lives, score;
    public int[] dx, dy;
    public int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    public int n_x, n_y, n_dx, n_dy, nSpeed;
    public int ndx, ndy;
    
    public Image heart, ghost,ghost2;
    private Image up, down, left, right;
    //pacman position
    public int pacman_x, pacman_y, pacmand_x, pacmand_y;
    public int req_dx, req_dy;
   
    //sound
  	SoundTrack sound = new SoundTrack();
  	//map
  	private final short levelData[] = {
    		19,	26,	26,	18,	18,	26,	26,	26,	18,	22,	0,	19,	18,	26,	26,	26,	18,	18,	26,	26,	22,
    		21,	0,	0,	17,	20,	0,	0,	0,	17,	20,	0,	17,	20,	0,	0,	0,	17,	20,	0,	0,	21,
    		17,	18,	18,	16,	16,	18,	18,	18,	16,	20,	0,	17,	16,	18,	18,	18,	16,	16,	18,	18,	20,
    		17,	24,	24,	16,	16,	16,	16,	16,	16,	16,	18,	16,	16,	16,	16,	16,	16,	16,	24,	24,	20,
    		17,	18,	18,	16,	20,	17,	16,	24,	24,	24,	24,	24,	24,	24,	16,	20,	17,	16,	18,	18,	20,
    		17,	16,	16,	16,	20,	17,	16,	18,	18,	22,	0,	19,	18,	18,	16,	20,	17,	16,	16,	16,	20,
    		25,	24,	24,	16,	20,	25,	24,	24,	16,	20,	0,	17,	16,	24,	24,	28,	17,	16,	24,	24,	28,
    		0,	0,	0,	17,	20,	19,	18,	18,	16,	16,	18,	16 , 16, 18, 18, 22, 17, 20, 0,	0,	0,
    		18,	18,	18,	16,	20,	17,	16,	16,	24,	16,	16,	16,	24,	16,	16,	20,	17,	16,	18,	18,	18,
    		24,	24,	24,	16,	20,	17,	16,	20,	3,	32,	32,	32,	6,	17,	16,	20,	17,	16,	24,	24,	24,
    		0,	0,	0,	17,	16,	16,	16,	20,	1,	32,	32,	32,	4,	17,	16,	16,	16,	20,	0,	0,	0,
    		19,	18,	18,	16,	20,	17,	16,	20,	9,	8,	8,	8,	12,	17,	16,	20,	17,	16,	18,	18,	22,
    		17,	16,	16,	16,	20,	17,	16,	24,	26,	26,	26,	26,	26,	24,	16,	20,	17,	16,	16,	16,	20,
    		17,	24,	24,	16,	20,	17,	20,	0,	0,	0,	0,	0,	0,	0,	17,	20,	17,	16,	24,	24,	20,
    		17,	18,	22,	17,	16,	16,	16,	18,	18,	22,	0,	19,	18,	18,	16,	16,	16,	20,	19,	18,	20,
    		25,	16,	20,	17,	16,	16,	16,	16,	16,	20,	0,	17,	16,	16,	16,	16,	16,	20,	17,	16,	28,
    		0,	17,	20,	17,	20,	17,	16,	16,	24,	24,	26,	24,	24,	16,	16,	20,	17,	20,	17,	20,	0,
    		19,	16,	16,	16,	20,	17,	16,	16,	18,	22,	0,	19,	18,	16,	16,	20,	17,	16,	16,	16,	22,
    		17,	24,	24,	24,	28,	25,	24,	16,	16,	20,	0,	17,	16,	16,	24,	28,	25,	24,	24,	24,	20,
    		17,	18,	18,	18,	18,	18,	18,	16,	16,	16,	18,	16,	16,	16,	18,	18,	18,	18,	18,	18,	20,
    		25,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	24,	28

    };
    //speed
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 7, 8};
    private final int maxSpeed = 8;
    private int currentSpeed = 4;
    public short[] screenData;
    private Timer timer;

    public GamePanel() {
    	
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    public void playMusic(int i) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	}
	public void stopMusic() {
		sound.stop();
	}
	public void playSE(int i) {
		sound.setFile(i);
		sound.play();
	}
    
    public void loadImages() {
    	down = new ImageIcon(getClass().getResource("/images/down.gif")).getImage();
    	up = new ImageIcon(getClass().getResource("/images/up.gif")).getImage();
    	left = new ImageIcon(getClass().getResource("/images/left.gif")).getImage();
    	right = new ImageIcon(getClass().getResource("/images/right.gif")).getImage();
        ghost = new ImageIcon(getClass().getResource("/images/ghost2.gif")).getImage();
        ghost2 = new ImageIcon(getClass().getResource("/images/ghost.gif")).getImage();
        heart = new ImageIcon(getClass().getResource("/images/heart.png")).getImage();
    }
       private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(505, 600);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(DELAY, this);
        timer.restart();
    }
       
    private void drawPauseMenu(Graphics2D g2d) {
    	    Font menuFont = new Font("Arial", Font.BOLD, 24);
    	    g2d.setFont(menuFont);
    	    
    	    String pauseMsg = "Game Paused";
    	    int msgWidth = g2d.getFontMetrics().stringWidth(pauseMsg);
    	    int centerX = SCREEN_SIZE / 2;
    	    int centerY = SCREEN_SIZE / 2 - 50;
    	    g2d.setColor(Color.white);
    	    g2d.fillRect(0, 0, SCREEN_SIZE, SCREEN_SIZE);
    	    g2d.setColor(Color.black);
    	    g2d.fillRect(10, 10, SCREEN_SIZE - 20, SCREEN_SIZE-20);
    	    g2d.setColor(Color.yellow);
    	    g2d.drawString(pauseMsg, centerX - msgWidth / 2, centerY);

    	    Font optionFont = new Font("Arial", Font.PLAIN, 20);
    	    g2d.setFont(optionFont);

    	    String continueMsg = "Press P to continue";
    	    String quitMsg = "press enter to give up";
    	    int continueWidth = g2d.getFontMetrics().stringWidth(continueMsg);
    	    int quitWidth = g2d.getFontMetrics().stringWidth(quitMsg);
    	    int optionY = centerY + 50;
    	    g2d.setColor(selectedOption == 0 ? Color.yellow : Color.white);
    	    g2d.drawString(continueMsg, centerX - continueWidth / 2, optionY);
    	    g2d.setColor(selectedOption == 1 ? Color.yellow : Color.white);
    	    g2d.drawString(quitMsg, centerX - quitWidth / 2, optionY + 30);
    	}
    private void playGame(Graphics2D g2d) {
    	
    	if (isPaused) {
    		drawPauseMenu(g2d);
        } else {
        	if (dying) {
            	playSE(2);
                death(g2d);

            } else {
                movePacman();
                drawPacman(g2d);
                g.moveGhosts(g2d);
                g.moveGhost2(g2d);
                checkMaze();
            }
        }
    }
    

    private void showIntroScreen(Graphics2D g2d) {
 
    	String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/3 +15, 250);
        
    }
    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {
        int i, dotCount;
        dotCount = 0;

        // check dot
        boolean allDotsEaten = true;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            if ((levelData[i] & 16) != 0) {
                if ((screenData[i] & 16) != 0) {
                    allDotsEaten = false;
                    break;
                }
            }
        }

        // reset dots
        if (allDotsEaten) {
        	score+=100;
        	
            initLevel();
            if(N_GHOSTS <= MAX_GHOSTS) {
        		N_GHOSTS++;
        	}
            for (i = 0; i < MAX_GHOSTS; i++) {
                if(ghostSpeed[i]<=8) {
                	ghostSpeed[i] += 1.5; //tăng tốc ghost
                }else if(ghostSpeed[i]>8) {
                	ghostSpeed[i] =ghostSpeed[i]; 
                }
            }
        }

    }
    
    private void death(Graphics g2d)  {
    	lives--;
		int freezeTimeInSeconds = 2;
		try {
		   Thread.sleep(freezeTimeInSeconds * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
        if (lives == 0) {
        	inGame = false;
        	JOptionPane.showMessageDialog(null, "Game Over");
        	playerName = JOptionPane.showInputDialog(this, "Nhập tên của bạn:");
            // Kiểm tra nếu người dùng không nhập tên hoặc nhập tên rỗng
            while (playerName == null || playerName.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Bạn phải nhập tên để lưu điểm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                playerName = JOptionPane.showInputDialog(this, "Nhập tên của bạn:");
            }
        	try {
                Class.forName(MySqlDemo.DRIVER_CLASS);
                Connection conn = DriverManager.getConnection(MySqlDemo.DB_URL, MySqlDemo.USER, MySqlDemo.PASS);
                String sql = "INSERT INTO highscore (name, score) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, playerName);
                pstmt.setInt(2, score);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Hey, it is work");
                }
                pstmt.close();
                conn.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        	
        	System.out.println(playerName+","+score);
            
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            gameFrame.setVisible(false);
            new GameMenu().setVisible(true);
        }
        g.continueLevel();
        
    }

    
    private void movePacman() {
        int pos;
        short ch;
        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            // Check for food
            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
                playSE(1);
            }

            // Check for walls and update direction if needed
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 

        // Update Pacman's position
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;

        // Check if Pacman is out of bounds and teleport to the other side if needed
        if (pacman_x < 0) {
            pacman_x = (N_BLOCKS - 1) * BLOCK_SIZE;
        } else if (pacman_x >= N_BLOCKS * BLOCK_SIZE) {
            pacman_x = 0;
        }
        if (pacman_y < 0) {
            pacman_y = (N_BLOCKS - 1) * BLOCK_SIZE;
        } else if (pacman_y >= N_BLOCKS * BLOCK_SIZE) {
            pacman_y = 0;
        }
    }


    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

            	g2d.setColor(new Color(0, 128, 0));
                g2d.setStroke(new BasicStroke(5));
                
                if ((levelData[i] == 0)) { 
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                 }

                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }

                if ((screenData[i] & 32) != 0) { 
//                    g2d.setColor(new Color(0,0,0));
               }
                i++;
            }
        }
    }

    private void initGame() {
    	lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 4;
//        N_GHOSTS = 1;
        currentSpeed = 3;
        for(int i=0;i<MAX_GHOSTS;i++) {
        	g.spawnGhost(i, 11, 11);
        }
        g.spawnGhost2(11, 11);

    }

    
    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        
        g.continueLevel();
    }

    

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
        	
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            if (key == KeyEvent.VK_P) { // Kiểm tra phím P
                isPaused = !isPaused; // Đảo ngược trạng thái tạm dừng
                if (isPaused) {
                    stopMusic(); // Tạm dừng nhạc nền khi tạm dừng game
                    timer.stop();
                } else {
                     // Chơi lại nhạc nền khi tiếp tục game
                }
            }
            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    stopMusic();
                    initGame();
                }
            }
            if (isPaused) {
                switch (key) {
	                case KeyEvent.VK_UP:
	                    selectedOption = 0;
	                    break;
	                case KeyEvent.VK_DOWN:
	                	selectedOption = 1;
	                    break;
                    case KeyEvent.VK_P:
                        selectedOption = 0;
                        break;
                    case KeyEvent.VK_RIGHT:
                    	selectedOption = 1;
                        break;
                    case KeyEvent.VK_ENTER:
                    	
                        if (selectedOption == 0) {
                            isPaused = false;
                        }else if(selectedOption == 1) {
                        	System.exit(0);
                        }
                        break;
                }
            }
        }
}
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
		
	}