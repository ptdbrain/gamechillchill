package game;

import chapter.chap0.src.main.GamePanel;
import chapter.chap1.GamePanel_chap1;
import chapter.chap3.GamePanel_chap3;
import chapter.chap3.RacingGame;
import chapter.chap0.src.main.GamePanel;
import javax.swing.*;

public class MAIN {
    public static JFrame frame;
    public static GamePanel gamePanel = new GamePanel();
    public static void main(String[] args) {
        frame = new JFrame("8th Year Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 840);
        frame.setLocationRelativeTo(null);

        // Bắt đầu với game đánh quái
        startStoryIntro();
        gamePanel.playMusic(0);

        frame.setVisible(true);
    }
    public static void startStoryIntro() {
        frame.getContentPane().removeAll();

        StoryIntroAndTransition storyIntro = new StoryIntroAndTransition();
        frame.add(storyIntro);

        // Tạo Timer để kiểm tra trạng thái của màn hình mở đầu
        Timer checkTransitionTimer = new Timer(100, null);
        checkTransitionTimer.addActionListener(e -> {
            if (storyIntro.isTransitionComplete()) {
                checkTransitionTimer.stop();
                // Chuyển sang game đánh quái sau khi màn hình mở đầu kết thúc
                startGameQuai();
            }
        });

        // Bắt đầu chuyển cảnh và timer kiểm tra
        // Không cần gọi startTransition() nữa vì chúng ta đã bắt đầu quá trình trong StoryIntroAndTransition
        checkTransitionTimer.start();

        storyIntro.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();
    }

    // Bắt đầu game đánh quái
    public static void startGameQuai() {
        frame.getContentPane().removeAll();

        //GamePanel gamePanel = new GamePanel(); // Game đánh quái
        frame.add(gamePanel);
        // Gắn callback để chuyển sang game mê cung
        gamePanel.setGameCompleteCallback(() -> switchToMazeGame());
        gamePanel.setupGame();
        gamePanel.startgameThread();
        gamePanel.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();

    }

    // Chuyển sang game mê cung
    public static void switchToMazeGame() {
    // Xóa tất cả các thành phần hiện có trên frame
    frame.getContentPane().removeAll();

    // Tạo hiệu ứng SceneTransition với đoạn hội thoại
    SceneTransition1 transition = new SceneTransition1(true); // Hiển thị đoạn hội thoại
    transition.setMapChangeListener(() -> {
        // Sau khi hiệu ứng kết thúc, chuyển sang màn hình game mê cung
        frame.getContentPane().removeAll();

        GamePanel_chap3 mazeGame = new GamePanel_chap3(); // Map mê cung
        frame.add(mazeGame);

        // Gắn callback để chuyển sang game đua xe
        mazeGame.setSceneChangeListener(() -> switchToRacingGame());

        // Lấy quyền focus
        mazeGame.requestFocusInWindow();

        frame.revalidate();
        frame.repaint();
    });

    // Thêm hiệu ứng SceneTransition vào frame
    frame.add(transition);
    frame.revalidate();
    frame.repaint();

    // Bắt đầu hiệu ứng chuyển cảnh
    transition.startTransition();

    // Dừng game hiện tại
    if (gamePanel != null) {
        gamePanel.stopGame();
    }
}


    // Chuyển sang game đua xe
    public static void switchToRacingGame() {
        frame.getContentPane().removeAll();

        RacingGame racingGame = new RacingGame(frame); // Game đua xe
        frame.add(racingGame);

        // Gắn callback để chuyển sang chap1
        racingGame.setGameOverCallback(() -> switchToChap1());
        racingGame.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();
    }

    // Chuyển sang chap1
    public static void switchToChap1() {
        frame.getContentPane().removeAll();

        GamePanel_chap1 chap1 = new GamePanel_chap1(); // Chap1
        frame.add(chap1);

        chap1.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();
    }
}
