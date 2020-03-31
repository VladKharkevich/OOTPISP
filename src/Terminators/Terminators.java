package Terminators;

import java.util.LinkedHashMap;

class Terminator
{
    public String aim;
    public Integer model;

    public Terminator(LinkedHashMap<String, String> params) {
        this.aim = params.get("aim");
        this.model = Integer.parseInt(params.get("model"));
    }
}

class Humanoid extends Terminator{
    public Weapon weapon;

    public Humanoid(LinkedHashMap<String, String> params) {
        super(params);
        this.weapon = Weapon.valueOf(params.get("weapon"));
    }
}

class LiquidTerminator extends Terminator{
    public String coldWeapon;

    public LiquidTerminator(LinkedHashMap<String, String> params) {
        super(params);
        this.coldWeapon = params.get("coldWeapon");
    }
}

class Assassin extends Terminator{
    public String currentHuman;

    public Assassin(LinkedHashMap<String, String> params) {
        super(params);
        this.currentHuman = params.get("currentHuman");
    }
}

class AI {
    public static String skynet = "Skynet";
    public static String legion = "Legion";
}

class Materials {
    public static String carbon_fiber = "carbon fiber";
    public static String liquid_metal = "liquid metal";
    public static String nanobots = "nanobots";
    public static String carbon_alloy = "carbon alloy";
}
