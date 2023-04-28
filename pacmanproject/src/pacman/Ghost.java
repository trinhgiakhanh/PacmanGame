package pacman;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Ghost {
	public Image ghost;
	
	GamePanel gp;
	public Ghost(GamePanel gp) {
		this.gp = gp;
	}
	
	public void moveGhost2(Graphics2D g2d) {
        int pos;
        int count;
        if (gp.n_x % gp.BLOCK_SIZE == 0 && gp.n_y % gp.BLOCK_SIZE == 0) {
            pos = gp.n_x / gp.BLOCK_SIZE + gp.N_BLOCKS * (int) (gp.n_y / gp.BLOCK_SIZE);

            count = 2;
            
            if (count == 0) {

                if ((gp.screenData[pos]  & 15) == 15) {
                	gp.n_dx = 0;
                	gp.n_dy = 0;
                } else {
                	gp.n_dx = -gp.n_dx;
                	gp.n_dy = -gp.n_dy;
                }

            } else {

                count = (int) (Math.random() * count);

                if (count > 3) {
                    count = 3;
                }
                gp.n_dx = gp.dx[count];
                gp.n_dy = gp.dy[count];
            }
        }
        gp.n_x = gp.n_x + (gp.n_dx * gp.nSpeed);
        gp.n_y = gp.n_y + (gp.n_dy * gp.nSpeed);
        
        // Teleport ghost if it moves outside the map
        if (gp.n_x < 0) {
        	gp.n_x = gp.N_BLOCKS * gp.BLOCK_SIZE;
        } else if (gp.n_x > gp.N_BLOCKS * gp.BLOCK_SIZE) {
        	gp.n_x = 0;
        } else if (gp.n_y < 0) {
        	gp.n_y = gp.N_BLOCKS * gp.BLOCK_SIZE;
        } else if (gp.n_y > gp.N_BLOCKS * gp.BLOCK_SIZE) {
        	gp.n_y = 0;
        }
        drawGhost2(g2d, gp.n_x + 1, gp.n_y + 1);
        if (gp.pacman_x > (gp.n_x - 12) && gp.pacman_x < (gp.n_x + 12)
                && gp.pacman_y > (gp.n_y - 12) && gp.pacman_y < (gp.n_y + 12)
                && gp.inGame) {
            gp.dying = true;
        }
    }
	
	public void moveGhosts(Graphics2D g2d) {

        int pos;
        int count;

        for (int i = 0; i < gp.N_GHOSTS; i++) {
            if (gp.ghost_x[i] % gp.BLOCK_SIZE == 0 && gp.ghost_y[i] % gp.BLOCK_SIZE == 0) {
                pos = gp.ghost_x[i] / gp.BLOCK_SIZE + gp.N_BLOCKS * (int) (gp.ghost_y[i] / gp.BLOCK_SIZE);

                count = 0;

                if ((gp.screenData[pos] & 1) == 0 && gp.ghost_dx[i] != 1) {
                	gp.dx[count] = -1;
                	gp.dy[count] = 0;
                    count++;
                }

                if ((gp.screenData[pos]  & 2) == 0 && gp.ghost_dy[i] != 1) {
                	gp.dx[count] = 0;
                	gp.dy[count] = -1;
                    count++;
                }

                if ((gp.screenData[pos]  & 4) == 0 && gp.ghost_dx[i] != -1) {
                	gp.dx[count] = 1;
                	gp.dy[count] = 0;
                    count++;
                }

                if ((gp.screenData[pos]  & 8) == 0 && gp.ghost_dy[i] != -1) {
                	gp.dx[count] = 0;
                	gp.dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((gp.screenData[pos]  & 15) == 15) {
                    	gp.ghost_dx[i] = 0;
                    	gp.ghost_dy[i] = 0;
                    } else {
                    	gp.ghost_dx[i] = -gp.ghost_dx[i];
                    	gp.ghost_dy[i] = -gp.ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    gp.ghost_dx[i] = gp.dx[count];
                    gp.ghost_dy[i] = gp.dy[count];
                }

            }

            gp.ghost_x[i] = gp.ghost_x[i] + (gp.ghost_dx[i] * gp.ghostSpeed[i]);
            gp.ghost_y[i] = gp.ghost_y[i] + (gp.ghost_dy[i] * gp.ghostSpeed[i]);
            
            // Teleport ghost if it moves outside the map
            if (gp.ghost_x[i] < 0) {
            	gp.ghost_x[i] = gp.N_BLOCKS * gp.BLOCK_SIZE;
            } else if (gp.ghost_x[i] > gp.N_BLOCKS * gp.BLOCK_SIZE) {
            	gp.ghost_x[i] = 0;
            } else if (gp.ghost_y[i] < 0) {
            	gp.ghost_y[i] = gp.N_BLOCKS * gp.BLOCK_SIZE;
            } else if (gp.ghost_y[i] > gp.N_BLOCKS * gp.BLOCK_SIZE) {
            	gp.ghost_y[i] = 0;
            }
            drawGhost(g2d, gp.ghost_x[i] + 1, gp.ghost_y[i] + 1,i);
            
            if (gp.pacman_x > (gp.ghost_x[i] - 12) && gp.pacman_x < (gp.ghost_x[i] + 12)
                    && gp.pacman_y > (gp.ghost_y[i] - 12) && gp.pacman_y < (gp.ghost_y[i] + 12)
                    && gp.inGame) {

                gp.dying = true;
            }
        }
        
    }
	public void spawnGhost(int i, int x, int y) {
		gp.ghost_x[i] = x * gp.BLOCK_SIZE;
		gp.ghost_y[i] = y * gp.BLOCK_SIZE;
		gp.ghost_dx[i] = 0;
		gp.ghost_dy[i] = 0;
		gp.ghostSpeed[i] = 3;
    }
	public void spawnGhost2( int x, int y) {
		gp.n_x = x * gp.BLOCK_SIZE;
		gp.n_y = y * gp.BLOCK_SIZE;
		gp.n_dx = 0;
		gp.n_dy = 0;
		gp.nSpeed = 3;
    }
	public void drawGhost(Graphics2D g2d, int x, int y,int i) {
    	g2d.drawImage(gp.ghost, x, y, gp);
    }
	public void drawGhost2(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(gp.ghost2, x, y,null);
    }
	public void continueLevel() {

    	int dx = 1;
        int random;

        for (int i = 0; i < gp.N_GHOSTS; i++) {

        	gp.ghost_y[i] = 11 *gp. BLOCK_SIZE; //start position
        	gp.ghost_x[i] = 11 * gp.BLOCK_SIZE;
        	gp.ghost_dy[i] = 0;
        	gp.ghost_dx[i] = dx;
            dx = -dx;
//            random = (int) (Math.random() * (currentSpeed + 1));
//
//            if (random > currentSpeed) {
//                random = currentSpeed;
//            }

            gp.ghostSpeed[i] = 2;
        }
        
        gp.n_x = 11 *gp. BLOCK_SIZE;
		gp.n_y = 11 *gp. BLOCK_SIZE;
		gp.n_dx = 0;
		gp.n_dy = dx;
		gp.nSpeed = 2;
		
        gp.pacman_x = 10 * gp.BLOCK_SIZE;  //start position
        gp.pacman_y = 16 * gp.BLOCK_SIZE;
        gp.pacmand_x = 0;	//reset direction move
        gp.pacmand_y = 0;
        gp.req_dx = 0;		// reset direction controls
        gp.req_dy = 0;
        gp.dying = false;
    }
	
	
}
