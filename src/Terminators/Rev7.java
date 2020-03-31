package Terminators;

import java.util.LinkedHashMap;

public class Rev7 extends LiquidTerminator{
    public final String producer = AI.legion;
    public final String material = Materials.carbon_fiber;

    public Rev7(LinkedHashMap<String, String> params){
        super(params);
    }
}
