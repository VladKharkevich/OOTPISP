package Terminators;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Battery implements Serializable {
    public TypeSource typeSource;
    public Integer lifeTime;
    public Integer model;

    public Battery(LinkedHashMap<String, String> params){
        this.typeSource = TypeSource.valueOf(params.get("typeSource"));
        this.lifeTime = Integer.parseInt(params.get("lifeTime"));
        this.model = Integer.parseInt(params.get("model"));
    }

    public Battery(){ }
}
