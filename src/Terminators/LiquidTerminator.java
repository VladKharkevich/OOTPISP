package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class LiquidTerminator extends Terminator implements Serializable {
    public String coldWeapon;

    public LiquidTerminator(LinkedHashMap<String, String> params) {
        super(params);
        this.coldWeapon = params.get("coldWeapon");
    }

    public LiquidTerminator(){}
}
