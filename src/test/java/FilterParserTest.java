
import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.builders.Q;
import com.github.germanosin.JsonQL.builders.Query;
import com.github.germanosin.JsonQL.builders.QueryBuilder;
import com.github.germanosin.JsonQL.exceptions.CQLParserException;
import com.github.germanosin.JsonQL.filters.BaseFilter;
import com.github.germanosin.JsonQL.filters.Filter;
import com.github.germanosin.JsonQL.parsers.CQLParser;
import com.github.germanosin.JsonQL.parsers.FilterRequestRequestParser;
import com.github.germanosin.JsonQL.utils.Json;
import com.github.germanosin.JsonQL.parsers.OrderRequestParser;
import com.github.germanosin.JsonQL.parsers.SelectRequestRequestParser;
import com.github.germanosin.JsonQL.exceptions.OperationNotFoundException;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;
import com.github.germanosin.JsonQL.forms.*;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by lerrox on 04.03.15.
 */
public class FilterParserTest {

    @Test
    public void simpleParserTest() throws WrongFormatException, OperationNotFoundException {

        JsonNode node = Json.parse("[\"==\", \"class\", \"street_limited\"]");
        FilterRequestRequestParser parser = FilterRequestRequestParser.getInstance();
        Filter filter = parser.parse(node);
        assertTrue(filter.getType().equals(Filter.Type.EQUALS));
        assertTrue(((BaseFilter)filter).getKey().equals("class"));
        assertTrue(((BaseFilter)filter).getValue().equals("street_limited"));
        assertTrue(((BaseFilter)filter).getValue() instanceof String);

        JsonNode generatedJson = filter.toJson();
        assertNotNull(generatedJson);
        assertTrue(generatedJson.isArray());
        assertTrue(generatedJson.get(0).asText().equals("=="));
        assertTrue(generatedJson.get(2).asText().equals("street_limited"));

        JsonNode node1 = Json.parse("[\"==\", \"array\", [\"street_limited\", \"123\"]]");
        FilterRequestRequestParser parser1 = FilterRequestRequestParser.getInstance();
        Filter filter1 = parser1.parse(node1);
        assertTrue(filter1.getType().equals(Filter.Type.EQUALS));
        assertTrue(((BaseFilter)filter1).getKey().equals("array"));
        String val = ((List<String>)((BaseFilter)filter1).getValue()).get(0);
        assertTrue(val.equals("street_limited"));
        assertTrue(((BaseFilter)filter1).getValue() instanceof List);

        JsonNode generatedJson2 = filter1.toJson();
        assertNotNull(generatedJson2);
        assertTrue(generatedJson2.isArray());
        assertTrue(generatedJson2.get(0).asText().equals("=="));
        assertTrue(generatedJson2.get(2).isArray());

    }

    @Test
    public void simpleInParserTest() throws WrongFormatException, OperationNotFoundException {
        JsonNode node = Json.parse("[\"in\", \"array\",  1, 2, 3, 4, 887 ]");
        FilterRequestRequestParser parser = FilterRequestRequestParser.getInstance();
        Filter filter = parser.parse(node);
        assertTrue(filter.getType().equals(Filter.Type.IN));
        assertTrue(((BaseFilter)filter).getKey().equals("array"));
        List<Integer> value = (ArrayList)((BaseFilter) filter).getValue();
        assertTrue(value.get(2).equals(3));
        assertTrue(value.size() == 5);

        JsonNode generatedJson = filter.toJson();
        assertNotNull(generatedJson);
        assertTrue(generatedJson.isArray());
        assertTrue(generatedJson.get(0).asText().equals("in"));
        assertTrue(generatedJson.get(3).asInt() == 2);

        JsonNode node2 = Json.parse("[\"!in\", \"array\",  \"121\", \"валенок\"]");
        FilterRequestRequestParser parser2 = FilterRequestRequestParser.getInstance();

        Filter filter2 = parser2.parse(node2);
        assertTrue(filter2.getType().equals(Filter.Type.NOT_IN));
        assertTrue(((BaseFilter)filter2).getKey().equals("array"));
        List<String> value2 = ((BaseFilter<ArrayList<String>>) filter2).getValue();
        assertTrue(value2.get(1).equals("валенок"));
        assertTrue(value2.size() == 2);

        JsonNode generatedJson2 = filter2.toJson();
        assertNotNull(generatedJson2);
        assertTrue(generatedJson2.isArray());
        assertTrue(generatedJson2.get(0).asText().equals("!in"));
        assertTrue(generatedJson2.get(3).asText().equals("валенок"));


    }

    @Test
    public void notSoSimpleParserTest() throws WrongFormatException, OperationNotFoundException {
        JsonNode node = Json.parse("[\n" +
                "      \"all\",\n" +
                "      [\"any\", \n" +
                "      [\"==\", \"class\", \"street_limited\"],\n" +
                "      [\"null\", \"some\"],\n" +
                "      [\"!null\", \"somed\"],\n" +
                "      [\">=\", \"admin_level\", 3],\n" +
                "      [\"!in\", \"category.id\", 1, 2, 4]\n" +
                "],             " +
                "      [\"==\", \"class\", \"street_limited\"],\n" +
                "      [\">=\", \"admin_level\", 3],\n" +
                "      [\"@within\", \"$geom\", [\"@bbox\", 44,55,50,60]],\n" +
                "      [\"!in\", \"category.id\", 1, 2, 4]\n" +
                "    ]");
        FilterRequestRequestParser parser = FilterRequestRequestParser.getInstance();

        Filter filter = parser.parse(node);
        assertNotNull(filter);
        assertEquals(filter.getType(), Filter.Type.ALL);

        JsonNode generatedJson = filter.toJson();
        assertNotNull(generatedJson);
        assertTrue(generatedJson.isArray());
        assertTrue(generatedJson.get(0).asText().equals("all"));
        assertTrue(generatedJson.get(1).isArray());

    }

    @Test
    public void selectRequestTest() throws WrongFormatException {
        JsonNode node = Json.parse(" [\"id\", \"name\", \"category.id\", [\"category.name\", \"category_name\"], [[\"@concat\", \"$title\", \" \", \"$width\", [\"@concat\", \"$title\", \" \", \"$width\"]], \"title\"]]");
        SelectRequestRequestParser parser = SelectRequestRequestParser.getInstance();
        SelectRequest request = parser.parse(node);
        assertNotNull(request);
        assertEquals(request.getOperands().size(), 5);
        JsonNode jsoned = request.toJson();
        assertNotNull(jsoned);
    }

    @Test
    public void orderRequestTest() throws WrongFormatException {
        JsonNode node = Json.parse(" [[\"asc\", \"name\"], [\"desc\",\"id\"]]");
        OrderRequestParser parser = OrderRequestParser.getInstance();
        OrderList order = parser.parse(node);
        assertNotNull(order);
        assertEquals(order.getOrders().size(), 2);
        assertNotNull(order.getOrders().get(0));
        assertEquals(order.getOrders().get(0).getFieldName(), "name");
        assertEquals(order.getOrders().get(0).getType(), Order.Type.ASC);

        JsonNode jsoned = order.toJson();
        assertNotNull(jsoned);
    }

    @Test
    public void cqlParserTest() throws CQLParserException, JSQLParserException {
        Filter filter = CQLParser.getInstance().parse("admin = true AND wheel != false");
        assertNotNull(filter);
        assertEquals(filter.getType(), Filter.Type.ALL);
        String jsonStr = Json.stringify(filter.toJson());
        assertEquals(jsonStr, "[\"all\",[\"==\",\"admin\",true],[\"!=\",\"wheel\",false]]");
    }

    public int median(int[] a){
        int summ = 0;
        for(int i = 0; i < a.length; i++){
            summ += a[i];
        }

        int start = 0;
        for(int i = 0; i < a.length; i++){
            start += a[i];
            if(summ - start == 0){
                return i;
            }
        }

        return -1;
    }

    @Test
    public void testTest(){
        int[] a = {1,2,3,4,5,-5,-4,-3,-2,-1};
        int median = median(a);
        int gfd = 12;
    }

    @Test
    public void queryBuilderTest(){
        Query query = QueryBuilder.create()
                .select()
                .field("field_1")
                .as("firstField")
                .field("field_2")
                .as("secondField")
                .function("some_func", Q.Field("dsssdsd"), Q.Function("dsdasdas", Q.Value(321)))
                .as("dsadsdas")
                .where()
                .eq("dsasad", 12)
                .btw("dsa", 12, 15)
                .and(
                        Q.Or(
                                Q.Eq("dsaasd", 12),
                                Q.Ge("dss", "dsa")
                        ),
                        Q.Func("some_func", Q.Field("dsdsds"), Q.Value(12))
                )
                .order()
                .asc("field1")
                .desc("field2")
                .limit(10)
                .offset(100)
                .generate();

        assertNotNull(query);

    }


}
