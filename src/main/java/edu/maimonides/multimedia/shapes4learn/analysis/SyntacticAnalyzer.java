package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.List;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.LinkedList;
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

    private AST synAST;
    private ListIterator<Token> tkenit;
    private LinkedList<Token> tkenStackInfix;

    public SyntacticAnalyzer() {
        this.tkenStackInfix = new LinkedList<Token>();
        this.synAST = new AST();

    }

    public AST analyze(List<Token> tokens) throws SyntacticException {

        String tkenString;
        AST newAST;

        this.tkenit = tokens.listIterator();

        while (this.tkenit.hasNext()) {
            tkenString = lookaheadString();
            switch (tkenString) {
                case "command_create":
                    newAST = matchCreate();
                    break;
                case "command_setcolor":
                    newAST = matchSetColor();
                    break;
                case "command_setbase":
                    newAST = matchSetBaseHeight();
                    break;
                case "command_setheight":
                    newAST = matchSetBaseHeight();
                    break;
                case "command_setradius":
                    newAST = matchSetRadius();
                    break;
                case "command_setposition":
                    newAST = matchSetPosition();
                    break;
                default:
                    throw new SyntacticException("Syntactic Exception: Expected command but get: " + tkenString);
            }
            this.synAST.addChild(newAST);
        }
        return this.synAST;
    }

    /**
     * setbase [expression] in rectangle [id];
     *
     * @return AST
     * @throws SyntacticException
     */
    public AST matchSetBaseHeight() throws SyntacticException {

        AST myAST;
        myAST = matchGeneric("command_setbase");

        matchE();
        myAST.addChild(infixConverterToAST(this.tkenStackInfix));
        matchGeneric("connector_in");
        myAST.addChild(matchGeneric("shape_rectangle"));
        myAST.addChild(matchGeneric("identifier"));
        matchGeneric("command_end");
        return myAST;
    }

    /**
     * @return @throws SyntacticException
     */
    public AST matchCreate() throws SyntacticException {
        //create rectangle|circle [id];
        AST myAST;
        myAST = matchGeneric("command_create");
        myAST.addChild(matchGeneric("shape_rectangle|shape_circle"));
        myAST.addChild(matchGeneric("identifier"));
        matchGeneric("command_end");

        return myAST;

    }

    /**
     *
     * @return @throws SyntacticException
     */
    private AST matchSetColor() throws SyntacticException {
        //setcolor [color_def] in shape [id]; 
        AST myAST;
        myAST = matchGeneric("command_setcolor");
        myAST.addChild(matchGeneric("color_def"));
        matchGeneric("connector_in");
        myAST.addChild(matchGeneric("shape_rectangle|shape_circle"));
        myAST.addChild(matchGeneric("identifier"));
        matchGeneric("command_end");
        return myAST;
    }

    /**
     *
     * @return @throws SyntacticException
     */
    private AST matchSetRadius() throws SyntacticException {
        //setradius [expression] in circle [id];
        AST myAST;
        myAST = matchGeneric("command_setradius");
        myAST.addChild(matchE());
        matchGeneric("connector_in");
        matchGeneric("shape_circle");
        myAST.addChild(matchGeneric("identifier"));
        matchGeneric("command_end");

        return myAST;
    }

    /**
     *
     * @return @throws SyntacticException
     */
    private AST matchSetPosition() throws SyntacticException {
        //setposition [expression],[expression] in shape [id];
        AST myAST;
        myAST = matchGeneric("command_setposition");

        matchE();
        myAST.addChild(infixConverterToAST(this.tkenStackInfix));
        matchGeneric("expression_separator");

        matchE();
        myAST.addChild(infixConverterToAST(this.tkenStackInfix));
        matchGeneric("connector_in");

        matchGeneric("shape_circle|shape_rectangle");

        myAST.addChild(matchGeneric("identifier"));
        matchGeneric("command_end");

        return myAST;

    }

    public AST matchGeneric(String tokenType) throws SyntacticException {
        AST myAST = new AST();
        if (this.tkenit.hasNext()) {
            Token tken = this.tkenit.next();
            if (tken.getType().matches(tokenType)) {
                myAST.setToken(tken);
            } else {
                throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType + ". \n I've found a token type: " + tken.getType() + " value:'" + tken.getValue() + "'");
            }
        } else {
            throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType);
        }
        return myAST;
    }

    public AST matchGenericExpr(String tokenType) throws SyntacticException {
        AST myAST = new AST();
        if (this.tkenit.hasNext()) {
            Token tken = this.tkenit.next();
            if (tken.getType().matches(tokenType)) {
                myAST.setToken(tken);
                this.tkenStackInfix.add(tken);
            } else {
                throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType + ". \n I've found a token type: " + tken.getType() + " value:'" + tken.getValue() + "'");
            }
        } else {
            throw new SyntacticException("Syntactic exception: I was expecting something like:" + tokenType);
        }
        return myAST;
    }

    /**
     * Verify if tokens in the list contains a valid expression.
     *
     * @param tkenit
     * @return
     * @throws SyntacticException
     */
//    private AST matchExpression(ListIterator<Token> tkenit) throws SyntacticException {
//
//        AST expAST = new AST(), childAST = new AST();
//
//        /*number] := [digit][number]|[digit]
//         A mathematical expression that supports numbers, addition, subtraction, multiplication, division and parenthesis. For example: 9+(4*(5-7)+8/2)
//         [expression] :=  [termino] + [termino] | [termino] - [termino] |  ( [expression] )
//         [termino] := [termino] * [termino] | [termino] / [termino] | [number]
//         */
//        /*
//         1+2+3+4
//         [number] := [digit][number]|[digit]
//         [expression] :=  [termino] | [termino] + [termino] | [termino] - [termino] 
//         [termino] :=  [factor] | [factor] * [factor] | [factor] / [factor]
//         [factor] := [number] | ( [expression] )
//         */
//        if (tkenit.hasNext()) {
//            Token tken = tkenit.next();
//            //tkenit.remove();
//            switch (tken.getType()) {
//                case "parenthesis_open":
//                    //expression
//                    expAST = matchExpression(tkenit);
//
//                    //matchExpression(this.tkenit);
//                    matchGeneric(this.tkenit, "parenthesis_close");
//
//                    if (lookahead(this.tkenit, "expression_operator_term|expression_operator_fact")) {
//                        expAST.addChild(matchExpression(this.tkenit));
//                    }
//                    break;
//
//                case "parenthesis_close":
//                    throw new SyntacticException("You've close a parenthesis but did't openen one.");
//                //break;
//
//                case "expression_number":
//                    //termino
//
//                    if (lookahead(this.tkenit, "expression_operator_term")) {
//                        expAST = matchExpression(tkenit);
//                        childAST.setToken(tken);
//                        expAST.addChild(childAST);
//
//                    } else if (lookahead(this.tkenit, "expression_operator_fact")) {
//                        expAST.addChild(matchExpression(this.tkenit));
//                        childAST.setToken(tken);
//                        expAST.addChild(childAST);
//
//                    } else if (lookahead(this.tkenit, "parethesis_open")) {
//                        expAST.addChild(matchExpression(this.tkenit));
//                        childAST.setToken(tken);
//                        expAST.addChild(childAST);
//
//                    } else {
//                        expAST.setToken(tken);
//                    }
//
//                    break;
//                case "expression_operator_term":
//                    expAST.setToken(tken);
//                    expAST.addChild(matchExpression(this.tkenit));
//
//                    break;
//                case "expression_operator_fact":
//                    expAST.setToken(tken);
//                    expAST.addChild(matchExpression(this.tkenit));
//
//                    break;
//                default:
//                    throw new SyntacticException("Syntactic exception: Not an expression: " + tken.getType() + " value:" + tken.getValue());
//
//            }
//
//        } else {
//            throw new SyntacticException("Syntactic exception: Expected expression");
//
//        }
//        return expAST;
//
//    }
    /**
     * matchE()
     *
     * @return
     * @throws SyntacticException
     */
    private AST matchE() throws SyntacticException {
        /*
         <E> ::= <T> <E'>  ->MatchE
         <E'> ::= + <E> | - <E> | ε -> matchExprTerm
         <T> ::= <F> <T'>
         <T'> ::= * <T> | / <T> | ε
         <F> ::= [N] | ( <E> ) | - <F> matchExprFactor
         */
        AST expAST = new AST();
        String lookStr = lookaheadString();
        switch (lookStr) {
            case "expression_number":
                matchExprFactor();
                matchExprTerm();
                break;
            case "parenthesis_open":
                matchGenericExpr("parenthesis_open");
                matchE();
                matchGenericExpr("parenthesis_close");
                if (lookahead("expression_operator_term|expression_operator_fact")) {
                    matchExprTerm();
                }
                break;

            default:
                throw new SyntacticException("Syntact error: Expected expression but get something else:" + lookStr);
        }
        return expAST;

    }

    private AST matchExprTerm() throws SyntacticException {
        AST expAST = new AST();
        String lookString = lookaheadString();

        while (lookString.matches("expression_operator_term|expression_operator_fact")) {
            switch (lookString) {
                case "expression_operator_term":
                    matchGenericExpr("expression_operator_term");
                    matchExprFactor();
                    matchExprTerm();
                    break;
                case "expression_operator_fact":
                    matchGenericExpr("expression_operator_fact");
                    matchExprFactor();
                    break;

            }
            lookString = lookaheadString();
        }

        return expAST;

    }

    private AST matchExprFactor() throws SyntacticException {
        AST expAST = new AST();
        String lookString = lookaheadString();
        switch (lookString) {
            case "expression_number":
                matchGenericExpr("expression_number");
                break;
            case "parenthesis_open":
                matchGenericExpr("parenthesis_open");
                matchE();
                matchGenericExpr("parenthesis_close");
                break;
            case "":
                break;
            default:
                throw new SyntacticException("Sytactic error: Expected number or parenthesis.");

        }
        return expAST;

    }

    /**
     * lookahead function check if the next token matches the type with the
     * string.
     *
     * @param tkenit
     * @param typeString
     * @return
     */
    private boolean lookahead(String typeString) {
        boolean matchStatus = false;
        if (this.tkenit.hasNext()) {
            Token tken = this.tkenit.next();
            if (tken.getType().matches(typeString)) {
                matchStatus = true;
            }
            this.tkenit.previous();
        }

        return matchStatus;
    }

    private String lookaheadString() {
        String matchStatus = "";
        if (this.tkenit.hasNext()) {
            Token tken = this.tkenit.next();
            matchStatus = tken.getType();
            this.tkenit.previous();
        }

        return matchStatus;
    }

    LinkedList ArismeticInfixToPrefix(LinkedList<Token> infixStack) {
        LinkedList<Token> preStack = new LinkedList<Token>(); //stack intermedio
        LinkedList<Token> prefix = new LinkedList<Token>(); // notation prefix
        Token token;
        String tokenType;

        while (!infixStack.isEmpty()) {
            token = infixStack.getLast();
            tokenType = token.getType();
            if (tokenType.equals("expression_operator_fact") || tokenType.equals("expression_operator_term")) {
                prefix.push(token);
            } else {
                if (tokenType.equals("parenthesis_open")) {
                    preStack.push(token);
                } else if (tokenType.equals("parenthesis_close")) {
                    while (!preStack.isEmpty() && !preStack.getLast().getType().equals("parenthesis_open")) {
                        prefix.push(preStack.pollLast());
                    }

                    if (!preStack.isEmpty()) {
                        token = preStack.pop();
                    }

                } else {
                    if (!preStack.isEmpty() && prcd(preStack.getFirst()) <= prcd(token)) {
                        preStack.push(token);
                    } else {
                        while (!preStack.isEmpty() && prcd(preStack.getFirst()) >= prcd(token)) {
                            prefix.push(preStack.pop());
                        }
                        preStack.push(token);
                    }
                }
            }
            infixStack.pollLast();

        }
        while (!preStack.isEmpty()) {
            prefix.add(preStack.pop());

        }
        return prefix;

    }

    /*
     Order of precedence in arismetic expressions
    
     */
    private int prcd(Token mToken) {
        switch (mToken.getType()) {

            case "expression_operator_fact":// * /
                return 4;
            case "expression_operator_term"://+ -
                return 2;
            case "parenthesis_open":
                return 1;
            case "parenthesis_close":
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Converts infix linked list to a AST
     *
     * @param infixExp
     * @return AST
     */
    AST infixConverterToAST(LinkedList<Token> infixExp) {
        LinkedList<Token> STACK = new LinkedList<Token>(); //stack intermedio
        LinkedList<Token> prefixExp = new LinkedList<Token>(); // notation prefix
        AST myAST;
        Token token;
        String tokenType;

        while (!infixExp.isEmpty()) {
            token = infixExp.pollLast();
            tokenType = token.getType();
            switch (tokenType) {
                case "expression_operator_fact":
                case "expression_operator_term":
                    while (!STACK.isEmpty() && (prcd(STACK.getFirst()) > prcd(token))) {
                        prefixExp.push(STACK.poll());
                    }

                    STACK.push(token);
                    break;
                case "parenthesis_close": //right parenthesis
                    STACK.push(token);
                    break;
                case "parenthesis_open": //left parenthesis
                    while (!STACK.isEmpty() && !STACK.getFirst().matchType("parenthesis_close")) {
                        prefixExp.push(STACK.pop());
                    }
                    STACK.pop();

                    break;
                default:
                    prefixExp.push(token);
                    break;
            }

        }
        while (!STACK.isEmpty()) {
            prefixExp.push(STACK.pop());
        }
        myAST = buildArismeticAST(prefixExp);

        return myAST;
    }

    /**
     * @param B return AST
     */
    AST buildArismeticAST(LinkedList<Token> B) {
        AST myAST = new AST();
        if (!B.isEmpty()) {
            switch (B.getFirst().getType()) {
                case "expression_operator_term":
                case "expression_operator_fact":
                    myAST.setToken(B.pop());
                    myAST.addChild(buildArismeticAST(B));
                    myAST.addChild(buildArismeticAST(B));
                    break;
                case "expression_number":
                    myAST.setToken(B.pop());
                    break;
            }

        }
        return myAST;
    }
}
