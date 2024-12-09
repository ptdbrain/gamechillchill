package chapter.chap0.src.main;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.entity.Player;
import chapter.chap0.src.entity.Projectile;
import chapter.chap0.src.monster.MON_GreenSlime;
import chapter.chap0.src.tile.TileManeger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public Runnable getGameCompleteCallback() {
        return gameCompleteCallback;
    }
    
    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48*48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;

    public final int screenWidth = tileSize * maxScreenCol; 
    public final int screenHeight = tileSize * maxScreenRow; 

    //FOR FULL SCREEN
    public final int screenWidth2 = screenWidth;
    public final int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 100;
    public int currentMap = 0;

    //FPS
    int FPS = 60;

    //SYSTEM
    public TileManeger tileM = new TileManeger(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public EventHandler eHandler = new EventHandler(this);
    public UI ui = new UI(this);
    public Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public Entity obj[][] = new Entity[maxMap][10];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public ArrayList<Entity > projectileList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();
    public Runnable gameCompleteCallback;
    //GAME STATE
    public int gameState;
    public int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int gameOverState = 6;
    private boolean running = true;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setGameCompleteCallback(Runnable callback) {
        this.gameCompleteCallback = callback;
    }
    public void setupGame() {
        aSetter.setObject();
        aSetter.setMonster();
        //playMusic(0);
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_ARGB);

        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void retry() {
        player.setDefaultPosition();
        player.restoreLifeAndMan();
        aSetter.setMonster();
    }

    public void restart() {
        player.setDefaultValues();
        player.setDefaultPosition();
        player.restoreLifeAndMan();
        player.setItem();
        aSetter.setObject();
        aSetter.setMonster();
    }
    public void stopGame() {
        stopMusic();
        running = false;

        try {
            if (gameThread != null) {
                gameThread.join(); // Đợi thread dừng hẳn
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    public void startgameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public  void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(running) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta>0) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                //System.out.println("FPS:"+FPS);
                drawCount=0;
                timer=0;
            }

        }

    }

    public void update() {
        if(gameState == playState) {
            //PLAYER
            player.update();

            //NPC
            for(int i=0; i<npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            //MONSTER
            for(int i=0; i<monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    }
                    if(monster[currentMap][i].alive == false) {
                        monster[currentMap][i] = null;
                    }
                }
            }

            //PROJECTILES
            for(int i=0; i<projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    if(projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false) {
                        projectileList.remove(i);
                    }
                }
            }
        }

        if(gameState == pauseState) {
            //nothing
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Graphics2D g2 = (Graphics2D) g;

        //TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        } else {
            //TILE
            tileM.draw(g2);

            //ADD ENTITY TO THE LIST
             entityList.add(player);

             for(int i=0; i< npc[1].length; i++) {
                 if(npc[currentMap][i] != null) {
                     entityList.add(npc[currentMap][i]);
                 }
             }

            for(int i=0; i< obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }

            for(int i=0; i< monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            for(int i=0; i< projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY,e2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i=0; i<entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();
            //PLAYER
            player.draw(g2);

            //UI
            ui.draw(g2);
        }


        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

}
