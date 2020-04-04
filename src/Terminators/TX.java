package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TX extends Assassin implements Serializable {
    public final String producer = AI.skynet;

    public TX(LinkedHashMap<String, String> params){
        super(params);
    }

    public TX(){}
}
