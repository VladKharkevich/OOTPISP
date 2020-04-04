package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Humanoid extends Terminator implements Serializable {
    public Weapon weapon;

    public Humanoid(LinkedHashMap<String, String> params) {
        super(params);
        this.weapon = Weapon.valueOf(params.get("weapon"));
    }

    public Humanoid(){}
}
