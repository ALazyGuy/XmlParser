package com.ltp.xmlparser.parser.handler;

import com.ltp.xmlparser.model.entity.*;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CandyHandler extends DefaultHandler {

    private static final Logger LOGGER = LogManager.getLogger(CandyHandler.class);

    @Getter
    private List<Candy> candies;
    private Candy candy;
    private Ingredients ingredients;
    private Value value;
    private String currentElement;

    public CandyHandler(){
        candies = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;

        switch (currentElement){
            case "candy":
                candy = new Candy();
                candy.setProduction(attributes.getValue("production"));
                break;
            case "candy-chocolate":
                candy = new ChocolateCandy();
                candy.setProduction(attributes.getValue("production"));
                FillingType filling = FillingType.EMPTY;
                String attr = attributes.getValue("filled");
                if(attr != null){
                    filling = FillingType.valueOf(attr.toUpperCase());
                }
                ((ChocolateCandy)candy).setFillingType(filling);
                ((ChocolateCandy)candy).setChocolateType(ChocolateType.valueOf(attributes.getValue("chocolate").toUpperCase()));
                break;
            case "value":
                value = new Value();
                break;
            case "ingredients":
                ingredients = new Ingredients();
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length);
        if(text.contains("<") || currentElement == null || text.trim().isEmpty()) return;

        switch (currentElement){
            case "name":
                candy.setName(text);
                break;
            case "energy":
                candy.setEnergy(Integer.valueOf(text));
                break;
            case "type":
                candy.setCandyType(CandyType.valueOf(text.toUpperCase()));
                break;
            case "release-date":
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    candy.setRelease(format.parse(text));
                }catch(ParseException e){
                    candy.setRelease(new Date());
                    LOGGER.log(Level.ERROR, String.format("Unable to parse date [%s]", text));
                }
                break;
            case "proteins":
                value.setProteins(Integer.valueOf(text));
                break;
            case "fats":
                value.setFats(Integer.valueOf(text));
                break;
            case "hydrocarbons":
                value.setHydrocarbons(Integer.valueOf(text));
                break;
            case "ingredient":
                ingredients.addIngredient(text);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "candy":
            case "candy-chocolate":
                candies.add(candy);
                candy = null;
                break;
            case "ingredients":
                candy.setIngredients(ingredients);
                ingredients = null;
                break;
            case "value":
                candy.setValue(value);
                value = null;
                break;
        }
    }
}
