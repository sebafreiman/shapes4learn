package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.List;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.ListIterator;
import java.util.Stack;

/**
 * This class is responsible for the second part of the interpreter: syntactic
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 *
 * @author Matias Giorgio
 *
 */
public class SyntacticAnalyzer {

    AST ast;
    ListIterator<Token> tkenit;
    Stack<Token> tkenStack;

    public SyntacticAnalyzer() {
    }

    public AST analyze(List<Token> tokens) throws SyntacticException {
        this.ast = new AST();

        // TODO Implement.
        Token tken;
        AST newAst;
        this.tkenit = tokens.listIterator();

        while (this.tkenit.hasNext()) {
            tken = this.tkenit.next();
            this.tkenit.remove();
            newAst = new AST();
            newAst.setToken(tken);

            this.ast.addChild(newAst);
            switch (tken.getType()) {
                case "command_create":
                    matchCreate();
                    break;
                case "command_setcolor":
                    matchSetColor();
                    break;
                case "command_setbase":
                    matchSetBaseHeight();
                    break;
//                case "command_setheight":
//                    matchSetBaseHeight();
//                    break;
//                case "command_setradius":
//                    matchSetRadius();
//                    break;
//                case "command_setposition":
//                    matchSetPosition();
//                    break;
                default:
                    throw new SyntacticException("Syntactic Exception: Expected command but get: " + tken.getType() + " (" + tken.getValue() + ")");
            }

        }

        return ast;
    }

    public boolean matchSetBaseHeight() throws SyntacticException {
//        setbase|setheight [expression] in rectangle [id];

        boolean matchStatus;
        matchStatus = true;

        if (!matchExpression(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "connector_in")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "shape_rectangle")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "identifier")) {
            matchStatus = false;
        }

        if (!matchGeneric(tkenit, "command_end")) {
            matchStatus = false;
        }

        return matchStatus;

    }

    public boolean matchCreate() throws SyntacticException {

        //if(lookahead_command == "create")
        //match (create)
        //match_shape (rectangle|circle)
        //match (id)
        boolean matchStatus;
        matchStatus = true;

        if (!matchGeneric(this.tkenit, "shape_rectangle|shape_circle")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "identifier")) {
            matchStatus = false;

        }

        if (!matchGeneric(this.tkenit, "command_end")) {
            matchStatus = false;
        }

        return matchStatus;

    }

    private boolean matchSetColor() throws SyntacticException {
        boolean matchStatus;
        matchStatus = true;

        if (!matchGeneric(this.tkenit, "color_def")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "connector_in")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "shape_rectangle|shape_circle")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "identifier")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "command_end")) {
            matchStatus = false;
        }

        return matchStatus;
    }

    private boolean matchSetRadius() throws SyntacticException {
        //setradius [expression] in circle [id];

        boolean matchStatus;
        matchStatus = true;

        if (!matchExpression(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchConnectorIn(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchConnectorShapeCircle(this.tkenit, "shape_type_circle")) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "identifier")) {
            matchStatus = false;
        }

        if (!matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;
    }

    private boolean matchSetPosition() throws SyntacticException {
        //Set the 2D-position (x,y) for the shape given by the id: 
        //setposition [expression],[expression] in shape [id];
        boolean matchStatus;
        matchStatus = true;

        if (!matchExpression(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchExpressionSeparator(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchExpression(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchConnectorShape(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchGeneric(this.tkenit, "identifier")) {
            matchStatus = false;
        }

        if (matchGeneric(this.tkenit, "command_end")) {
            matchStatus = false;
        }

        return matchStatus;
    }

    public boolean matchShape(ListIterator<Token> tokens) {
        boolean matchStatus = false;
        Token tken = tokens.next();
        tokens.remove();
        if (tken.getType().matches("shape_rectangle|shape_circle")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
            matchStatus = true;

        }
        return matchStatus;
    }

    public boolean matchGeneric(ListIterator<Token> tokens, String tokenType) throws SyntacticException {
        boolean matchStatus = false;
        if (tokens.hasNext()) {
            Token tken = tokens.next();
            //tokens.remove();
            if (tken.getType().matches(tokenType)) {
//                AST newAst = new AST();
//                newAst.setToken(tken);
//                ast.addChild(newAst);
                matchStatus = true;
            } else {
                throw new SyntacticException("Syntactic exception: I was expecting something like: " + tokenType);
            }
        } else {
            throw new SyntacticException("Syntactic exception: I was expecting something like: " + tokenType);

        }
        return matchStatus;

    }

    public boolean matchCommandEnd(ListIterator<Token> tokens) {
        boolean matchStatus = true;
        Token tken = tokens.next();
        tokens.remove();
        if (tken.getType().matches("command_end")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchColorDef(ListIterator<Token> tokens) {
        boolean matchStatus = true;
        Token tken = tokens.next();
        tokens.remove();
        if (tken.getType().matches("color_def")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchConnectorIn(ListIterator<Token> tkenit) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("connector_in")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchConnectorShape(ListIterator<Token> tkenit) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("connector_shape")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchExpression(ListIterator<Token> tkenit) throws SyntacticException {

        /*number] := [digit][number]|[digit]
         A mathematical expression that supports numbers, addition, subtraction, multiplication, division and parenthesis. For example: 9+(4*(5-7)+8/2)
         [expression] :=  [termino] + [termino] | [termino] - [termino] |  ( [expression] )
         [termino] := [termino] * [termino] | [termino] / [termino] | [number]
         */
        boolean matchStatus = false;
        if (tkenit.hasNext()) {
            Token tken = tkenit.next();
            //tkenit.remove();
            switch (tken.getType()) {
                case "parenthesis_open":
                    matchExpression(this.tkenit);
                    matchGeneric(this.tkenit, "parenthesis_close");
                    
                    if (lookahead(this.tkenit, "expression_operator")) {
                        matchExpression(this.tkenit);
                    }
                    matchStatus = true;

                    break;

                case "parenthesis_close":
                    //throw new SyntacticException("You close parenthesis but did not opened.");
                    break;
                    
                case "expression_number":
                    if (lookahead(this.tkenit, "expression_operator")) {
                        matchExpression(this.tkenit);
                    }
                    matchStatus = true;

                    break;
                case "expression_operator":
                    //if (lookahead(this.tkenit, "parethesis_open|expression_number")) {
                        matchExpression(this.tkenit);
                    //}
                    matchStatus = true;

                    break;
                default:
                    throw new SyntacticException("Syntactic exception: Not an expression: " + tken.getType() + " value:" + tken.getValue());

            }

        }
        return matchStatus;

    }

    private boolean matchConnectorShapeRectangle(ListIterator<Token> tkenit) {
        boolean matchStatus = false;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("shape_rectangle")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
            matchStatus = true;

        }
        return matchStatus;
    }

    private boolean matchExpressionSeparator(ListIterator<Token> tkenit) {
        boolean matchStatus = false;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("expression_separator")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
            matchStatus = true;

        }
        return matchStatus;
    }

    private boolean matchConnectorShapeCircle(ListIterator<Token> tkenit, String matchString) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches(matchString)) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean lookahead(ListIterator<Token> tkenit, String matchString) {
        boolean matchStatus = false;
        if (tkenit.hasNext()) {
            Token tken = tkenit.next();
            if (tken.getType().matches(matchString)) {
                matchStatus = true;
            }
            tkenit.previous();
        }

        return matchStatus;
    }
}
