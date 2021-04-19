package com.ltp.xmlparser.model.entity;

import lombok.Data;

@Data
public class Value {
    private int hydrocarbons;
    private int proteins;
    private int fats;

    @Override
    public int hashCode(){
        int result = 3 * hydrocarbons + 19 * fats + proteins;
        return result;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Value current = (Value)o;
        return (current.getFats() == fats) && (current.getHydrocarbons() == hydrocarbons) && (current.getProteins() == proteins);
    }

    @Override
    public String toString(){
        String result = String.format("Proteins: %d, Fats: %d, Hydrocarbons: %d", proteins, fats, hydrocarbons);
        return result;
    }
}
