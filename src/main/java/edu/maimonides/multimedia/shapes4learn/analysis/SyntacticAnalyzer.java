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

    AST synAST;
    ListIterator<Token> tkenit;
    Stack<Token> tkenStack;

    public SyntacticAnalyzer() {
        tkenStack = new Stack<Token>();
        this.synAST = new AST();

    }

    public AST analyze(List<Token> tokens) throws SyntacticException {

        Token tken;
        AST newAST = new AST();

        this.tkenit = tokens.listIterator();

        while (this.tkenit.hasNext()) {
            tken = this.tkenit.next();
            switch (tken.getType()) {
                case "command_create":
                    newAST = matchCreate(tken);
                    break;
                case "command_setcolor":
                    newAST = matchSetColor(tken);
                    break;
                case "command_setbase":
                    newAST = matchSetBaseHeight(tken);
                    break;
                case "command_setheight":
                    newAST = matchSetBaseHeight(tken);
                    break;
                case "command_setradius":
                    newAST = matchSetRadius(tken);
                    break;
                case "command_setposition":
                    newAST = matchSetPosition(tken);
                    break;
                default:
                    throw new SyntacticException("Syntactic Exception: Expected command but get: " + tken.getType() + " (" + tken.getValue() + ")");
            }
            this.synAST.addChild(newAST);
            newAST = new AST();
        }
        return this.synAST;
    }

    public AST matchSetBaseHeight(Token cmdToken) throws SyntacticException {
//setbase [expression] in rectangle [id];

        AST myAST = new AST();
        myAST.setToken(cmdToken);

        myAST.addChild(matchE(this.tkenit));
        matchGeneric(this.tkenit, "connector_in");
        myAST.addChild(matchGeneric(this.tkenit, "shape_rectangle"));
        myAST.addChild(matchGeneric(this.tkenit, "identifier"));
        matchGeneric(tkenit, "command_end");
        return myAST;
    }

    public AST matchCreate(Token cmdToken) throws SyntacticException {
        //create rectangle|circle [id];
        AST myAST = new AST();
        myAST.setToken(cmdToken);

        myAST.addChild(matchGeneric(this.tkenit, "shape_rectangle|shape_circle"));
        myAST.addChild(matchGeneric(this.tkenit, "identifier"));
        matchGeneric(this.tkenit, "command_end");

        return myAST;

    }

    private AST matchSetColor(Token cmdToken) throws SyntacticException {
        //setcolor [color_def] in shape [id]; 
        AST myAST = new AST();
        myAST.setToken(cmdToken);
        myAST.addChild(matchGeneric(this.tkenit, "color_def"));
        matchGeneric(this.tkenit, "connector_in");
        myAST.addChild(matchGeneric(this.tkenit, "shape_rectangle|shape_circle"));
        myAST.addChild(matchGeneric(this.tkenit, "identifier"));
        matchGeneric(this.tkenit, "command_end");
        return myAST;
    }

    private AST matchSetRadius(Token cmdToken) throws SyntacticException {
        //setradius [expression] in circle [id];
        AST myAST = new AST();
        myAST.setToken(cmdToken);

        myAST.addChild(matchE(this.tkenit));
        matchGeneric(tkenit, "connector_in");
        matchGeneric(this.tkenit, "shape_circle");
        myAST.addChild(matchGeneric(this.tkenit, "identifier"));
        matchGeneric(this.tkenit, "command_end");

        return myAST;
    }

    private AST matchSetPosition(Token cmdToken) throws SyntacticException {
        //setposition [expression],[expression] in shape [id];
        AST myAST = new AST();

        myAST.setToken(cmdToken);
        myAST.addChild(matchE(this.tkenit));
        matchGeneric(this.tkenit, "expression_separator");

        myAST.addChild(matchE(this.tkenit));
        matchGeneric(this.tkenit, "connector_in");

        matchGeneric(this.tkenit, "shape_circle|shape_rectangle");

        myAST.addChild(matchGeneric(this.tkenit, "identifier"));
        matchGeneric(this.tkenit, "command_end");

        return myAST;

    }

    public AST matchGeneric(ListIterator<Token> tokens, String tokenType) throws SyntacticException {
        AST myAST = new AST();
        if (tokens.hasNext()) {
            Token tken = tokens.next();
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
