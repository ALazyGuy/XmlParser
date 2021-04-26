package com.ltp.xmlparser.parser;

import com.ltp.xmlparser.exception.XmlException;
import com.ltp.xmlparser.model.entity.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StaxParser implements AbstractParser{

    private static final Logger LOGGER = LogManager.getLogger(StaxParser.class);

    private XMLStreamReader reader;
    private List<Candy> candies;

    private Candy candy;
    private Ingredients ingredients;
    private Value value;

    public StaxParser(){
        candies = new ArrayList<>();
    }

    @Override
    public List<Candy> parse(String filename) throws XmlException {
        try{
            reader = XMLInputFactory.newFactory().createXMLStreamReader(filename, new FileInputStream(filename));

            while(reader.hasNext()){
                reader.next();
                String name;
                if(reader.isStartElement()){
                    name = reader.getLocalName();
                    switch(name){
                        case "candy":
                            candy = new Candy();
                            candy.setProduction(reader.getAttributeValue(null, "production"));
                            break;
                        case "candy-chocolate":
                            candy = new ChocolateCandy();
                            candy.setProduction(reader.getAttributeValue(null, "production"));
                            FillingType filling = FillingType.EMPTY;
                            String attr = reader.getAttributeValue(null, "filled");
                            if(attr != null){
                                filling = FillingType.valueOf(attr.toUpperCase());
                            }
                            ((ChocolateCandy)candy).setFillingType(filling);
                            ((ChocolateCandy)candy).setChocolateType(ChocolateType.valueOf(reader.getAttributeValue(null, "chocolate").toUpperCase()));
                            break;
                        case "value":
                            value = new Value();
                            break;
                        case "ingredients":
                            ingredients = new Ingredients();
                            break;
                        case "name":
                            candy.setName(reader.getElementText());
                            break;
                        case "energy":
                            candy.setEnergy(Integer.valueOf(reader.getElementText()));
                            break;
                        case "type":
                            candy.setCandyType(CandyType.valueOf(reader.getElementText().toUpperCase()));
                            break;
                        case "release-date":
                            try {
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                candy.setRelease(format.parse(reader.getElementText()));
                            }catch(ParseException e){
                                candy.setRelease(new Date());
                                LOGGER.log(Level.ERROR, String.format("Unable to parse date [%s]", reader.getElementText()));
                            }
                            break;
                        case "proteins":
                            value.setProteins(Integer.valueOf(reader.getElementText()));
                            break;
                        case "fats":
                            value.setFats(Integer.valueOf(reader.getElementText()));
                            break;
                        case "hydrocarbons":
                            value.setHydrocarbons(Integer.valueOf(reader.getElementText()));
                            break;
                        case "ingredient":
                            ingredients.addIngredient(reader.getElementText());
                            break;
                    }
                }else if(reader.isEndElement()){
                    name = reader.getLocalName();
                    switch (name){
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

        }catch(XMLStreamException | FileNotFoundException e){
            throw new XmlException("Unable to read data", e);
        }

        return candies;
    }
}
