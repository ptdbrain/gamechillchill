package chapter.chap0.src.object;

import chapter.chap0.src.entity.Entity;
import chapter.chap0.src.main.GamePanel;

public class OBJ_Shield_Wood extends Entity {

    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);
        name = "Shield Wood";
        down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\nAn old shield!";
    }

}
