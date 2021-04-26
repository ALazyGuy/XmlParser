package com.ltp.xmlparser.parser;

import com.ltp.xmlparser.exception.XmlException;
import com.ltp.xmlparser.model.entity.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomParser implements AbstractParser {

    private List<Candy> candies;
    private Candy candy;
    private Ingredients ingredients;
    private Value value;

    private DocumentBuilder builder;

    public DomParser() throws XmlException{
        candies = new ArrayList<>();
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlException("Unable to create dom builder", e);
        }
    }

    @Override
    public List<Candy> parse(String filename) throws XmlException {
        Document document = getDocument(filename);

        NodeList nodes = document.getFirstChild().getChildNodes();

        parseNodes(nodes);

        return candies;
    }

    private void parseNodes(NodeList nodes) throws XmlException{
        Node current;
        for(int node = 0; node < nodes.getLength(); node++) {
            current = nodes.item(node);

            if(current.getNodeType() != Node.ELEMENT_NODE){
                continue;
            }

            switch (current.getNodeName()) {
                case "candy":
                    candy = new Candy();
                    candy.setProduction(current.getAttributes().getNamedItem("production").getNodeValue());
                    parseNodes(current.getChildNodes());
                    candies.add(candy);
                    break;
                case "candy-chocolate":
                    candy = new ChocolateCandy();
                    candy.setProduction(current.getAttributes().getNamedItem("production").getNodeValue());
                    ((ChocolateCandy)candy).setChocolateType(ChocolateType.valueOf(current.getAttributes().getNamedItem("chocolate").getNodeValue().toUpperCase()));
                    Node fillingNode = current.getAttributes().getNamedItem("filled");
                    FillingType filling = FillingType.EMPTY;
                    if(fillingNode != null){
                        filling = FillingType.valueOf(fillingNode.getNodeValue().toUpperCase());
                    }
                    ((ChocolateCandy)candy).setFillingType(filling);
                    parseNodes(current.getChildNodes());
                    candies.add(candy);
                    break;
                case "name":
                    candy.setName(current.getTextContent());
                    break;
                case "energy":
                    candy.setEnergy(Integer.valueOf(current.getTextContent()));
                    break;
                case "type":
                    candy.setCandyType(CandyType.valueOf(current.getTextContent().toUpperCase()));
                    break;
                case "release-date":
                    try {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        candy.setRelease(format.parse(current.getTextContent()));
                    }catch(ParseException e){
                        throw new XmlException("Unable to parse date", e);
                    }
                    break;
                case "value":
                    value = new Value();
                    parseNodes(current.getChildNodes());
                    candy.setValue(value);
                    break;
                case "proteins":
                    value.setProteins(Integer.valueOf(current.getTextContent()));
                    break;
                case "fats":
                    value.setFats(Integer.valueOf(current.getTextContent()));
                    break;
                case "hydrocarbons":
                    value.setHydrocarbons(Integer.valueOf(current.getTextContent()));
                    break;
                case "ingredients":
                    ingredients = new Ingredients();
                    parseNodes(current.getChildNodes());
                    candy.setIngredients(ingredients);
                    break;
                case "ingredient":
                    ingredients.addIngredient(current.getTextContent());
                    break;
            }
        }
    }

    private Document getDocument(String filename) throws XmlException{
        File file = new File(filename);
        Document result;
        try{
            result =  builder.parse(file);
        }catch(Exception e){
            throw new XmlException("Unable to parse input xml file", e);
        }
        return result;
    }
}
