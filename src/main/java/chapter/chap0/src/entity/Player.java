package chapter.chap0.src.entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import chapter.chap0.src.main.GamePanel;
import chapter.chap0.src.main.KeyHandler;
import chapter.chap0.src.object.OBJ_Shield_Wood;
import chapter.chap0.src.object.OBJ_Sword_Normal;

public class Player extends Entity{

    public KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;
    boolean moving = false;
    public boolean attackCancel = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int keyCounter = 0;
    public int diedMonsterCounter = 0;



    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        //Attack Area
        attackArea.width = 36;
        attackArea.height = 36;

        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItem();

    }



    public void setDefaultValues() {
        // worldX = gp.tileSize * 35;
        // worldY = gp.tileSize * 43;

        worldX = gp.tileSize * 8;
        worldY = gp.tileSize * 18;


        speed = 4;
        direction = "up";

        //PLAYER STATUS
/*        level = 1;*/
        maxLife = 6;
        life = maxLife;
        strength = 2; //the more strength he has, the more damage he gives
        dexterity = 1; //the more dexterity he has, the less damage he receives
/*        exp = 0;
        nextLevelExp = 5;
        coin = 0;*/
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
/*        projectile = new OBJ_Fireball(gp);*/
        attack = getAttack(); //the total attack value is decide by strength and weapon
        defense = getDefense(); //the total defense value is decide by dexterity and shield
    }

    public void setDefaultPosition() {
        worldX = gp.tileSize*35;
        worldY = gp.tileSize*43;

        direction = "down";
    }

    public void restoreLifeAndMan() {
        life = maxLife;
        invincible = false;
    }

    public void setItem() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }

    public int getAttack() {
        return strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {

        up1 = setup("/player/huster_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/huster_up_2", gp.tileSize, gp.tileSize);

        down1 = setup("/player/huster_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/huster_down_2", gp.tileSize, gp.tileSize);

        left1 = setup("/player/huster_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/huster_left_2", gp.tileSize, gp.tileSize);

        right1 = setup("/player/huster_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/huster_right_2", gp.tileSize, gp.tileSize );

    }

    public void getPlayerAttackImage() {

        if(currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/huster_up_1", gp.tileSize, gp.tileSize);
            attackUp2 = setup("/player/huster_attack_up_2", gp.tileSize+20, gp.tileSize+20);

            attackDown1 = setup("/player/huster_down_1", gp.tileSize, gp.tileSize);
            attackDown2 = setup("/player/huster_attack_down_2", gp.tileSize, gp.tileSize);

            attackLeft1 = setup("/player/huster_left_1", gp.tileSize, gp.tileSize);
            attackLeft2 = setup("/player/huster_attack_left_2", gp.tileSize, gp.tileSize);

            attackRight1 = setup("/player/huster_right_1", gp.tileSize, gp.tileSize);
            attackRight2 = setup("/player/huster_attack_right_2", gp.tileSize, gp.tileSize);
        }

    }


    public void update() {

        if(attacking == true) {
            attacking();
        } else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
                || keyH.rightPressed == true || keyH.enterPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {
                direction = "right";
            }

            moving = true;

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK EVENT COLLISION
            gp.eHandler.checkEvent();

            //IF COLLISION IS FALSE , PLAYER CAN MOVE
            if(collisionOn == false && keyH.enterPressed == false) {
                switch (direction) {
                    case "up": worldY-=speed; break;
                    case "down": worldY+=speed; break;
                    case "left":  worldX-=speed; break;
                    case "right": worldX+=speed; break;
                }
            }

            if(keyH.enterPressed == true && attackCancel == false) {
                attacking = true;
                spriteCounter = 0;
            }

            attackCancel = false;
            keyH.enterPressed = false;

            spriteCounter++;
            if(spriteCounter>12) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2) {
                    spriteNum =1;
                }
                spriteCounter=0;
            }
        } else {
                standCounter++;

                if(standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }

            }

        if(keyH.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30) {
            //Set default coordinates, direction and user
            projectile.set(worldX,worldY,direction,true,this);

            //Add it to list
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;

            gp.playSE(9);
        }



        //This needs to be outside of key if statement
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if(life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(10);
        }
    }

    public void attacking() {

        spriteCounter++;
        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        else if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            //Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/Y for the attackArea
            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            //attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            //After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;


        }
        else if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }


    public void pickUpObject(int i) {
        if(keyH.enterPressed == true) {
            if(i != 999) {
                String text = "";
                if(gp.obj[gp.currentMap][i].name == "door") {
                    if(keyCounter>0){
                        gp.obj[gp.currentMap][i] = null;
                        keyCounter--;
                    }
                } else if(gp.obj[gp.currentMap][i].name == "chest") {
                    if(diedMonsterCounter >= 0) {
                        //later
                        gp.obj[gp.currentMap][i] = null;
                    }
                } else {
                    if(inventory.size() < maxInventorySize) {
                        if(gp.obj[gp.currentMap][i].name == "key") ++keyCounter;
                        text = "Mình lấy được chìa khóa rồi!";
                        inventory.add(gp.obj[gp.currentMap][i]);
                    } else {
                        text = "Túi đầy rồi bỏ bớt đồ trong túi thôi!";
                    }
                    gp.ui.addMessage(text);
                    gp.obj[gp.currentMap][i] = null;
                }
            }
        }
    }

    public void interactNPC(int i) {
        if(gp.keyH.enterPressed == true) {
            if(i != 999) {
                attackCancel = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][i].speak();
            } else {
                attacking = true;
            }
        }
    }

    public void contactMonster(int i) {
        if(i != 999) {
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
                gp.playSE(6);
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage <= 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i) {
        if(i != 999) {
            if(gp.monster[gp.currentMap][i].invincible == false) {
                gp.playSE(5 );
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage <= 0) {
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
/*                gp.ui.addMessage(damage + "damage!");*/
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if(gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    ++diedMonsterCounter;
/*                    gp.ui.addMessage("Mình đã tiêu diệt được " + gp.monster[gp.currentMap][i].name + "!");*/
/*                    gp.ui.addMessage("Exp " + gp.monster[gp.currentMap][i].exp);*/
/*                    exp += gp.monster[gp.currentMap][i].exp;*/
/*                    checkLevelUp();*/
                }
            }
        }
    }

    /*public void checkLevelUp() {
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLife+=2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(7);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level" + level + "now\n" + "You feel stronger!";
        }
    }*/

    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_axe || selectedItem.type == type_sword) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if(selectedItem.type == type_shield || selectedItem.type == type_shield_blue) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                } else
                if(attacking == true) {
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                break;
            case "down":
                if(attacking == false) {
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                } else
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackDown1;}
                    if(spriteNum == 2) {image = attackDown2;}
                }
                break;
            case "left":
                if(attacking == false) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                } else
                if(attacking == true) {
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                break;
            case "right":
                if(attacking == false) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                } else
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2 ;}
                }
                break;
        }

        if(invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//        g2.setColor(Color.red);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

    }

}
