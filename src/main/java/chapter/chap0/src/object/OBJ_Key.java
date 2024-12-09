package chapter.chap0.src.object;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {


    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "key";

        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        collision = true;
        description = "[" + name + "]\nAn old key !";
    }

}
