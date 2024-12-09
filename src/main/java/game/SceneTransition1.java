package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SceneTransition1 extends JPanel {
    private float alpha = 0.0f; // Độ mờ (0.0 là trong suốt, 1.0 là tối hoàn toàn)
    private boolean isFadingIn = true; // Hiệu ứng fade-in
    private boolean isShowingDialog = false; // Hiển thị đoạn hội thoại
    private boolean showDialog = true; // Kiểm soát có hiển thị thoại hay không
    private String[] dialogLines = {
        "Thắng rồi !!!",
        "Ơ chỉ là mơ sao",
        "Chưa gì đã sáng rồi",
        "Lên lớp rồi ngủ tiếp vậy"
    };
    private int dialogIndex = 0; // Dòng hội thoại hiện tại
    private int dialogDisplayTime = 0; // Thời gian hiển thị dòng thoại
    private int maxDialogDisplayTime = 40; // Thời gian (số bước Timer) để giữ mỗi dòng thoại
    private Timer timer; // Timer để điều khiển hiệu ứng
    private MapChangeListener mapChangeListener; // Callback để chuyển cảnh

    public SceneTransition1(boolean showDialog) {
        this.showDialog = showDialog; // Gán giá trị để quyết định có hiển thị thoại không
        setOpaque(false); // Đảm bảo nền trong suốt
    }

    // Bắt đầu hiệu ứng chuyển cảnh
    public void startTransition() {
        timer = new Timer(40, e -> { // Timer chạy mỗi 40ms
            if (isFadingIn) {
                alpha += 0.05f; // Tăng độ mờ
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    isFadingIn = false; // Dừng hiệu ứng fade-in
                    if (showDialog) {
                        isShowingDialog = true; // Bắt đầu hiển thị thoại nếu được phép
                    } else {
                        if (mapChangeListener != null) {
                            mapChangeListener.onMapChange(); // Chuyển cảnh ngay nếu không hiển thị thoại
                            timer.stop();
                        }
                    }
                }
            } else if (isShowingDialog) {
                dialogDisplayTime++;
                if (dialogDisplayTime >= maxDialogDisplayTime) {
                    dialogDisplayTime = 0; // Reset thời gian hiển thị
                    dialogIndex++;
                    if (dialogIndex >= dialogLines.length) {
                        isShowingDialog = false; // Kết thúc hội thoại
                        if (mapChangeListener != null) {
                            mapChangeListener.onMapChange(); // Gọi callback để chuyển cảnh
                        }
                        timer.stop();
                    }
                }
            }
            repaint(); // Vẽ lại giao diện
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Vẽ hiệu ứng mờ dần
        g2d.setColor(new Color(0, 0, 0, Math.min(alpha, 1.0f))); // Màn hình tối dần
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Hiển thị đoạn hội thoại nếu được phép
        if (isShowingDialog && dialogIndex < dialogLines.length) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String line = dialogLines[dialogIndex];
            FontMetrics metrics = g2d.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(line)) / 2; // Căn giữa dòng chữ
            int y = getHeight() / 2; // Hiển thị ở giữa màn hình
            g2d.drawString(line, x, y);
        }
    }

    // Gắn callback để chuyển cảnh
    public void setMapChangeListener(MapChangeListener listener) {
        this.mapChangeListener = listener;
    }

    // Interface callback
    public interface MapChangeListener {
        void onMapChange();
    }
}
