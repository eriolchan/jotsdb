package org.opentsdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ResponseParseTest {

    @Test
    public void testDeserialize_MultipleDataPoints() {
        Gson gson = new GsonBuilder().create();

        String result =
                "[\n" +
                "  {\n" +
                "    \"metric\": \"test.demo\",\n" +
                "    \"tags\": {\n" +
                "      \"type\": \"1\",\n" +
                "      \"vid\": \"15840\"\n" +
                "    },\n" +
                "    \"aggregateTags\": [],\n" +
                "    \"dps\": {\n" +
                "      \"1490844900\": 207,\n" +
                "      \"1490845200\": 211\n" +
                "    }\n" +
                "  }\n" +
                "]";

        DataPoint[] actual = gson.fromJson(result, DataPoint[].class);
        assertEquals(1, actual.length);

        DataPoint value = actual[0];
        assertEquals("test.demo", value.metric);
        assertEquals(2, value.tags.size());

        Map<String, String> tags = value.tags;
        assertEquals("1", tags.get("type"));
        assertEquals("15840", tags.get("vid"));

        assertEquals(0, value.aggregateTags.size());

        Map<String, Double> dps = value.dps;
        assertEquals(2, dps.size());
        assertEquals(Double.valueOf(207), dps.get("1490844900"));
        assertEquals(Double.valueOf(211), dps.get("1490845200"));
    }

    @Test
    public void testDeserialize_NoDataPoint() {
        Gson gson = new GsonBuilder().create();

        String result =
                "[\n" +
                "  {\n" +
                "    \"metric\": \"test.demo\",\n" +
                "    \"tags\": {\n" +
                "      \"type\": \"1\",\n" +
                "      \"vid\": \"15840\"\n" +
                "    },\n" +
                "    \"aggregateTags\": [],\n" +
                "    \"dps\": {}\n" +
                "  }\n" +
                "]";

        DataPoint[] actual = gson.fromJson(result, DataPoint[].class);
        assertEquals(1, actual.length);

        DataPoint value = actual[0];
        assertEquals("test.demo", value.metric);
        assertEquals(2, value.tags.size());

        Map<String, String> tags = value.tags;
        assertEquals("1", tags.get("type"));
        assertEquals("15840", tags.get("vid"));

        assertEquals(0, value.aggregateTags.size());

        Map<String, Double> dps = value.dps;
        assertEquals(0, dps.size());
    }

    @Test
    public void testDeserialize_Empty() {
        Gson gson = new GsonBuilder().create();

        String result = "[]";

        DataPoint[] actual = gson.fromJson(result, DataPoint[].class);
        assertEquals(0, actual.length);
    }

    @Test
    public void testDeserialize_ErrorDetail() {
        Gson gson = new GsonBuilder().create();

        String result =
                "{\n" +
                "  \"error\": {\n" +
                "    \"code\": 400,\n" +
                "    \"message\": \"Missing sub queries\",\n" +
                "    \"details\": \"Missing sub queries\",\n" +
                "    \"trace\": \"net.opentsdb.tsd.BadRequestException: Missing sub queries\"\n" +
                "  }\n" +
                "}";

        ErrorDetail actual = gson.fromJson(result, ErrorDetail.class);
        assertNotNull(actual.error);

        ErrorDetail.Error error = actual.error;
        assertEquals(400, error.code);
        assertEquals("Missing sub queries", error.message);
        assertEquals("net.opentsdb.tsd.BadRequestException: Missing sub queries", error.trace);
    }
}