package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class T600 extends Humanoid implements Serializable {
    public final double height = 2.4;
    public final Integer weight = 320;
    public final String producer = AI.skynet;
    public final String camouflage = "rubber";

    public T600(LinkedHashMap<String, String> params){
        super(params);
    }

    public T600(){}
}
