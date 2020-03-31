package Terminators;

import java.util.LinkedHashMap;

public class Battery {
    public TypeSource typeSource;
    public Integer lifeTime;
    public Integer model;

    public Battery(LinkedHashMap<String, String> params){
        this.typeSource = TypeSource.valueOf(params.get("typeSource"));
        this.lifeTime = Integer.parseInt(params.get("lifeTime"));
        this.model = Integer.parseInt(params.get("model"));
    }
}
