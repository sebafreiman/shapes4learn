/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.analysis;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SyntacticAnalyzerTest {

    AST ast;
    List<Token> lexATokens;

    public SyntacticAnalyzerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        LexicalAnalyzer lexA = new LexicalAnalyzer();
        ast = new AST();
        try {
            lexATokens = lexA.analyze("create circle micircle ;");
        } catch (LexicalException ex) {
            Logger.getLogger(SyntacticAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of analyze method, of class SyntacticAnalyzer.
     */
    @Test
    public void testAnalyze() throws Exception {
        System.out.println("analyze");
        SyntacticAnalyzer instance = new SyntacticAnalyzer();
        AST result = instance.analyze(lexATokens);

        assertEquals(10, result.listChildren().size());
    }

}
