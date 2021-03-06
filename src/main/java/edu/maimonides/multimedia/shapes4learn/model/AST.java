package edu.maimonides.multimedia.shapes4learn.model;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalException;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic implementation of a tree. The attributes to be stored will be
 * implemented by the students according to what they considered appropriated.
 *
 * @author Matias Giorgio
 *
 */
public class AST {

    /**
     * The {@link AST} children.
     */
    private List<AST> children;
    private Token token;
/**
 * Runs astVisitor on each AST nodes in pre-order.
 * @param myVisitor
 * @throws LexicalException
 * @throws SemanticException 
 */
    public void preOrder(astVisitor myVisitor) throws LexicalException, SemanticException {

        for (AST child : children) {
            child.preOrder(myVisitor);
        }
        myVisitor.visit(this);

    }

    public AST(List<AST> children, Token token) {
        this.children = children;
        this.token = token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }

    public AST() {
        this.children = new LinkedList<>();
        this.token = new Token();
    }

    /**
     * Add a new child to the current AST.
     *
     * @param child The new child.
     */
    public void addChild(AST child) {
        this.children.add(child);
    }

    /**
     * Removes a child from the AST.
     *
     * @param child The child to be removed.
     * @return <code>true</code> if the child existed and could be removed.
     */
    public boolean removeChild(AST child) {
        return this.children.remove(child);
    }

    public boolean clearChilds() {
        return this.children.removeAll(children);
    }

    /**
     * Removes the child from the AST given by an index.
     *
     * @param child The child to be removed.
     * @return <code>true</code> if there was a child in the given index and it
     * could be removed.
     */
    public boolean removeChild(int index) {
        return this.removeChild(index);
    }

    /**
     * Retrieves a child from the AST.
     *
     * @param index The index where the child will be retrieved from.
     * @return <code>true</code> if there was a child in the given index.
     */
    public AST getChild(int index) {
        return this.children.get(index);
    }

    /**
     * Lists all the children from the AST.
     *
     * @return The List of children.
     */
    public List<AST> listChildren() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * Replaces a child of the present AST by a new AST.
     *
     * @param oldAST The old child that will be replaced.
     * @param newAST The new child.
     * @return <code>true</code> if the old AST existed and could be replaced.
     */
    public boolean replace(AST oldAST, AST newAST) {
        int index = this.children.indexOf(oldAST);

        if (index > -1) {
            this.children.set(index, newAST);
            return true;
        }
        return false;
    }
}
