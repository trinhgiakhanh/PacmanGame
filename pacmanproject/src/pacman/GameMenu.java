package pacman;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GameMenu extends JFrame implements ActionListener {
    private JButton startButton;
    private JButton quitButton;
    private JButton hscoreButton;
    private GamePanel gamePanel;
    public Image setIMG(String imgName) {
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream(imgName));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return img;
	}
    public GameMenu() {
    	// Thiết lập cấu hình của JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Pacman");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Tạo các thành phần giao diện
        gamePanel = new GamePanel();
        gamePanel.playSE(0);
        startButton = new JButton("Bắt đầu chơi");
        startButton.setMaximumSize(new Dimension(300, 60)); // cao 50, rộng 300
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa theo trục X
        startButton.addActionListener(this);

        quitButton = new JButton("Thoát");
        quitButton.setMaximumSize(new Dimension(300, 60)); // cao 50, rộng 300
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa theo trục X
        quitButton.addActionListener(this);
        
        hscoreButton = new JButton("Điểm cao nhất");
        hscoreButton.setMaximumSize(new Dimension(300, 60)); // cao 50, rộng 300
        hscoreButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa theo trục X
        hscoreButton.addActionListener(this);
        // JPanel chứa giao diện
        JPanel panel = new JPanel();
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = null;
                img = setIMG("/images/images.jpg");
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createRigidArea(new Dimension(100, 300)));
        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(quitButton);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(hscoreButton);
        panel.add(Box.createVerticalGlue());

        // Thêm JPanel vào JFrame
        add(panel);

        // Hiển thị JFrame
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
        	gamePanel.stopMusic();
            dispose(); // Đóng menu
            GameFrame.GamePlay();
        } else if (e.getSource() == quitButton) {
            // Thực hiện hành động khi người dùng nhấn nút "Thoát"
            System.exit(0);
        }else if(e.getSource() == hscoreButton) {
        	HighScore HighScore = new HighScore(this);
        	HighScore.setVisible(true);
        }
    }
    public static void main(String[] args) {
        GameMenu menu = new GameMenu();
    }
}
