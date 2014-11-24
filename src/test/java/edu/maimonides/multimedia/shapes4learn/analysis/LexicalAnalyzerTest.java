/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.analysis;

import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sfreiman
 */
public class LexicalAnalyzerTest {

    public LexicalAnalyzerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of analyze method, of class LexicalAnalyzer.
     */
    @Test
    public void CreateRectangle() throws Exception {
        System.out.println("CreateRectangle");
        String code = "create rectangle mirectangle ;";
        LexicalAnalyzer instance = new LexicalAnalyzer();
        List<Token> expResult = null;
        List<Token> result = instance.analyze(code);
        assertEquals(4, result.size());
        assertEquals("create", result.get(0).getValue());
        assertEquals("command_create", result.get(0).getType());

        assertEquals("rectangle", result.get(1).getValue());
        assertEquals("shape_rectangle", result.get(1).getType());

        assertEquals("mirectangle", result.get(2).getValue());
        assertEquals("identifier", result.get(2).getType());

        assertEquals(";", result.get(3).getValue());
        assertEquals("command_end", result.get(3).getType());
    }

    @Test
    public void SetBase() throws Exception {
        System.out.println("SetBase");
        String code = "setbase 1234 in rectangle mirectangle ;";
        LexicalAnalyzer instance = new LexicalAnalyzer();
        List<Token> expResult = null;
        List<Token> result = instance.analyze(code);
        assertEquals(6, result.size());
        assertEquals("setbase", result.get(0).getValue());
        assertEquals("command_setbase", result.get(0).getType());

        assertEquals("1234", result.get(1).getValue());
        assertEquals("expression_number", result.get(1).getType());

        assertEquals("in", result.get(2).getValue());
        assertEquals("connector_in", result.get(2).getType());

        assertEquals("rectangle", result.get(3).getValue());
        assertEquals("shape_rectangle", result.get(3).getType());

        assertEquals("mirectangle", result.get(4).getValue());
        assertEquals("identifier", result.get(4).getType());

        assertEquals(";", result.get(5).getValue());
        assertEquals("command_end", result.get(5).getType());
    }

    @Test
    public void SetHeight() throws Exception {
        System.out.println("SetHeight");
        String code = "setheight 1 + 2 in rectangle mirectangle ;";
        LexicalAnalyzer instance = new LexicalAnalyzer();
        List<Token> expResult = null;
        List<Token> result = instance.analyze(code);
        assertEquals(8, result.size());
        assertEquals("setheight", result.get(0).getValue());
        assertEquals("command_setheight", result.get(0).getType());

        assertEquals("1", result.get(1).getValue());
        assertEquals("expression_number", result.get(1).getType());

        assertEquals("+", result.get(2).getValue());
        assertEquals("expression_operator_term", result.get(2).getType());

        assertEquals("2", result.get(3).getValue());
        assertEquals("expression_number", result.get(3).getType());

        assertEquals("in", result.get(4).getValue());
        assertEquals("connector_in", result.get(4).getType());

        assertEquals("rectangle", result.get(5).getValue());
        assertEquals("shape_rectangle", result.get(5).getType());

        assertEquals("mirectangle", result.get(6).getValue());
        assertEquals("identifier", result.get(6).getType());

        assertEquals(";", result.get(7).getValue());
        assertEquals("command_end", result.get(7).getType());
    }
    
    @Test
    public void SetRadius() throws Exception {
        System.out.println("SetRadius");
        String code = "setradius 92 in circle micircle ;";
        LexicalAnalyzer instance = new LexicalAnalyzer();
        List<Token> expResult = null;
        List<Token> result = instance.analyze(code);
        assertEquals(6, result.size());
        assertEquals("setradius", result.get(0).getValue());
        assertEquals("command_setradius", result.get(0).getType());

        assertEquals("92", result.get(1).getValue());
        assertEquals("expression_number", result.get(1).getType());

        assertEquals("in", result.get(2).getValue());
        assertEquals("connector_in", result.get(2).getType());

        assertEquals("circle", result.get(3).getValue());
        assertEquals("shape_circle", result.get(3).getType());

        assertEquals("micircle", result.get(4).getValue());
        assertEquals("identifier", result.get(4).getType());

        assertEquals(";", result.get(5).getValue());
        assertEquals("command_end", result.get(5).getType());
    }

}
