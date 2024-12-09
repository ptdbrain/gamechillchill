package chapter.chap0.src.object;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Chest extends Entity {


    public OBJ_Chest(GamePanel gp) {
        super(gp);
        name = "chest";

        down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
        collision = true;
    }

}
