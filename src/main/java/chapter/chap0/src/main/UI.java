package chapter.chap0.src.main;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.object.OBJ_Heart;
import chapter.chap0.src.object.OBJ_Key;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank, keyImage;
    public boolean messageOn = false;
//    public String message = "";
//    int messageCounter = 0;
//    public boolean gameFinish = false;
//    DecimalFormat dFormat = new DecimalFormat("#0.00" );
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int slotCol = 0;
    public int slotRow = 0;//0: the first screen 1: the second screen
    int subState = 0;


    double playTime;

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.PLAIN, 80);

        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;

        //CREATED HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;


    }

    public void addMessage(String text) {
//        messageOn = true;
//        message = text;
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        //PLAY STATE
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }
        //PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreem();
        }

        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }

        //OPTION  STATE
        if(gp.gameState == gp.optionState) {
            drawOptionScreen();
        }

        //GAME OVER STATE
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

    }

    public void drawPlayerLife() {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        //MAX LIFE
        while(i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
         i = 0;
        while(i < gp.player.life) {
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));

        for(int i=0; i<message.size(); i++) {
            if(message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                int counter = messageCounter.get(i) + 1; //messageCounter++
                messageCounter.set(i, counter); //set the counter to the array

                messageY+=50;

                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {

        if(titleScreenState == 0) {
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
            String text = "8th Year Student";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*3;

            //SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text,x+5,y+5);

            //MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text,x,y);

            //BLUE BOY
            x = gp.screenWidth/2 - (gp.tileSize*2)/2;
            y += gp.tileSize*2;
            g2.drawImage(gp.player.down1,x,y,gp.tileSize*2,gp.tileSize*2,null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,49F));

            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize*3.5;
            g2.drawString(text,x,y);
            if(commandNum == 0) {
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text,x,y);
            if(commandNum == 1) {
                g2.drawString(">",x-gp.tileSize,y);
            }


            text = "QUIT";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text,x,y);
            /*if(commandNum == 2) {
                g2.drawString(">",x-gp.tileSize,y);
            }
        } else if(titleScreenState == 1) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your class!";
            int x = getXforCenteredText(text);
            int y = gp.tileSize*3;
            g2.drawString(text,x,y);

            text = "Fighter";
            x = getXforCenteredText(text);
            y += gp.tileSize*3;
            g2.drawString(text,x,y);
            if(commandNum == 0) {
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Thief";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text,x,y);
            if(commandNum == 1) {
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Sorcerer";
            x = getXforCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text,x,y);
            if(commandNum == 2) {
                g2.drawString(">",x-gp.tileSize,y);
            }

            text = "Back";
            x = getXforCenteredText(text);
            y += gp.tileSize*2;
            g2.drawString(text,x,y);
            if(commandNum == 3) {
                g2.drawString(">",x-gp.tileSize,y);
            }
*/
        }

    }

    public void drawPauseScreem() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSE";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);

    }

    public void drawDialogueScreen() {

        //WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,29F));
        x += gp.tileSize;
        y += gp.tileSize;
        //g2.drawString(currentDialogue,x,y);
        for(String line: currentDialogue.split("\n")) {
            g2.drawString(line,x,y);
            y+=40;
        }

    }

    public void drawCharacterScreen() {
        //CREATE A FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        //NAMES
        /*g2.drawString("Level",textX,textY);
        textY += lineHeight;*/
        g2.drawString("Life",textX,textY);
        textY += lineHeight;
        g2.drawString("Strength",textX,textY);
        textY += lineHeight;
        g2.drawString("Dexterity",textX,textY);
        textY += lineHeight;
        g2.drawString("Attack",textX,textY);
        textY += lineHeight;
        g2.drawString("Defense",textX,textY);
        textY += lineHeight;
        /*g2.drawString("Exp",textX,textY);
        textY += lineHeight;
        g2.drawString("Next Level",textX,textY);
        textY += lineHeight;
        g2.drawString("Coin",textX,textY);
        textY += lineHeight;*/
        g2.drawString("Weapon",textX,textY);
        textY += lineHeight;
        g2.drawString("Shield",textX,textY);
        textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;

        //Reset textY
        textY = frameY + gp.tileSize;

        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += 15;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 15, null);
        textY += lineHeight;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 15, null);

    }

    public void drawInventory() {
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        //SOLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize+3;

        //DRAW PLAYER'S ITEM
        for(int i=0; i<gp.player.inventory.size(); i++) {

            if(gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield ) {
                g2.setColor(new Color(240,190,190));
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if(i==4 || i==9 || i==14 ) {
                slotX = slotXStart;
                slotY += slotSize ;
            }
        }

        //CURSOR
        int cursorX = slotXStart + (gp.tileSize * slotCol);
        int cursorY = slotYStart + (gp.tileSize * slotRow);
        int cursorWitdth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAWW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWitdth, cursorHeight,10,10);

        //DESCRIPTION NAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;

        //DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }

    }

    public void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        text = "Game Over";

        //SHADOW
        g2.setColor(Color.BLACK);
        x = getXforCenteredText(text);
        y = gp.tileSize*6;
        g2.drawString(text,x,y);

        //MAIN
        g2.setColor(Color.WHITE);
        g2.drawString(text,x-4,y-4);

        //Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize*3;
        g2.drawString(text,x,y);
        if(commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        //Quit
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Quit";
        x = getXforCenteredText(text);
        y += gp.tileSize*2;
        g2.drawString(text,x,y);
        if(commandNum == 1) {
            g2.drawString(">",x-40,y);
        }

    }

    public void drawOptionScreen() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        switch (subState) {
            case 0: option_top(frameX, frameY);break;
            case 1: optionFullScreenNotifition(frameX,frameY);break;
            case 2: option_control(frameX, frameY);break;
           // case 3: drawExitConfirmation();break;
        }
    }

    public void option_top(int frameX,int frameY) {
        int textX;
        int textY;

        String text = "Option";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //MUSIC
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }

        //CONTROL
//        textY += gp.tileSize;
//        g2.drawString("Control", textX, textY);
//        if (commandNum == 2) {
//            g2.drawString(">", textX - 25, textY);
//            if(gp.keyH.enterPressed == true) {
//                subState = 1;
//                commandNum = 0;
//            }
//        }

        //ENDGAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.titleState;
            }
        }

        //BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
            }
        }

        //MUSIC CHECK BOX
        textX = frameX + gp.tileSize*5;
        textY = frameY + gp.tileSize+24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24*gp.music.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,20);


        //SE CHECK BOX
        textY += gp.tileSize;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24*gp.se.volumeScale;
        g2.fillRect(textX,textY,volumeWidth,20);

    }

    public void optionFullScreenNotifition(int frameX, int frameY) {
        int textX = gp.tileSize;
        int textY = gp.tileSize*3;

        currentDialogue = "Trạng thái thay đổi sẽ trở\n lại như cũ khi khởi động lại game!";

        for(String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //BACK
        textY = frameY + gp.tileSize*7;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
            }

        }
    }

    public void option_control(int frameX, int frameY) {
        int textX;
        int textY;

        String text = "Control";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text,textX,textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;

        g2.drawString("Move",textX,textY);
        textY += gp.tileSize;

        g2.drawString("Confirm/Attack",textX,textY);
        textY += gp.tileSize;

        g2.drawString("Shot/Cast",textX,textY);
        textY += gp.tileSize;

        g2.drawString("Character Screen",textX,textY);
        textY += gp.tileSize;

        g2.drawString("Pause",textX,textY);
        textY += gp.tileSize;

        g2.drawString("Option",textX,textY);
    }

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c) ;
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);

    }

    public int getXforCenteredText(String text) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;

    }

    public int getXforAlignToRightText(String text, int tailX) {

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;

    }

}
