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
        tkenStack = new Stack<Token>();
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

    public AST matchSetBaseHeight() throws SyntacticException {
//        setbase|setheight [expression] in rectangle [id];
        AST matchAST = new AST();
        matchAST = matchE(this.tkenit);
        matchGeneric(this.tkenit, "connector_in");
        matchGeneric(this.tkenit, "shape_rectangle");
        matchGeneric(this.tkenit, "identifier");
        matchGeneric(tkenit, "command_end");
        return matchAST;
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

        matchExpression(this.tkenit);

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

        matchExpression(this.tkenit);

        if (!matchExpressionSeparator(this.tkenit)) {
            matchStatus = false;
        }

        matchExpression(this.tkenit);

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

                throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType + ". \n I've founda token type: " + tken.getType() + " value:'" + tken.getValue() + "'");
            }
        } else {
            throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType);
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

    /**
     * Verify if tokens in the list contains a valid expression.
     *
     * @param tkenit
     * @return
     * @throws SyntacticException
     */
    private AST matchExpression(ListIterator<Token> tkenit) throws SyntacticException {

        AST expAST = new AST(), childAST = new AST();

        /*number] := [digit][number]|[digit]
         A mathematical expression that supports numbers, addition, subtraction, multiplication, division and parenthesis. For example: 9+(4*(5-7)+8/2)
         [expression] :=  [termino] + [termino] | [termino] - [termino] |  ( [expression] )
         [termino] := [termino] * [termino] | [termino] / [termino] | [number]
         */
        /*
         1+2+3+4
         [number] := [digit][number]|[digit]
         [expression] :=  [termino] | [termino] + [termino] | [termino] - [termino] 
         [termino] :=  [factor] | [factor] * [factor] | [factor] / [factor]
         [factor] := [number] | ( [expression] )
         */
        if (tkenit.hasNext()) {
            Token tken = tkenit.next();
            //tkenit.remove();
            switch (tken.getType()) {
                case "parenthesis_open":
                    //expression
                    expAST = matchExpression(tkenit);

                    //matchExpression(this.tkenit);
                    matchGeneric(this.tkenit, "parenthesis_close");

                    if (lookahead(this.tkenit, "expression_operator_term|expression_operator_fact")) {
                        expAST.addChild(matchExpression(this.tkenit));
                    }
                    break;

                case "parenthesis_close":
                    throw new SyntacticException("You've close a parenthesis but did't openen one.");
                //break;

                case "expression_number":
                    //termino

                    if (lookahead(this.tkenit, "expression_operator_term")) {
                        expAST = matchExpression(tkenit);
                        childAST.setToken(tken);
                        expAST.addChild(childAST);

                    } else if (lookahead(this.tkenit, "expression_operator_fact")) {
                        expAST.addChild(matchExpression(this.tkenit));
                        childAST.setToken(tken);
                        expAST.addChild(childAST);

                    } else if (lookahead(this.tkenit, "parethesis_open")) {
                        expAST.addChild(matchExpression(this.tkenit));
                        childAST.setToken(tken);
                        expAST.addChild(childAST);

                    } else {
                        expAST.setToken(tken);
                    }

                    break;
                case "expression_operator_term":
                    expAST.setToken(tken);
                    expAST.addChild(matchExpression(this.tkenit));

                    break;
                case "expression_operator_fact":
                    expAST.setToken(tken);
                    expAST.addChild(matchExpression(this.tkenit));

                    break;
                default:
                    throw new SyntacticException("Syntactic exception: Not an expression: " + tken.getType() + " value:" + tken.getValue());

            }

        } else {
            throw new SyntacticException("Syntactic exception: Expected expression");

        }
        return expAST;

    }

    private AST matchE(ListIterator<Token> tkenit) throws SyntacticException {
        /*
         <E> ::= <T> <E'>  ->MatchE
         <E'> ::= + <E> | - <E> | ε -> matchExprTerm
         <T> ::= <F> <T'>
         <T'> ::= * <T> | / <T> | ε
         <F> ::= [N] | ( <E> ) | - <F> matchExprFact
         */
        Token tken;
        AST expAST = new AST();
        String lookStr = lookaheadString(tkenit);
        switch (lookStr) {
            case "expression_number":
                matchExprFactor(tkenit);
                matchExprTerm(tkenit);
                break;
            case "parenthesis_open":
                this.tkenStack.add(tkenit.next());
                matchE(tkenit);
                matchGeneric(tkenit, "parenthesis_close");
                if (lookahead(tkenit, "expression_operator_term")) {
                    matchExprTerm(tkenit);
                }
                break;
            default:
                throw new SyntacticException("Syntact error: Expected expression but get something else:" + lookStr);
        }

        //matchExprFactor(tkenit);
        return expAST;

    }

    private AST matchExprTerm(ListIterator<Token> tkenit) throws SyntacticException {
        AST expAST = new AST();

        //matchExprFactor(tkenit);

        String lookString = lookaheadString(tkenit);

        while (lookString.matches("expression_operator_term|expression_operator_fact")) {
            switch (lookString) {
                case "expression_operator_term":
                    this.tkenStack.add(tkenit.next());
                    matchExprFactor(tkenit);
                    matchExprTerm(tkenit);
                    break;
                case "expression_operator_fact":
                    this.tkenStack.add(tkenit.next());
                    matchExprFactor(tkenit);
                    break;

            }
            lookString = lookaheadString(tkenit);
        }

        return expAST;

    }

    private AST matchExprFactor(ListIterator<Token> tkenit) throws SyntacticException {
        AST expAST = new AST();
        String lookString = lookaheadString(tkenit);
        switch (lookString) {
            case "expression_number":
                this.tkenStack.add(tkenit.next());
                break;
            case "parenthesis_open":
                this.tkenStack.add(tkenit.next());
                matchE(tkenit);
                matchGeneric(tkenit, "parenthesis_close");
                break;
            case "":
                break;
            default:
                throw new SyntacticException("Sytactic error: Expected number or parenthesis.");

        }
        return expAST;

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

    /**
     * lookahead function check if the next token matches the type with the
     * string.
     *
     * @param tkenit
     * @param typeString
     * @return
     */
    private boolean lookahead(ListIterator<Token> tkenit, String typeString) {
        boolean matchStatus = false;
        if (tkenit.hasNext()) {
            Token tken = tkenit.next();
            if (tken.getType().matches(typeString)) {
                matchStatus = true;
            }
            tkenit.previous();
        }

        return matchStatus;
    }

    private String lookaheadString(ListIterator<Token> tkenit) {
        String matchStatus = "";
        if (tkenit.hasNext()) {
            Token tken = tkenit.next();
            matchStatus = tken.getType();
            tkenit.previous();
        }

        return matchStatus;
    }

}
