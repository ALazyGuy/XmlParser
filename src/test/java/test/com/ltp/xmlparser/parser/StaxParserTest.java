package test.com.ltp.xmlparser.parser;

import com.ltp.xmlparser.exception.XmlException;
import com.ltp.xmlparser.model.entity.Candy;
import com.ltp.xmlparser.model.entity.CandyType;
import com.ltp.xmlparser.model.entity.Ingredients;
import com.ltp.xmlparser.model.entity.Value;
import com.ltp.xmlparser.parser.AbstractParser;
import com.ltp.xmlparser.parser.StaxParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class StaxParserTest {

    private static final Logger LOGGER = LogManager.getLogger(StaxParserTest.class);

    private Candy expected;
    private AbstractParser parser;

    @BeforeClass
    public void init(){
        try {
            parser = new StaxParser();
            expected = new Candy();
            expected.setProduction("Prod1");
            expected.setName("Candy-1A");
            expected.setEnergy(125);
            expected.setCandyType(CandyType.CARAMEL);
            expected.setRelease(new SimpleDateFormat("yyyy-MM-dd").parse("2021-11-10"));
            Ingredients ingredients = new Ingredients();
            ingredients.addIngredient("milk");
            ingredients.addIngredient("sugar");
            expected.setIngredients(ingredients);
            Value value = new Value();
            value.setFats(15);
            value.setHydrocarbons(7);
            value.setProteins(10);
            expected.setValue(value);
        } catch (ParseException e) {
            LOGGER.log(Level.ERROR, "Unable to create Candy for test");
        }
    }

    @Test(description = "Validate stax parser")
    public void parseTest() throws XmlException{
        List<Candy> candies = parser.parse("src/main/resources/data/Candies.xml");
        Candy actual = candies.get(0);
        AssertJUnit.assertTrue(expected.equals(actual));
    }

    @AfterClass
    public void terminate(){
        expected = null;
        parser = null;
    }

}
