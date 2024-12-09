package chapter.chap1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Dialog {
    private String message;
    private int x, y;
    private boolean isVisible;
    private int duration;
    private long startTime;
    private final int DIALOG_WIDTH = 200;
    private final int DIALOG_HEIGHT = 60;
    private final int PADDING = 10;
    
    public Dialog() {
        this.isVisible = false;
        this.duration = 2000; // Hiển thị trong 2 giây
    }
    
    public void show(String message, int characterX, int characterY) {
        this.message = message;
        
        // Tính toán vị trí của khung chat
        // Đặt khung chat phía trên nhân vật
        this.x = characterX - DIALOG_WIDTH/2 + 60; // 60 là một nửa kích thước nhân vật
        this.y = characterY - DIALOG_HEIGHT - 10;
        
        // Đảm bảo khung chat không vượt ra ngoài màn hình
        if (this.x < 0) this.x = 0;
        if (this.y < 0) this.y = 0;
        
        this.isVisible = true;
        this.startTime = System.currentTimeMillis();
    }
    
    public void update() {
        if (isVisible) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > duration) {
                isVisible = false;
            }
        }
    }
    
    public void draw(Graphics g) {
        if (!isVisible) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ khung chat với độ trong suốt
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        
        // Vẽ nền khung chat
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, DIALOG_WIDTH, DIALOG_HEIGHT, 15, 15);
        
        // Vẽ viền khung chat
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, DIALOG_WIDTH, DIALOG_HEIGHT, 15, 15);
        
        // Vẽ mũi tên chỉ xuống nhân vật
        int[] xPoints = {x + DIALOG_WIDTH/2 - 10, x + DIALOG_WIDTH/2 + 10, x + DIALOG_WIDTH/2};
        int[] yPoints = {y + DIALOG_HEIGHT, y + DIALOG_HEIGHT, y + DIALOG_HEIGHT + 10};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Reset độ trong suốt
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Vẽ text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Wrap text if necessary
        String[] words = message.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int lineHeight = g2d.getFontMetrics().getHeight();
        int currentY = y + PADDING + lineHeight;
        
        for (String word : words) {
            if (g2d.getFontMetrics().stringWidth(currentLine + " " + word) < DIALOG_WIDTH - 2*PADDING) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                g2d.drawString(currentLine.toString(), x + PADDING, currentY);
                currentY += lineHeight;
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            g2d.drawString(currentLine.toString(), x + PADDING, currentY);
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void hide() {
        isVisible = false;
    }
}