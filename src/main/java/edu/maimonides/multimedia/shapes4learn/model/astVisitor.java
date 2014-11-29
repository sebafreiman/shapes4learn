/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.model;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalException;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticException;

/**
 *
 * @author sfreiman
 */
public interface astVisitor {

    public void visit(AST myAST) throws LexicalException, SemanticException;


}
