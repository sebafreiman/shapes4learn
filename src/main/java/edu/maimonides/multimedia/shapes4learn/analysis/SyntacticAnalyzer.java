package edu.maimonides.multimedia.shapes4learn.analysis;

import java.util.List;

import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Token;
import java.util.ListIterator;

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
                case "command_setheight":
                    matchSetBaseHeight();
                    break;
                case "command_setradius":
                    matchSetRadius();
                    break;
                case "command_setposition":
                    matchSetPosition();
                    break;
                default:
                    throw new SyntacticException("Expected command but get:" + tken.getType() + " and value:" + tken.getValue());
            }

        }

        return ast;
    }

    public boolean matchSetBaseHeight() {
//        setbase|setheight [expression] in rectangle [id];

        boolean matchStatus;
        matchStatus = true;

        if (!matchExpression(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchConnectorIn(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchConnectorShapeRectangle(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchId(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;

    }

    public boolean matchCreate() {

        //if(lookahead_command == "create")
        //match (create)
        //match_shape (rectangle|circle)
        //match (id)
        boolean matchStatus;
        matchStatus = true;

        if (!matchShape(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchId(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;

    }

    private boolean matchSetColor() {
        boolean matchStatus;
        matchStatus = true;

        if (!this.tkenit.hasNext() || !matchColorDef(this.tkenit)) {
            matchStatus = false;
        }

        if (!this.tkenit.hasNext() || !matchConnectorIn(this.tkenit)) {
            matchStatus = false;
        }

        if (!this.tkenit.hasNext() || !matchConnectorShape(this.tkenit)) {
            matchStatus = false;
        }

        if (!this.tkenit.hasNext() || !matchId(this.tkenit)) {
            matchStatus = false;
        }

        if (!this.tkenit.hasNext() || !matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;
    }

    private boolean matchSetRadius() {
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

        if (!matchId(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;
    }

    private boolean matchSetPosition() {
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

        if (!matchId(this.tkenit)) {
            matchStatus = false;
        }

        if (!matchCommandEnd(tkenit)) {
            matchStatus = false;
        }

        return matchStatus;
    }

    public boolean matchShape(ListIterator<Token> tokens) {
        boolean matchStatus = true;
        Token tken = tokens.next();
        tokens.remove();
        if (tken.getType().matches("circle|rectangle")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    public boolean matchId(ListIterator<Token> tokens) {
        boolean matchStatus = true;
        Token tken = tokens.next();
        tokens.remove();
        if (tken.getType().matches("identifier")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
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

    private boolean matchExpression(ListIterator<Token> tkenit) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("expression")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchConnectorShapeRectangle(ListIterator<Token> tkenit) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("shape_rectangle")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
        }
        return matchStatus;
    }

    private boolean matchExpressionSeparator(ListIterator<Token> tkenit) {
        boolean matchStatus = true;
        Token tken = tkenit.next();
        tkenit.remove();
        if (tken.getType().matches("expression_separator")) {
            AST newAst = new AST();
            newAst.setToken(tken);
            ast.addChild(newAst);
        } else {
            matchStatus = false;
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

}
