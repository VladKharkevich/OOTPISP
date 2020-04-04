package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Terminator implements Serializable {
    public String aim;
    public Integer model;

    public Terminator(){}

    public Terminator(LinkedHashMap<String, String> params) {
        this.aim = params.get("aim");
        this.model = Integer.parseInt(params.get("model"));
    }

}
