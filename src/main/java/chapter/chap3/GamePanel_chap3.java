package chapter.chap3;

import chapter.chap0.src.main.GamePanel;
import game.SceneTransition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel_chap3 extends JPanel implements KeyListener {
    private SceneChangeListener sceneChangeListener;
    public interface SceneChangeListener {
        void onSceneChange();
    }

    public void setSceneChangeListener(SceneChangeListener listener) {
        this.sceneChangeListener = listener;
    }
    //suy nghĩ
    private String suynghi = " ";
    private boolean showSuynghi = false;
    private  javax.swing.Timer suynghiTimer;
    private int buocsuynghi = 0;
    private String[] cacsuynghi = {
        "Sao mình lại ở trong nhà xe C7 thế này",
        "À, chắc mình đang chuẩn bị về nhà. Lối ra ở ngay kia rồi",
        "Mình chỉ cần đẩy hết những chiếc xe trước mặt này ra là được",

    };
    //ảnh
    private BufferedImage backgroundImage, bikeImage, character, character_1, muiTen;
    private Image bikImage_scaled, backgroundImage_scaled, character_scaled, character_scaled_right;
    private Image character_1_scaled, preImage, muiTen_scaled;
    public int check = 0;
    final int TypeSize = 12;
    final int scale = 10;
    final int tileSize = TypeSize * scale;
    final int maxScreenCol = 8;
    final int maxScreenRow = 7;
    final int ScreenWidth = tileSize * maxScreenCol;
    final int ScreenHeight = tileSize * maxScreenRow;
    private int x = 0 * tileSize;  // Vị trí ban đầu của nhân vật (trục X)
    private int y = 1 * tileSize;  // Vị trí ban đầu của nhân vật (trục Y)
    private int preX = x;
    private int preY = y;
    public int key = -1;
    boolean winCheck = false;
    Timer timer;
    Rectangle rect1 = new Rectangle(0 * tileSize, 0 * tileSize, 8 * tileSize, tileSize);
    Rectangle rect2 = new Rectangle(5 * tileSize, 1 * tileSize, 3 * tileSize, 1 * tileSize);
    Rectangle rect3 = new Rectangle(7 * tileSize, 2 * tileSize, 1 * tileSize, 1 * tileSize);
    Rectangle rect4 = new Rectangle(0 * tileSize, 5 * tileSize, 1 * tileSize, tileSize);
    Rectangle rect5 = new Rectangle(0 * tileSize, 6 * tileSize, 8 * tileSize, tileSize);
    Rectangle rect6 = new Rectangle(6 * tileSize, 5 * tileSize, 2 * tileSize, 1 * tileSize);
    Rectangle rect7 = new Rectangle(1 * tileSize, 1 * tileSize, 1 * tileSize, 1 * tileSize);
    Rectangle rectA = new Rectangle(x, y, tileSize, tileSize);

    Rock_chap3[] r = {
        new Rock_chap3(0, 3),
        new Rock_chap3(1, 2),
        new Rock_chap3(1, 4),
        new Rock_chap3(2, 3),
        new Rock_chap3(2, 5),
        new Rock_chap3(3, 2),
        new Rock_chap3(3, 4),
        new Rock_chap3(4, 1),
        new Rock_chap3(4, 3),
        new Rock_chap3(4, 5),
        new Rock_chap3(5, 2),
        new Rock_chap3(5, 3),
        new Rock_chap3(5, 4),
        new Rock_chap3(6, 4)
    };

    public static GamePanel gamePanel = new GamePanel();

    public GamePanel_chap3() {
        try {
            backgroundImage = ImageIO.read(new File("pic/NEWBG3.png")); 
            bikeImage = ImageIO.read(new File("pic/xe_may.png"));
            character = ImageIO.read(new File("pic/character.png")); 
            character_1 = ImageIO.read(new File("pic/character_1.png")); 
            muiTen = ImageIO.read(new File("pic/Arrow.png"));
            muiTen_scaled = muiTen.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            character_1_scaled = character_1.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            character_scaled = character.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            character_scaled_right = character.getScaledInstance(-1 * tileSize, tileSize, Image.SCALE_SMOOTH);
            bikImage_scaled = bikeImage.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);

            backgroundImage_scaled = backgroundImage.getScaledInstance(ScreenWidth, ScreenHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Suynghitrongem();
        this.setPreferredSize(new Dimension(tileSize * maxScreenCol, tileSize * maxScreenRow));
        //this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocus();

        gamePanel.playMusic(12);

        timer = new Timer(33, e -> repaint());
        timer.start();
    }
    private void Suynghitrongem() {
        suynghiTimer = new Timer (2000, e -> {
            if (buocsuynghi < cacsuynghi.length){
                suynghi = cacsuynghi[buocsuynghi];
                showSuynghi = true;
                //repaint();
                buocsuynghi++;

            }else{
                showSuynghi = false;
                suynghiTimer.stop();

            }
        });
        suynghiTimer.setInitialDelay(0);
        suynghiTimer.start();
    }
    public boolean chamTuong(Rectangle rectA) {
        if (rect1.intersects(rectA) || rect2.intersects(rectA) || rect3.intersects(rectA) || 
                rect4.intersects(rectA) || rect5.intersects(rectA) || rect6.intersects(rectA) || rect7.intersects(rectA)) {
            return true;
            
        }
        else return false;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage_scaled, 0, 0, null);
        drawCharacter(g, key);
        // 7 3
        g.drawImage(muiTen_scaled, 7 *tileSize, 3 *tileSize, null);
        //g.drawImage(character_scaled, x, y, null); // Vẽ nhân vật tại (x, y)
        for (int i = 0; i < 14; i++) r[i].draw(g, bikImage_scaled);
        if(showSuynghi){
            g.setColor(new Color(0,0,0,150));
            g.fillRoundRect(x + 70, y - 10 , 450, 40, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN,15));
            g.drawString(suynghi,x + 80, y + 17);
            
        }
    }
    public void drawCharacter(Graphics g, int key) {
        if (key == -1 || key == KeyEvent.VK_LEFT) {
            g.drawImage(character_scaled, x, y, null);
            preImage = character_scaled;
        } else if (key == KeyEvent.VK_RIGHT) {
            g.drawImage(character_1_scaled, x, y, null);
            preImage = character_1_scaled;
        } else {
            g.drawImage(preImage, x, y, null);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
        preX = x;
        preY = y;

        switch (key) {
            case KeyEvent.VK_R:
                resetGame();
                break;
            case KeyEvent.VK_LEFT:
                if (x > 0) x -= tileSize;
                break;
            case KeyEvent.VK_RIGHT:
                if (x < (tileSize * maxScreenCol) - tileSize) x += tileSize;
                break;
            case KeyEvent.VK_UP:
                if (y > 0) y -= tileSize;
                break;
            case KeyEvent.VK_DOWN:
                if (y < (tileSize * maxScreenRow) - tileSize) y += tileSize;
                break;
        }

        rectA.setLocation(x, y);
        if (chamTuong(rectA)) revertPosition();
        handleRockCollision();
        checkWinCondition();
    }

    private void resetGame() {
        x = 0 * tileSize;
        y = 1 * tileSize;
        preX = x;
        preY = y;
        key = -1;
        for (int i=0; i<14; i++) {
            r[i].x = r[i].DefaultX;
            r[i].y = r[i].DefaultY;
            r[i].rect.setLocation(r[i].x, r[i].y);
        }
    }

    private void revertPosition() {
        x = preX;
        y = preY;
        rectA.setLocation(x, y);
    }

    private void handleRockCollision() {
        for (int i = 0; i < r.length; i++) {
            if (r[i].vaCham(rectA)) {
                revertPosition();
                r[i].diChuyen(key);
                gamePanel.playSE(13);
                if (chamTuong(r[i].rect)) r[i].luiLai();

                for (int j = 0; j < r.length; j++) {
                    if (i != j && r[i].vaCham(r[j].rect)) {
                        r[i].luiLai();
                        break;
                    }
                }
            }
        }
    }

    private void checkWinCondition() {
        // Kiểm tra nếu tọa độ phù hợp để thắng
        if (x == 7 * tileSize && (y == 3 * tileSize || y == 4 * tileSize) || key == KeyEvent.VK_Z) {
            System.out.println("YOU WIN");
            
            // Gọi chuyển cảnh sang game đua xe nếu có SceneChangeListener
            if (sceneChangeListener != null) {
                gamePanel.stopMusic();
                sceneChangeListener.onSceneChange();
            } else {
                System.err.println("SceneChangeListener is null! Cannot change scene.");
            }
        }
    }
    

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
}
