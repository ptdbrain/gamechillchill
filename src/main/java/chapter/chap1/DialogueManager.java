package chapter.chap1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

public class DialogueManager {
    private List<Dialogue> dialogues; // Danh sách tất cả hội thoại
    private Dialogue currentDialogue; // Hội thoại hiện tại
    private int currentOption = 0; // Tùy chọn được chọn hiện tại

    public DialogueManager(List<Dialogue> dialogues) {
        this.dialogues = dialogues;
        this.currentDialogue = dialogues.get(0); // Bắt đầu từ hội thoại đầu tiên
    }

    public boolean isActive() {
        return currentDialogue != null;
    }

    public void start() {
        currentDialogue = dialogues.get(0); // Khởi động hội thoại từ đầu
        currentOption = 0;
    }

    public void update(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && currentOption > 0) {
            currentOption--;
        } else if (key == KeyEvent.VK_DOWN && currentOption < currentDialogue.getResponses().size() - 1) {
            currentOption++;
        } else if (key == KeyEvent.VK_ENTER) {
            // Chuyển đến hội thoại tiếp theo
            String nextId = currentDialogue.getResponses().get(currentOption).getNext();
            currentDialogue = dialogues.stream()
                    .filter(d -> d.getId().equals(nextId))
                    .findFirst()
                    .orElse(null);
            currentOption = 0;
        }
    }

    public void draw(Graphics g, int screenWidth, int screenHeight) {
        if (currentDialogue == null) return;

        // Vẽ hộp thoại
        g.setColor(Color.BLACK);
        g.fillRect(50, screenHeight - 200, screenWidth - 100, 150);
        g.setColor(Color.WHITE);
        g.drawRect(50, screenHeight - 200, screenWidth - 100, 150);

        // Vẽ nội dung hội thoại
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(currentDialogue.getText(), 70, screenHeight - 170);

        // Vẽ các tùy chọn
        List<Response> responses = currentDialogue.getResponses();
        for (int i = 0; i < responses.size(); i++) {
            if (i == currentOption) {
                g.setColor(Color.YELLOW); // Lựa chọn được chọn
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(responses.get(i).getText(), 70, screenHeight - 120 + i * 30);
        }
    }
}
