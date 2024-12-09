package chapter.chap0.src.main;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][][];

    int previousEventX, previousEventY;
    boolean canToachEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while(col < gp.maxWorldCol && row < gp.maxWorldRow && map < gp.maxMap) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

            if(row == gp.maxWorldRow) {
                row = 0;
                map++;
            }

        }

    }

    public void checkEvent() {
        //Check if the player character is more than 1 title away from the last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize) {
            canToachEvent = true;
        }
        if (canToachEvent == true) {
            // Kiểm tra va chạm với OBJ_Chest (rương) tại vị trí (map 0, col 10, row 7)
            if (hit(0, 10, 7, "any")) {  // Chạm vào rương
                handleChestEvent();
            }
        }
        
        if(canToachEvent == true) {
            if(hit(0,35,43,"any") == true) { damagePit1(gp.dialogueState); }
            else if(hit(0,8,21,"any") == true) { damagePit2(gp.dialogueState); }
            else if(hit(0,38,11,"any") == true) { damagePit3(gp.dialogueState); }
            else if(hit(0,11,35,"any") == true) { damagePit4(gp.dialogueState); }
            else if(hit(0,8,18,"any") == true) { damagePit5(gp.dialogueState); }
            //if(hit(24,15,"right") == true) { teleport(gp.dialogueState); }
            else if(hit(0,10,7,"any") == true) {teleport(1,23,21);}
            else if(hit(1,12,13,"any") == true) {teleport(0,23,21);}
        }

    }
    public void handleChestEvent() {
        System.out.println("Player touched the chest!");
    
        if (gp.gameCompleteCallback != null) {
            gp.gameCompleteCallback.run(); // Chuyển cảnh qua map mê cung
        } else {
            System.err.println("Game complete callback is not set!");
        }
    
        canToachEvent = false; // Đảm bảo rằng sự kiện không xảy ra liên tục
    }
    public boolean hit(int map,int col, int row, String reqDirection) {

        boolean hit = false;

        //eventRect[col][row] = new EventRect();

        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col*gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row*gp.tileSize + eventRect[map][col][row].y;

            if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
                if(gp.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }


        return hit;

    }

//    public void teleport(int gameState) {
//        gp.gameState = gameState;
//        gp.ui.currentDialogue = "Teleport!";
//        gp.player.worldX = gp.tileSize*37;
//        gp.player.worldY = gp.tileSize*10;
////        gp.gameState = gameState;
//    }

    public void damagePit1(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Lại là giấc mơ ở vùng đất xa xôi, mình sẽ dùng\n" +
                "laptop gaming tiêu diệt đám ma vật! ";
        canToachEvent = false;
    }
    public void damagePit2(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Chỗ này hình như bị chặn bởi dòng chảy ma pháp!\n" +
                "Haizzz chìa khóa ở ngay kia mà, thôi đi đường vòng";
        canToachEvent = false;
    }

    public void damagePit3(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Chỗ này bị bỏ hoang sao!";
        gp.playSE(17);
        canToachEvent = false;
    }

    public void damagePit4(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Hồ này trông quen quen :)";
        canToachEvent = false;
    }

    public void damagePit5(int gameState) {

        gp.gameState = gameState;
        gp.ui.currentDialogue = "Khám phá lâu đài kia thôi!";
        canToachEvent = false;
    }

    // public void healingPool(int gameState) {
    //     if(gp.keyH.enterPressed == true) {
    //         gp.gameState = gameState;
    //         gp.player.attackCancel = true;
    //         gp.ui.currentDialogue = "You drink the water.\nYour life has been recorved!";
    //         gp.player.life = gp.player.maxLife;
    //         gp.aSetter.setMonster();
    //     }
    //     gp.keyH.enterPressed = false;
    // }

    public void teleport(int map, int col, int row) {
        gp.currentMap = map;
        gp.player.worldX = gp.tileSize*col;
        gp.player.worldY = gp.tileSize*row;
        previousEventX = gp.player.worldX;
        previousEventY = gp.player.worldY;
        canToachEvent = false;
        gp.playSE(11);
    }

}
