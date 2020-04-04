package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Assassin extends Terminator implements Serializable {
    public String currentHuman;

    public Assassin(LinkedHashMap<String, String> params) {
        super(params);
        this.currentHuman = params.get("currentHuman");
    }

    public Assassin(){}
}
