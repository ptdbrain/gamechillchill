package chapter.chap0.src.entity;

import chapter.chap0.src.main.GamePanel;

public class Projectile extends Entity{

    GamePanel gp;
    Entity user;
    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }
    public void update() {

//        if(user == gp.player) {
//            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster[gp.currentMap]);
//            if(monsterIndex != 999) {
//                gp.player.damageMonster(monsterIndex);
//                alive = false;
//            }
//        }
//
//        if(user != gp.player) {
//            boolean contactPlayer = gp.cChecker.checkPlayer(this);
//            if(gp.player.invincible == false && contactPlayer == true) {
//               damagePlayer(attack);
//               alive = false;
//             }
//        }

        switch (direction) {
            case "up": worldY -= speed;break;
            case "down": worldY += speed;break;
            case "left": worldX -= speed;break;
            case "right": worldX += speed;break;
        }
        life--;
        if(life <= 0){
            alive = false;
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

}
