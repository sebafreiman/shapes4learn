package edu.maimonides.multimedia.shapes4learn.analysis;

import edu.maimonides.multimedia.shapes4learn.interpreter.ArismeticSolverVisitor;
import edu.maimonides.multimedia.shapes4learn.model.AST;

/**
 * This class is responsible for the third part of the interpreter: semantic
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 *
 * @author Matias Giorgio
 *
 */
public class SemanticAnalyzer {

    public SemanticAnalyzer() {
    }
/**
 * This class performance and semantic analysis. 
 * Optimization: solve arismetics expressions and replace subAST with solution.
 * Semantic analysis: check if arismetic solution is zero or positive.
 * @param myAST
 * @return resultAST
 * @throws SemanticException
 * @throws LexicalException 
 */
    public AST analyze(AST myAST) throws SemanticException, LexicalException {
        AST resultAST=myAST;
        ArismeticSolverVisitor ASV=new ArismeticSolverVisitor();
        
        resultAST.preOrder(ASV);

        return resultAST;
    }
}
