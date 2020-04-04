package Terminators;

import sample.MainScreen;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class T800 extends Humanoid implements Serializable {
    public final double Height = 1.9;
    public final Integer Weight = 200;
    public final String Producer = AI.skynet;
    public final String Camouflage = "rubber";
    public Battery battery;

    public T800(LinkedHashMap<String, String> params){
        super(params);
        this.battery = (Battery) MainScreen.storage.get(Integer.parseInt(params.get("battery")));
    }

    public T800(){}
}
