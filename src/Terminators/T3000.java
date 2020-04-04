package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class T3000 extends LiquidTerminator implements Serializable {
    public final String producer = AI.skynet;
    public final String material = Materials.nanobots;

    public T3000(LinkedHashMap<String, String> params){
        super(params);
    }

    public T3000(){}
}
