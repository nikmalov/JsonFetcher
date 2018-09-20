package com.ignidev.nik.JsonFetcher;

import com.ignidev.nik.JsonFetcher.core.Number;
import com.ignidev.nik.JsonFetcher.utility.NetworkUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ParsingTest {

    private final static String jsonExampleValid =
            "{\n" +
            "\"numbers\": [\n" +
            "4, 150, 12, 21, 136, 16, 3\n" +
            "]\n" +
            "}";

    @Test
    public void testNumberInit() {
        assertEquals(new Number(140), new Number(0, 3, true));
    }

    @Test
    public void testEmpty() {
        List<Number> numbers = NetworkUtils.parseJsonToNumbers("");
        assertEquals(numbers, Collections.<Number>emptyList());
    }

    @Test
    public void testInvalidJson() {
        List<Number> numbers = NetworkUtils.parseJsonToNumbers("quarters");
        assertEquals(numbers, Collections.<Number>emptyList());
    }

    @Test
    public void testValidJson() {
        List<Number> numbers = NetworkUtils.parseJsonToNumbers(jsonExampleValid);
        List<Number> testResult = new ArrayList<>(7);
        testResult.add(new Number(4));
        testResult.add(new Number(150));
        testResult.add(new Number(12));
        testResult.add(new Number(21));
        testResult.add(new Number(136));
        testResult.add(new Number(16));
        testResult.add(new Number(3));
        assertEquals(numbers, testResult);
    }

}
