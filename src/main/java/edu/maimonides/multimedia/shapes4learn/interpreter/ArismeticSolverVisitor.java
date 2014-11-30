/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.interpreter;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalException;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticException;
import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import edu.maimonides.multimedia.shapes4learn.model.astVisitor;
import java.util.Stack;

/**
 * Visitor to solve arismetic expressions.
 * @author sfreiman
 */
public class ArismeticSolverVisitor implements astVisitor {

    private int result, op1, op2;

    Stack<Token> acumStack;

    public ArismeticSolverVisitor() {
        acumStack = new Stack<>();
    }

    public void visit(AST arismeticAST) throws LexicalException, SemanticException {
        String tokenType = arismeticAST.getToken().getType();
        switch (tokenType) {
            case "expression_number":
                acumStack.push(arismeticAST.getToken());
                break;
            case "expression_op_product":
            case "expression_op_division":
            case "expression_op_subtract":
            case "expression_op_addition":

                op1 = Integer.parseInt(arismeticAST.getChild(0).getToken().getValue());
                op2 = Integer.parseInt(arismeticAST.getChild(1).getToken().getValue());

                arismeticAST.getToken().setValue(this.evaluate(tokenType));
                acumStack.push(arismeticAST.getToken());
                arismeticAST.clearChilds();
                break;
            default:
                break;

        }

    }
/**
 * Evaluate the arismetics operators and returns the string representation of
 * the result.
 * Performs validation of positive results.
 * @param tokenType
 * @return
 * @throws SemanticException 
 */
    private String evaluate(String tokenType) throws SemanticException {
        switch (tokenType) {
            case "expression_op_product":
                result = op1 * op2;
                break;
            case "expression_op_division":
                if (op2 != 0) {
                    result = op1 / op2;
                } else {
                    throw new SemanticException("Semantic exception: Unable to divide by 0. I'm a computer, you know?");
                }
                break;
            case "expression_op_subtract":
                result = op1 - op2;
                break;
            case "expression_op_addition":
                result = op1 + op2;
                break;
        }

        if (result < 0) {
            throw new SemanticException("Semantic Exception: Think positive, also arismetics ");
        }
        return String.valueOf(result);
    }

}
