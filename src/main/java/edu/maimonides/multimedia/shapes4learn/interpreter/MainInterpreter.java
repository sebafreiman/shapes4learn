/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.maimonides.multimedia.shapes4learn.interpreter;
import edu.maimonides.multimedia.shapes4learn.analysis.*;
import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.ShapeAmbient;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Interpreter implementation
  * @author sfreiman
 */
public class MainInterpreter implements Interpreter{

    @Override
    public void interpret(String code, ShapeAmbient ambient) throws CodeException {
        LexicalAnalyzer lexA = new LexicalAnalyzer();
        SyntacticAnalyzer synA = new SyntacticAnalyzer();
        SemanticAnalyzer semA = new SemanticAnalyzer();
        ShapeDrawVisitor shapeDrawV = new ShapeDrawVisitor();
        AST ast = new AST();
        
        try {
            List<Token> lexATokens = lexA.analyze(code);
            ast=synA.analyze(lexATokens);
            ast=semA.analyze(ast);
            shapeDrawV.setAmbient(ambient);
            ast.preOrder(shapeDrawV);
            
        } catch (LexicalException | SyntacticException | SemanticException ex ) {
            throw new CodeException(ex.getMessage());
        }
    }

    @Override
    public void interpret(InputStream stream, ShapeAmbient ambient) throws CodeException, IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
