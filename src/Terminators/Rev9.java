package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Rev9 extends Assassin implements Serializable {
    public final String producer = AI.legion;

    public Rev9(LinkedHashMap<String, String> params){
        super(params);
    }

    public Rev9(){}
}
