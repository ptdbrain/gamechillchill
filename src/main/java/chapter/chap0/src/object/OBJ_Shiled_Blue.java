package chapter.chap0.src.object;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.main.GamePanel;

public class OBJ_Shiled_Blue extends Entity {

    GamePanel gp;

    public OBJ_Shiled_Blue(GamePanel gp) {

        super(gp);
        name = "Shile Blue";
        type = type_shield_blue;
        down1 = setup("/objects/shield_blue", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        collision = true;
        description = "[" + name + "]";

    }

}
