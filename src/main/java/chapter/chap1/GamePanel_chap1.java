package chapter.chap1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel_chap1 extends JPanel implements Runnable, KeyListener {
    // Screen and Rendering Properties
    public final int ScreenHeight = 840;
    public final int ScreenWidth = 960;
    private final int tileSize = 25;
    private final int speed = 20;

    // Camera and Background Properties
    private int cameraX = 20;
    private int cameraY = 1500;
    private int preCamX = cameraX;
    private int preCamY = cameraY;
    private int BGWidth = 3302;
    private int BGHeight = 2347;

    // Character Positioning
    private int DefaultX = ScreenHeight / 2 - tileSize / 2;
    private int DefaultY = ScreenWidth / 2 - tileSize / 2;

    // Game State Variables
    private int key = -1;
    private char stand = 'f';
    private boolean isDialogueActive = false;

    //suy nghĩ
    private String suynghi = " ";
    private boolean showSuynghi = false;
    private  javax.swing.Timer suynghiTimer;
    private int buocsuynghi = 0;
    private String[] cacsuynghi = {
        "Lúc nào cũng là những giấc mơ quái dị đó",
        "Ngủ trong lớp mà cũng gặp, mệt mỏi quá",
        "May mà thầy Hóa dễ nên không bị mắng",
        "Điểm danh xong rồi, lên thư viện ngủ tiếp vậy",

    };

    // Game Objects
    private BufferedImage backgroundImage,girl_Image;
    Rectangle hcnTest, Girl_Rec;
    Area polyArea;
    Area rectArea;
    Rock_chap1 r1;
    
    // Animations
    Animation UP, DOWN, LEFT, RIGHT, STAND_FRONT, STAND_BACK, STAND_LEFT, STAND_RIGHT;

    // Dialogue System
    private DialogueManager dialogueManager;

    // Game Thread
    Thread thread;

    public GamePanel_chap1() {
        Suynghitrongem();
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        
        // Initialize Background
        try {
            backgroundImage = ImageIO.read(new File("pic/BackGround.png"));
            girl_Image = ImageIO.read(new File("pic/GIRl_CHAR_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize Character
        r1 = new Rock_chap1(68, 708);

        // Initialize Dialogue System
        List<Dialogue> dialogues = loadDialogues();
        dialogueManager = new DialogueManager(dialogues);

        // Setup Panel
        this.setFocusable(true);
        this.addKeyListener(this);

        // Initialize Game Thread
        thread = new Thread(this);
        thread.start();

        // Initialize Animations
        initializeAnimations();
    }

    private void Suynghitrongem() {
        suynghiTimer = new Timer (2000, e -> {
            if (buocsuynghi < cacsuynghi.length){
                suynghi = cacsuynghi[buocsuynghi];
                showSuynghi = true;
                repaint();
                buocsuynghi++;

            }else{
                showSuynghi = false;
                suynghiTimer.stop();

            }
        });
        suynghiTimer.setInitialDelay(0);
        suynghiTimer.start();
    }

    private void initializeAnimations() {
        UP = new Animation("character_move_up", 4);
        DOWN = new Animation("character_move_down", 4);
        LEFT = new Animation("character_move_left", 4);
        RIGHT = new Animation("character_move_right", 4);
        STAND_FRONT = new Animation("character_stand_front", 3);
        STAND_BACK = new Animation("character_stand_back", 3);
        STAND_LEFT = new Animation("character_stand_left", 3);
        STAND_RIGHT = new Animation("character_stand_right", 3);

        // Preload animation images
        UP.getImage(); DOWN.getImage(); LEFT.getImage(); RIGHT.getImage();
        STAND_RIGHT.getImage(); STAND_BACK.getImage(); STAND_LEFT.getImage(); STAND_FRONT.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //System.out.println(r1.x + " " + r1.y + " " + " " + cameraX + " " + cameraY);
        // Draw Background
        if (backgroundImage != null) {
            g.drawImage(
                backgroundImage,
                0, 0, ScreenWidth, ScreenHeight,
                cameraX, cameraY, cameraX + ScreenWidth, cameraY + ScreenHeight,
                this
            );
        }

        g.drawImage(girl_Image, 2400 - cameraX ,2088 - cameraY, 120, 90, null);
        Girl_Rec = new Rectangle(2340 - cameraX ,2118 - cameraY, 200, 100);

        drawCharacterAnimation(g);

        // Draw Dialogue if Active
        if (isDialogueActive) {
            dialogueManager.draw(g, ScreenWidth, ScreenHeight);
        }
        if(showSuynghi){
            g.setColor(new Color(0,0,0,150));
            g.fillRoundRect(r1.x - 40, r1.y - 40 , 300, 40, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN,14));
            g.drawString(suynghi,r1.x - 30, r1.y - 20);
        }
    }

    private void drawCharacterAnimation(Graphics g) {
        if (key == KeyEvent.VK_DOWN) DOWN.operation(g, r1.x, r1.y);
        else if (key == KeyEvent.VK_UP) UP.operation(g, r1.x, r1.y);
        else if (key == KeyEvent.VK_LEFT) LEFT.operation(g, r1.x, r1.y);
        else if (key == KeyEvent.VK_RIGHT) RIGHT.operation(g, r1.x, r1.y);
        else {
            if (stand == 'f') STAND_FRONT.operation(g, r1.x, r1.y);
            else if (stand == 'b') STAND_BACK.operation(g, r1.x, r1.y);
            else if (stand == 'l') STAND_LEFT.operation(g, r1.x, r1.y);
            else if (stand == 'r') STAND_RIGHT.operation(g, r1.x, r1.y);
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(33); // Approximately 30 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // If dialogue is active, handle dialogue interactions
        if (isDialogueActive) {
            dialogueManager.update(e);
            if (!dialogueManager.isActive()) {
                isDialogueActive = false;
            }
            repaint();
            return;
        }

        // Handle key presses for movement
        key = e.getKeyCode();
        handleMovement(key);

        // Collision detection
        handleCollisions();

        repaint();
    }

    private void handleMovement(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (cameraX > tileSize && r1.x == DefaultX) {
                    cameraX -= speed;
                    stand = 'l';
                } else {
                    r1.diChuyen(key);
                    stand = 'l';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (cameraX < backgroundImage.getWidth() - ScreenWidth - tileSize && r1.x == DefaultX) {
                    cameraX += speed;
                    stand = 'r';
                } else {
                    r1.diChuyen(key);
                    stand = 'r';
                }
                break;
            case KeyEvent.VK_UP:
                if (cameraY > tileSize && r1.y == DefaultY) {
                    cameraY -= speed;
                    stand = 'b';
                } else {
                    r1.diChuyen(key);
                    stand = 'b';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (cameraY < backgroundImage.getHeight() - ScreenHeight - tileSize && r1.y == DefaultY) {
                    cameraY += speed;
                    stand = 'f';
                } else {
                    r1.diChuyen(key);
                    stand = 'f';
                }
                break;
        }
    }
    // xử lý va chạm 
    private void handleCollisions() {
        // Library collision area
        hcnTest = new Rectangle(2040 - cameraX, 766 - cameraY, 1012, 1392);
        
        // Complex polygon area
        Polygon poly = new Polygon(
            new int[] {3302 - cameraX, 1958 - cameraX, 1958 - cameraX, 1750 - cameraX, 1750 - cameraX, 1535 - cameraX, 860 - cameraX, 
            1535 - cameraX, 1535 - cameraX, 432 - cameraX, 432 - cameraX, 0 - cameraX, 0 - cameraX, 3302 - cameraX},
            new int[] {471 - cameraY, 471 - cameraY, 561 - cameraY, 561 - cameraY, 620 - cameraY, 620 - cameraY, 1295 - cameraY, 
            1295 - cameraY, 2181 - cameraY, 2181 - cameraY, 2220 - cameraY, 2220 - cameraY, 0 - cameraY, 0 - cameraY},
            14
        );
        
        polyArea = new Area(poly);
        rectArea = new Area(r1.rect);
        polyArea.intersect(rectArea);

        // Collision checks
        if (r1.vaCham(hcnTest) || !polyArea.isEmpty() || r1.x > 908 || r1.y > 793 || r1.x < 33) {
            r1.luiLai();
            cameraX = preCamX;
            cameraY = preCamY;

        } else {
            preCamX = cameraX;
            preCamY = cameraY;
            r1.preX = r1.x;
            r1.preY = r1.y;
        }

        // Girl interaction
        if (r1.vaCham(Girl_Rec)) {
            System.out.println("Girl");
            // Trigger dialogue on collision
            isDialogueActive = true;
            dialogueManager.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        key = -1;
    }
   

    // Dialogue initialization method
    private List<Dialogue> loadDialogues() {
        List<Dialogue> dialogues = new ArrayList<>();
        dialogues.add(new Dialogue(
            "linh_start",
            "Đạt, là cậu đấy à? Trông có vẻ kém quá, tớ không nhận ra luôn!",
            List.of(
                new Response("Ơ, cậu là ai vậy?", "linh_reveal_identity"),
                new Response("Kém chỗ nào? Cậu là ai mà nói thế?", "linh_reveal_identity")
            )
        ));
        dialogues.add(new Dialogue(
            "linh_reveal_identity",
            "Tớ là Linh đây, bạn cũ của cậu. Quên tớ rồi à?",
            List.of(
                new Response("À, chào Linh! Lâu rồi không gặp.", "linh_continue_casual1"),
                new Response("Haha, làm sao mà quên được. Dạo này cậu thế nào?", "linh_continue_casual1")
            )
        ));
        dialogues.add(new Dialogue(
            "linh_continue_casual1",
            "Tớ vẫn khỏe, dạo này học hành thế nào, mình cùng lớp OOP này mà!",
            List.of(
                new Response("Ngon rồi, lại được làm bạn đồng hành như xưa.", "linh_continue_casual2"),
                new Response("Dạo này hơi mệt mỏi chút xíu!", "linh_continue_casual2")
            )
        ));

        // Linh tiếp tục câu chuyện
        dialogues.add(new Dialogue(
            "linh_continue_casual2",
            "Ừ, trông cậu hơi mệt đấy.",
            List.of(
                new Response("Mệt lắm à, dạo này tớ hay gặp ác mộng.", "linh_continue_casual3"),
                new Response("Vấn đề tâm lý một chút thôi mà!", "linh_continue_casual3")
            )
        ));

        // Linh phản ứng khi Đạt đùa cạnh tranh
        dialogues.add(new Dialogue(
            "linh_continue_casual3",
            "Tâm lý à, tớ mới học qua khóa tâm lý học 5 phút của thầy Trương Viết Bạn đây, kể tớ nghe xem sao.",
            List.of(
                new Response("Vì chuyện cũ ngày xưa ấy mà.", "linh_talk_about_nightmares"),
                new Response("Tớ bị ám ảnh bởi quá khứ, từng gây tai nạn đấy.", "linh_talk_about_nightmares")
            )
        ));

        // Tiếp tục hội thoại thân thiện
        dialogues.add(new Dialogue(
            "linh_talk_about_nightmares",
            "Nhưng cụ thể là như thế nào, mơ thấy OOP bị tạch à hay sao?",
            List.of(
                new Response("Tớ hay mơ thấy những con quái vật đuổi theo, bắt tớ phải chạy liên tục.", "linh_direct_confession"),
                new Response("Tớ mơ lại giấc mơ về tai nạn ngày xưa, mọi thứ quá chân thật khiến tớ hoảng sợ.", "linh_direct_confession")
            )
        ));

        // Hội thoại về ác mộng
        dialogues.add(new Dialogue(
            "linh_direct_confession",
            "Nghe ghê quá vậy, chắc là cậu đang gặp áp lực lớn đấy.",
            List.of(
                new Response("Mẹ cậu bây giờ khỏe rồi chứ? Ngày xưa cô ấy bị tai nạn mà.", "linh_ask_about_mother"),
                new Response("Còn mẹ cậu thế nào, lâu rồi tớ chưa qua chơi nhà nữa.", "linh_ask_about_mother")
            )
        ));

        // Linh gợi ý gặp bác sĩ tâm lý
        dialogues.add(new Dialogue(
            "linh_ask_about_mother",
            "Mẹ tớ bây giờ khỏe rồi, nhưng tớ vẫn ghét đứa gây ra tai nạn hồi đó lắm.",
            List.of(
                new Response("...Vậy à.Nếu đứa đó là tớ thì cậu có giận không", "linh_answer_about_mother"),
                new Response("Thực ra, chính tớ là người gây ra tai nạn đó. Tớ xin lỗi.", "linh_answer_about_mother")
            )
        ));

        // Linh phản ứng về mẹ
        dialogues.add(new Dialogue(
            "linh_answer_about_mother",
            "Là cậu sao? Tại sao hồi đó cậu lại bỏ chạy vậy?",
            List.of(
                new Response("Lúc đó tớ quá hoảng sợ, cậu tha lỗi cho tớ nhé.", "linh_response_about_accident"),
                new Response("Tớ xin lỗi, cậu tha thứ cho tớ nhé.", "linh_response_about_accident")
            )
        ));

        // Linh phản ứng về tai nạn
        dialogues.add(new Dialogue(
            "linh_response_about_accident",
            "Thực ra, tớ cũng thầm cảm ơn tai nạn đó. Nếu không có, mẹ tớ đã không bỏ chuyến bay gặp khủng bố hôm ấy rồi... Cũng nhờ cậu mà thôi, tớ tha thứ cho cậu đấy.",
            List.of(
                new Response("Vậy sao, thì ra cậu cũng trút bỏ được phần nào gánh nặng.", "linh_direct_confession"),
                new Response("Tớ hiểu rồi. Cảm ơn cậu.", "linh_direct_confession")
            )
        ));

        // Đạt tỏ tình
        dialogues.add(new Dialogue(
            "linh_direct_confession",
            "Thôi, dù sao cậu cũng là bạn thân của tớ mà, bỏ qua đi. Lên thư viện ngồi học một chút nhé.",
            List.of(
                new Response("Còn có một chuyện tớ muốn nói với cậu.", "linh_response_to_love")
            )
        ));

        // Linh đồng ý
        dialogues.add(new Dialogue(
            "linh_response_to_love",
            "Chuyện gì nữa vậy?",
            List.of(
                new Response("Tớ thích cậu từ lâu rồi, nhưng vì chuyện ngày xưa tớ làm mẹ cậu bị tai nạn nên tớ không dám nói. Nhưng bây giờ thì khác rồi, cậu làm người yêu tớ nhé?", "end_game")
            )
        ));

        // Kết thúc game
        dialogues.add(new Dialogue(
            "end_game",
            "Đồ ngốc này, tớ cũng thích cậu từ lâu rồi mà, hahaha.",
            List.of()
        ));


        return dialogues;
    }
}