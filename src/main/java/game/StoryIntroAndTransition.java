package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class StoryIntroAndTransition extends JPanel {
    private int index = 0; // Track the current part of the story
    private int charIndex = 0; // Track the current character being displayed
    private float alpha = 0.0f; // Fade effect alpha
    private boolean isFadingIn = true;
    private String displayedText = ""; // Text being displayed currently
    private Timer typingTimer; // Timer for typing effect
    private boolean isStoryFinished = false;
    private boolean transitionComplete = false;  // Transition complete state
    private String[] storyParts = {
        "Tôi là Đạt",
        
        "một chàng trai từng được gọi là ‘con nhà người ta’ trong truyền thuyết.",
        
        "Cũng đúng thôi, tôi học giỏi, đẹp trai, nhà giàu",

        " Bạn đừng nghĩ tôi là thằng nhà giàu chỉ biết phá làng phá xóm , ",
        
        "Tôi là sinh viên Đại học Bách Khoa – nơi toàn các thiên tài.",
        
        "Hồi nhỏ tôi có cô bạn thân",

        "Cô ấy tên Uvuvwevwevwe Onyetenyevwe Ugwemubwem Osas.",
        
        "Cô ấy xinh gái, tốt bụng, thông minh",
        
        "Chúng tôi lúc nào cũng dính lấy nhau từ việc học bài đến quậy phá",

        "Tôi đã có tình cảm với cô ấy từ lúc nào không hay",
        
        "Nhưng tuổi trẻ mà, ",
        
        "đâu phải lúc nào cũng khôn ngoan.",

        "Lên Đại học, tôi bị nhiễm phong cách boi phố",
        
        "Tôi bị lôi kéo vào hội đua xe trái phép",
        
        "tưởng oai phong lẫm liệt, nhưng thực ra chỉ là lũ trẻ trâu đội mũ bảo hiểm giả.",

        "Tôi đi đua xe ngày đêm mà không quan tâm đến an toàn của chính mình",
        
        "Đỉnh điểm là vào một đêm định mệnh, tôi đã gây tai nạn kinh hoàng...",
        
        "Người bị tông… lại chính là mẹ của Uvuvwevwevwe Onyetenyevwe Ugwemubwem Osas.",
        
        "Lúc đó, tôi hoảng loạn đến mức bỏ trốn mà không kịp nghĩ gì. ",
        
        "Lúc đó, tôi đã tưởng tôi là Max Verstappen",
        
        "Tôi không bị công an tóm, nhưng tôi hèn, tôi không dám đi đầu thú",

        "Tôi chỉ biết trốn chui trốn lủi trong nhà",
        
        "Tôi lao vào học hành, chơi game để quên đi tất cả. ",
        
        "Nhưng không, quá khứ như cái bumerang",

        "ném đi bao nhiêu thì nó lại quay về đập vào mặt mình.",
        
        "Đêm nào tôi cũng mơ thấy mình bị quái vật truy đuổi, ",
        
        "hoặc là giấc mơ về cái đêm kinh hoàng đó.",

        "Giấc mơ quá chân thực làm tôi tưởng như mình đã quay ngược thời gian về lúc đó",
        
        "Rồi mỗi khi giật mình tỉnh dậy, tôi chỉ biết tự nhủ: ",

        "Chắc tại bài tập OOP của thầy Hóa khó quá nên bị ác mộng thôi.",
        
        "Cứ thế, ",
        
        "tôi từ một học sinh A+ OOP-trò cưng của thầy Hóa, ",

        "trở thành một đứa mất hồn, tinh thần lúc nào cũng hoảng loạn và chỉ biết vùi đầu vào game. ",
    
        "Trong game ",

        "tôi thường chọn nhân vật anh hùng giải cứu người khác",

        "Nhưng sâu thẳm trong lòng, tôi biết mình chỉ đang chạy trốn thực tại đen tối ",

        "không chỉ khỏi lỗi lầm, mà còn khỏi chính bản thân mình.",
        
        " Liệu tôi có đủ dũng cảm để đối mặt với sai lầm của mình ",

        "Hay tôi sẽ mãi mãi là chàng trai mắc kẹt trong vòng xoáy của tội lỗi và nỗi sợ hãi? ",
        
        "Tôi không biết, nhưng nếu đây là một game, chắc chắn tôi sẽ chọn chế độ chơi dễ…",
        
        "Hãy theo dõi hành trình của tôi để xem liệu tôi có tìm được câu trả lời. ",    
    };

    public StoryIntroAndTransition() {
        setPreferredSize(new Dimension(960, 540));
        setBackground(Color.BLACK);
        setOpaque(true);

        // Handle mouse click to proceed through the story
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isStoryFinished) {
                    transitionComplete = true;  // Mark transition complete
                } else if (charIndex < storyParts[index].length()) {
                    // Skip typing effect and show full text
                    charIndex = storyParts[index].length(); 
                    displayedText = storyParts[index];
                    repaint();
                } else {
                    // Move to the next part of the story
                    index++;
                    if (index < storyParts.length) {
                        charIndex = 0;
                        displayedText = "";
                        startTypingEffect();
                    } else {
                        isStoryFinished = true;
                    }
                    repaint();
                }
            }
        });

        startFadeEffect();
    }

    // Method to start the fade effect
    private void startFadeEffect() {
        SwingWorker<Void, Void> fadeWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Fade in
                while (alpha < 1.0f && !isStoryFinished) {
                    Thread.sleep(50); // Wait for a bit to create the fade effect
                    alpha += 0.05f;
                    repaint();
                }

                // Start typing first part
                startTypingEffect();

                // Wait for story to progress
                while (index < storyParts.length) {
                    Thread.sleep(1000000);  // Wait for 3 seconds before moving to the next part of the story
                    if (charIndex == storyParts[index].length()) {
                        index++;
                        if (index < storyParts.length) {
                            charIndex = 0;
                            displayedText = "";
                            startTypingEffect();
                        }
                    }
                }
                // Fade out
                while (alpha > 0.0f) {
                    Thread.sleep(50); // Wait for a bit to create the fade effect in reverse
                    alpha -= 0.05f;
                    repaint();
                }

                transitionComplete = true;  // Mark the transition as complete
                return null;
            }

            @Override
            protected void done() {
                // Transition to the game when the story finishes
                if (transitionComplete) {
                    startGame();  // Start the game after the transition is complete
                }
            }
        };

        fadeWorker.execute();  // Start the fade effect
    }

    // Method to start the typing effect
    private void startTypingEffect() {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }

        typingTimer = new Timer(20, e -> {
            if (charIndex < storyParts[index].length()) {
                displayedText += storyParts[index].charAt(charIndex);
                charIndex++;
                repaint();
            } else {
                typingTimer.stop();
            }
        });
        typingTimer.start();
    }

    // Check if the transition is complete
    public boolean isTransitionComplete() {
        return transitionComplete;
    }

    // Method to draw the story screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw fade effect
        g2d.setColor(new Color(0, 0, 0, Math.min(alpha, 1.0f)));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the current part of the story
        if (index < storyParts.length) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Serif", Font.PLAIN, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();  // Get the height of each line

            // Calculate the width of the screen and center it
            int x = (getWidth() - fm.stringWidth(displayedText)) / 2;  // Small margin from left edge
            int y = getHeight() / 2;  // Vertically center

            // Wrap text to fit screen width
            String[] words = displayedText.split(" ");
            StringBuilder currentLine = new StringBuilder();
            int lineY = y;

            for (String word : words) {
                String tempLine = currentLine + word + " ";
                if (fm.stringWidth(tempLine) < getWidth() - 40) {  // Check if the line fits within the screen width
                    currentLine.append(word).append(" ");
                } else {
                    // Draw the current line
                    g2d.drawString(currentLine.toString(), x, lineY);
                    currentLine = new StringBuilder(word + " "); // Start a new line
                    lineY += lineHeight;
                }
            }

            // Draw the last line if there is any
            if (currentLine.length() > 0) {
                g2d.drawString(currentLine.toString(), x, lineY);
            }
        }
    }

    // Method to start the game after the story is finished
    private void startGame() {
        // Transition to the game
        //JFrame gameFrame = new JFrame("Game Window");
        // gameFrame.setSize(960, 540);
        // gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // gameFrame.setLocationRelativeTo(null);
        // gameFrame.setVisible(true);

        // Close the story screen when transitioning to the game
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}