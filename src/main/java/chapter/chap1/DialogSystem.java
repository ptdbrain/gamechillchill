package chapter.chap1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class DialogSystem {
    private String[] messages; // Danh sách câu thoại
    private String[] options; // Các lựa chọn
    private int currentMessage = 0; // Câu thoại hiện tại
    private int currentOption = 0; // Lựa chọn hiện tại
    private boolean isActive = false; // Trạng thái hiển thị hộp thoại

    public void start(String[] messages, String[] options) {
        this.messages = messages;
        this.options = options;
        this.currentMessage = 0;
        this.currentOption = 0;
        this.isActive = true;
    }

    public void update(KeyEvent e) {
        if (!isActive) return;

        int key = e.getKeyCode();
        if (options != null) {
            if (key == KeyEvent.VK_UP && currentOption > 0) {
                currentOption--; // Chọn tùy chọn trên
            } else if (key == KeyEvent.VK_DOWN && currentOption < options.length - 1) {
                currentOption++; // Chọn tùy chọn dưới
            } else if (key == KeyEvent.VK_ENTER) {
                // Xử lý lựa chọn (tùy thuộc vào logic)
                if (currentMessage == messages.length - 1) {
                    // Kết thúc hội thoại nếu đây là câu cuối
                    isActive = false;
                } else {
                    // Chuyển đến câu tiếp theo
                    currentMessage++;
                    currentOption = 0;
                }
            }
        }
    }

    public void draw(Graphics g, int screenWidth, int screenHeight) {
        if (!isActive) return;

        // Vẽ hộp thoại
        g.setColor(Color.BLACK);
        g.fillRect(50, screenHeight - 200, screenWidth - 100, 150);
        g.setColor(Color.WHITE);
        g.drawRect(50, screenHeight - 200, screenWidth - 100, 150);

        // Vẽ câu thoại
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(messages[currentMessage], 70, screenHeight - 170);

        // Vẽ các lựa chọn
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (i == currentOption) {
                    g.setColor(Color.YELLOW); // Lựa chọn được chọn
                } else {
                    g.setColor(Color.WHITE);
                }
                g.drawString(options[i], 70, screenHeight - 120 + i * 30);
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
