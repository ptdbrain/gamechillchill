package chapter.chap3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import chapter.chap0.src.main.GamePanel;
import chapter.chap1.GamePanel_chap1;
import game.SceneTransition;

public class RacingGame extends JPanel implements ActionListener, KeyListener {
    // Kích thước màn hình
    private static final int SCREEN_WIDTH = 960;
    private static final int SCREEN_HEIGHT = 840;
    private static final int ROAD_WIDTH = 400;

    // Biến liên quan đến xe
    private int carX, carY;
    private int carWidth = 50;
    private int carHeight = 80;
    private int carSpeed = 15;

    // Biến điều khiển xe
    private boolean moveLeft = false;
    private boolean moveRight = false;

    // Các chướng ngại vật
    private ArrayList<Obstacle> obstacles;
    private Random random;
    private Timer gameLoop;
    private Timer movementTimer;
    private Timer speedIncreaseTimer;

    // Tốc độ cuộn của đường
    private int baseScrollSpeed = 10;
    private int currentScrollSpeed = 10;
    private double speedMultiplier = 1.1; // Tăng tốc độ mỗi giây

    // Hình ảnh
    private BufferedImage roadImage;
    private BufferedImage carImage;
    private BufferedImage[] obstacleImages;

    // Vị trí của đường
    private int roadY1 = 0;
    private int roadY2 = -SCREEN_HEIGHT;

    // Tham chiếu đến JFrame
    private JFrame parentFrame;
    private Runnable gameOverCallback;

    // Constructor của RacingGame
    public static GamePanel gamePanel = new GamePanel();

    public RacingGame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        gamePanel.playMusic(14);
        //this.setBackground(Color.BLACK);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(this); // Đăng ký KeyListener cho RacingGame

        try {
            // Tải ảnh từ thư mục "pic"
            roadImage = ImageIO.read(new File("pic/road.png"));
            carImage = ImageIO.read(new File("pic/car3.png"));

            String[] obstaclePaths = {
                "pic/police.png",
                "pic/barrier.png",
                "pic/truck.png",
                "pic/car1.png",
                "pic/car2.png"
            };

            obstacleImages = new BufferedImage[obstaclePaths.length];
            for (int i = 0; i < obstaclePaths.length; i++) {
                obstacleImages[i] = ImageIO.read(new File(obstaclePaths[i]));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load one or more images. Please check file paths.");
        }

        resetGame(); // Khởi tạo lại game
    }
    public void setGameOverCallback(Runnable callback) {
        this.gameOverCallback = callback;
    }
    // Khởi tạo lại game
    private void resetGame() {
        carX = SCREEN_WIDTH / 2 - carWidth / 2;
        carY = SCREEN_HEIGHT - carHeight - 50;
        obstacles = new ArrayList<>();
        random = new Random();
        currentScrollSpeed = baseScrollSpeed;

        // Dừng và khởi động lại tất cả các timer
        if (gameLoop != null) gameLoop.stop();
        if (movementTimer != null) movementTimer.stop();
        if (speedIncreaseTimer != null) speedIncreaseTimer.stop();

        gameLoop = new Timer(16, this);
        gameLoop.start();

        movementTimer = new Timer(16, e -> smoothMovement());
        movementTimer.start();

        speedIncreaseTimer = new Timer(1000, e -> currentScrollSpeed *= speedMultiplier);
        speedIncreaseTimer.start();
    }

    // Di chuyển xe mượt mà
    private void smoothMovement() {
        if (moveLeft && carX > (SCREEN_WIDTH - ROAD_WIDTH) / 2) {
            carX -= carSpeed;
        }
        if (moveRight && carX < (SCREEN_WIDTH + ROAD_WIDTH) / 2 - carWidth) {
            carX += carSpeed;
        }
        repaint();
    }

    // Vẽ các đối tượng trong game
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (roadImage != null) {
            // Vẽ đường
            g.drawImage(roadImage, (SCREEN_WIDTH - ROAD_WIDTH) / 2, roadY1, ROAD_WIDTH, SCREEN_HEIGHT, null);
            g.drawImage(roadImage, (SCREEN_WIDTH - ROAD_WIDTH) / 2, roadY2, ROAD_WIDTH, SCREEN_HEIGHT, null);
        } else {
            g.setColor(Color.RED);
            g.drawString("Failed to load road image.", 50, 50);
        }

        if (carImage != null) {
            // Vẽ xe
            g.drawImage(carImage, carX, carY, carWidth, carHeight, null);
        } else {
            g.setColor(Color.RED);
            g.drawString("Failed to load car image.", 50, 100);
        }

        // Vẽ vật cản
        for (Obstacle obs : obstacles) {
            if (obs.image != null) {
                g.drawImage(obs.image, obs.x, obs.y, obs.width, obs.height, null);
            }
        }

        // Hiển thị tốc độ
        g.setColor(Color.BLACK);
        g.drawString("Speed: " + currentScrollSpeed, 10, 20);
    }

    // Điều khiển tốc độ đường cuộn và vật cản
    @Override
    public void actionPerformed(ActionEvent e) {
        roadY1 += currentScrollSpeed;
        roadY2 += currentScrollSpeed;

        if (roadY1 >= SCREEN_HEIGHT) roadY1 = roadY2 - SCREEN_HEIGHT;
        if (roadY2 >= SCREEN_HEIGHT) roadY2 = roadY1 - SCREEN_HEIGHT;

        // Sinh chướng ngại vật mới
        int obstacleSpawnChance = Math.max(60 - (int) currentScrollSpeed / 5, 5);
        if (random.nextInt(obstacleSpawnChance) == 0) {
            int obstacleType = random.nextInt(obstacleImages.length);
            obstacles.add(new Obstacle(
                    (SCREEN_WIDTH - ROAD_WIDTH) / 2 + random.nextInt(ROAD_WIDTH - 50),
                    -50,
                    50,
                    50,
                    obstacleImages[obstacleType]
            ));
        }

        // Xử lý va chạm và cập nhật vị trí vật cản
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle obs = obstacles.get(i);
            obs.y += currentScrollSpeed;

            // Kiểm tra va chạm
            if (obs.intersects(carX, carY, carWidth, carHeight)) {
                gamePanel.playSE(15);
                gamePanel.stopMusic();
                gamePanel.playMusic(18);
                gameOver(); // Gọi gameOver nếu va chạm
                return;
            }

            if (obs.y > SCREEN_HEIGHT) {
                obstacles.remove(i);
            }
        }

        repaint();
    }

    // Xử lý kết thúc game (Chuyển cảnh sử dụng SceneTransition)
    private void gameOver() {
        gameLoop.stop();
        movementTimer.stop();
        speedIncreaseTimer.stop();
    
        // Tạo hiệu ứng chuyển cảnh có thoại
        SceneTransition transitionPanel = new SceneTransition(true); // Hiển thị thoại
        parentFrame.getContentPane().removeAll(); // Xóa nội dung hiện tại
        parentFrame.add(transitionPanel); // Thêm SceneTransition vào JFrame
        parentFrame.revalidate();
        parentFrame.repaint();
    
        // Gắn callback để chuyển sang GamePanel_chap1 sau hiệu ứng
        transitionPanel.setMapChangeListener(() -> {
            parentFrame.getContentPane().removeAll(); // Xóa SceneTransition
            GamePanel_chap1 chap1 = new GamePanel_chap1(); // Tạo chap1
            parentFrame.add(chap1); // Thêm chap1 vào JFrame
    
            chap1.requestFocusInWindow(); // Đảm bảo chap1 nhận focus để điều khiển nhân vật
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    
        // Bắt đầu hiệu ứng chuyển cảnh
        transitionPanel.startTransition();
    }
    
    
    // Điều khiển bàn phím
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            moveLeft = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    // Lớp đại diện cho vật cản
    private class Obstacle {
        int x, y, width, height;
        BufferedImage image;

        Obstacle(int x, int y, int width, int height, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }

        boolean intersects(int carX, int carY, int carWidth, int carHeight) {
            return x < carX + carWidth &&
                    x + width > carX &&
                    y < carY + carHeight &&
                    y + height > carY;
        }
    }
}
