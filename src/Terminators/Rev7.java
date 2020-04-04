package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Rev7 extends LiquidTerminator implements Serializable {
    public final String producer = AI.legion;
    public final String material = Materials.carbon_fiber;

    public Rev7(LinkedHashMap<String, String> params){
        super(params);
    }

    public Rev7(){}
}
