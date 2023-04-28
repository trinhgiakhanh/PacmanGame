package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighScore extends JDialog {
    private JPanel scorePanel;
    private JButton exitButton;
    
    public class Score {
        private String name;
        private int score;

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    
    public HighScore(JFrame parentFrame) {
        super(parentFrame, "Điểm cao nhất", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(parentFrame);
        
        // Tạo JPanel để chứa bảng điểm cao nhất
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        add(scorePanel, BorderLayout.CENTER);
        
        // Lấy danh sách 5 điểm cao nhất từ cơ sở dữ liệu
        ArrayList<Score> highScores = getHighScores();
        JLabel scoreLabel = new JLabel("Highscore Babeee");
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scorePanel.add(scoreLabel);
        // Thêm điểm cao nhất vào bảng
        for (int i = 0; i < highScores.size(); i++) {
            JLabel Title = new JLabel((i + 1) + ". " + highScores.get(i).name + "|" + highScores.get(i).score);
            Title.setAlignmentX(Component.CENTER_ALIGNMENT);
            Title.setFont(new Font("Arial", Font.BOLD, 20));
            scorePanel.add(Title);
        }
        
        // Thêm nút "Exit" để quay lại GameMenu
        exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        scorePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        scorePanel.add(exitButton);
    }
    
    // Phương thức để lấy danh sách 5 điểm cao nhất từ cơ sở dữ liệu
    public ArrayList<Score> getHighScores() {
        ArrayList<Score> highScores = new ArrayList<>();
        Connection conn = null;
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        // Tạo kết nối đến cơ sở dữ liệu
        try {
            Class.forName(MySqlDemo.DRIVER_CLASS);
            conn = DriverManager.getConnection(MySqlDemo.DB_URL, MySqlDemo.USER, MySqlDemo.PASS);

            // Thực hiện truy vấn để lấy toàn bộ bảng điểm và sắp xếp lại theo thứ tự giảm dần của điểm số
            String sql = "SELECT top 5 name, score FROM highscore ORDER BY score DESC ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // Lấy tên và điểm của người có điểm cao nhất và trả về chúng
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                Score highestScore = new Score(name, score);
                highScores.add(highestScore);
            }
        }
        // Xử lý ngoại lệ và đóng kết nối
        catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return highScores;
    }
}