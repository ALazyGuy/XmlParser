package com.ltp.xmlparser.parser;

import com.ltp.xmlparser.exception.XmlException;
import com.ltp.xmlparser.model.entity.Candy;

import java.util.List;

public interface AbstractParser {

    List<Candy> parse(String filename) throws XmlException;

}
