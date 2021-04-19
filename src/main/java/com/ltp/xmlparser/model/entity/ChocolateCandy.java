package com.ltp.xmlparser.model.entity;

import lombok.Data;

@Data
public class ChocolateCandy extends Candy {

    private FillingType fillingType;
    private ChocolateType chocolateType;

    @Override
    public String toString() {
        String result = String.format("%s -> { %s, %s }", super.toString(), fillingType.name(), chocolateType.name());
        return result;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += (fillingType == FillingType.EMPTY ? 3 : 19) * (chocolateType == ChocolateType.DARK ? 23 : 31);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        ChocolateCandy current = (ChocolateCandy)o;
        boolean result = super.equals(current);
        result = result && (current.getChocolateType() == chocolateType && current.getFillingType() == fillingType);
        return result;
    }
}
