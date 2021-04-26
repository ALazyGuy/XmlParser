package com.ltp.xmlparser.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Candy {
    private String name;
    private int energy;
    private Date release;
    private CandyType candyType;
    private Value value;
    private Ingredients ingredients;
    private String production;

    @Override
    public int hashCode() {
        int result = name.hashCode() + energy * 31 + ((int)(release.getTime() % 100)) * 3 + candyType.hashCode() + value.hashCode() + ingredients.hashCode() + production.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Candy current = (Candy)o;
        boolean result = current.getName().equals(name);
        result = result && current.getEnergy() == energy;
        result = result && current.getRelease().getTime() == release.getTime();
        result = result && current.getCandyType() == candyType;
        result = result && current.getValue().equals(value);
        result = result && current.getIngredients().equals(ingredients);
        result = result && current.getProduction().equals(production);
        return result;
    }

    @Override
    public String toString() {
        String result = String.format("[ %s -> Energy: %d, Release: %s, Type: %s, Value: %s, Ingredients: %s, Production: %s]",
                name,
                energy,
                release.toString(),
                candyType.toString(),
                value.toString(),
                ingredients.toString(),
                production);
        return result;
    }
}
