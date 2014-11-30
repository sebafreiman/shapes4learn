/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.analysis;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.LinkedList;
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
    List<Token> lexCreate, lexSetBase, lexSet;
    List<Token> lexAll;
    
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
        lexAll = new LinkedList<>();
        
        ast = new AST();
        try {
            lexCreate = lexA.analyze("create circle micircle ;");
            lexSetBase = lexA.analyze("setbase 1 + 2 in rectangle micircle ; ");
            lexAll.addAll(lexCreate.subList(0, lexCreate.size()));
            lexAll.addAll(lexSetBase.subList(0, lexSetBase.size()));
            
            lexSet = lexA.analyze("setradius ( 6 * ( 5 * 4 )  +  ( 1 ) ) + ( 2 )  in circle micircle ; ");
            lexAll.addAll(lexSet.subList(0, lexSet.size()));
            
            lexSet = lexA.analyze("setposition 1 * 2 + 3 , 7 + 7 * 2 - 1 in shape miid ; ");
            lexAll.addAll(lexSet.subList(0, lexSet.size()));
            
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
    public void testAnalyzeAll() throws Exception {
        System.out.println("analyze create");
        SyntacticAnalyzer instance = new SyntacticAnalyzer();
        AST result = instance.analyze(lexAll);
        
        assertEquals(4, result.listChildren().size());
    }
    
    @Test
    public void testAnalyzeSetBase() throws Exception {
        System.out.println("analyze setbase");
        SyntacticAnalyzer instance = new SyntacticAnalyzer();
        AST result = instance.analyze(lexSetBase);
        
        assertEquals(1, result.listChildren().size());
    }
    
    @Test
    public void testInfitToPrefix() throws Exception {
        LexicalAnalyzer lexB = new LexicalAnalyzer();
        
        System.out.println("Infix to Prefix Test");
        SyntacticAnalyzer instance = new SyntacticAnalyzer();
        AST result = instance.infixConverterToAST((LinkedList<Token>) lexB.analyze("( 1 / 0 )"));
        assertEquals("expression_op_division", result.getToken().getType());
    }
    
}
