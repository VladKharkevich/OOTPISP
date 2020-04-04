package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class T1000 extends LiquidTerminator implements Serializable {
    public final String producer = AI.skynet;
    public final String material = Materials.liquid_metal;

    public T1000(LinkedHashMap<String, String> params){
        super(params);
    }

    public T1000(){}
}
