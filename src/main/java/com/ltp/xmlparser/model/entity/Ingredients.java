package com.ltp.xmlparser.model.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Ingredients {
    private List<String> content;

    public Ingredients(){
        content = new ArrayList<>();
    }

    public void addIngredient(String ingredient){
        content.add(ingredient);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(content.toArray()) * 17;
        return result;
    }

    @Override
    public String toString() {
        String result = String.format("{ %s }", content.stream()
                                                       .collect(Collectors.joining(", ")));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Ingredients current = (Ingredients)o;
        if(o == null || current.getContent() == null || current.getContent().size() != this.content.size()) return false;
        return Arrays.equals(current.getContent().toArray(), content.toArray());
    }
}
