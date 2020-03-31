package Terminators;

import java.util.LinkedHashMap;

public class T3000 extends LiquidTerminator{
    public final String producer = AI.skynet;
    public final String material = Materials.nanobots;

    public T3000(LinkedHashMap<String, String> params){
        super(params);
    }
}
