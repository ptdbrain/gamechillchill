package chapter.chap0.src.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;

    //DEBUG
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
         this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            titleState(code);
        }

        //PLAY STATE
        else if(gp.gameState == gp.playState) {
            playState(code);
//            //DEBUG
//            if(code == KeyEvent.VK_T) {
//                if(checkDrawTime == false) {
//                    checkDrawTime = true;
//                } else if(checkDrawTime == true) {
//                    checkDrawTime = false;
//                }
//            }
            if(code == KeyEvent.VK_R) {
                switch (gp.currentMap) {
                    case 0: gp.tileM.loadMap("/maps/world01.txt",0); break;
                    case 1: gp.tileM.loadMap("/maps/interior01.txt",1); break;
                }

            }
        }

        //PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            pauseState(code);
        }

        //DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }

        //CHARACTER STATE
        else if(gp.gameState == gp.characterState) {
            characterState(code);
        }

        //OPTION STATE
        else if(gp.gameState == gp.optionState) {
            optionState(code);
        }

        //GAME OVER STATE
        else if(gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
    }

    public void titleState(int code) {
        if(gp.ui.titleScreenState == 0) {
            if(code == KeyEvent.VK_UP) {
                if(gp.ui.commandNum==0){
                    gp.ui.commandNum = 2;
                } else {
                    gp.ui.commandNum--;
                }
            }
            if(code == KeyEvent.VK_DOWN ) {
                if(gp.ui.commandNum==2){
                    gp.ui.commandNum = 0;
                } else {
                    gp.ui.commandNum++;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }
                if(gp.ui.commandNum == 1) {
                    //add later
                }
                if(gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        } else if(gp.ui.titleScreenState == 1) {
            if(code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum<0){
                    gp.ui.commandNum = 3;
                }
            }
            if(code == KeyEvent.VK_DOWN ) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum>3){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER) {
                if(gp.ui.commandNum == 0) {
                    System.out.println("Do some fighter specific stuff!");
                    gp.gameState = gp.playState;
                    gp.stopMusic();
                    gp.playMusic(16);
                }
                if(gp.ui.commandNum == 1) {
                    System.out.println("Do some thief specific stuff!");
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 2) {
                    System.out.println("Do some Sorcerer specific stuff!");
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 3) {
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                }
            }
        }
    }

    public void playState(int code) {
        if(code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_DOWN ) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
/*        if(code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }*/
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionState;
        }
    }

    public void pauseState(int code) {
        if(code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code) {
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_UP) {
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSE(8);
            }
        }
        if(code == KeyEvent.VK_LEFT) {
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSE(8);
                }
        }
        if(code == KeyEvent.VK_DOWN) {
            if(gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.playSE(8);
            }
        }
        if(code == KeyEvent.VK_RIGHT) {
            if(gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.playSE(8);
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    public void optionState(int code ) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionState;
        }

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0: maxCommandNum = 3;
        }

        if(code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSE(8);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSE(8);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_LEFT) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 0) {
                    if(gp.music.volumeScale > 0) {
                        gp.music.volumeScale--;
                    } else if(gp.music.volumeScale == 0) {
                        gp.music.volumeScale=0;
                    }
                    gp.music.checkVolume();
                    gp.playSE(8);
                }
                if(gp.ui.commandNum == 1 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(8);
                }
            }
        }
        if(code == KeyEvent.VK_RIGHT) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 0){
                    if(gp.music.volumeScale < 5) {
                        gp.music.volumeScale++;
                    } else if(gp.music.volumeScale == 5) {
                        gp.music.volumeScale=5;
                    }
                    gp.music.checkVolume();
                    gp.playSE(8);
                }
                if(gp.ui.commandNum == 1 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(8);
                }
            }
        }
    }

    public void gameOverState(int code) {
        if(code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            gp.playSE(8);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            gp.playSE(8);
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(16);
            } else if(gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_DOWN ) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
