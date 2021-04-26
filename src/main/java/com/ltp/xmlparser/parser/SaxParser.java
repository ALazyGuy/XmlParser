package com.ltp.xmlparser.parser;

import com.ltp.xmlparser.exception.XmlException;
import com.ltp.xmlparser.model.entity.Candy;
import com.ltp.xmlparser.parser.handler.CandyHandler;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class SaxParser implements AbstractParser{

    private SAXParser saxParser;
    private CandyHandler handler;

    public SaxParser() throws XmlException{
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception e){
            throw new XmlException("Unable to create sax parser");
        }
        handler = new CandyHandler();
    }

    @Override
    public List<Candy> parse(String filename) throws XmlException {
        List<Candy> result;
        try {
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(filename);
            result = handler.getCandies();
        } catch (Exception e){
            throw new XmlException("Unable to parse data", e);
        }

        return result;
    }
}
