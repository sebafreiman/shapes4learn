/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.maimonides.multimedia.shapes4learn.interpreter;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalException;
import edu.maimonides.multimedia.shapes4learn.analysis.SemanticException;
import edu.maimonides.multimedia.shapes4learn.model.AST;
import edu.maimonides.multimedia.shapes4learn.model.Color;
import edu.maimonides.multimedia.shapes4learn.model.ShapeAmbient;
import edu.maimonides.multimedia.shapes4learn.model.astVisitor;
import edu.maimonides.multimedia.shapes4learn.model.shapes.Circle;
import edu.maimonides.multimedia.shapes4learn.model.shapes.Rectangle;
import edu.maimonides.multimedia.shapes4learn.model.shapes.Shape;

/**
 *
 * @author sfreiman
 */
class ShapeDrawVisitor implements astVisitor {

    ShapeAmbient ambient;

    @Override
    public void visit(AST myAST) throws LexicalException, SemanticException {
        switch (myAST.getToken().getType()) {
            case "command_create":
                createShape(myAST);
                break;
            case "command_setbase":
                setBase(myAST);
                break;

            case "command_setheight":
                setHeight(myAST);
                break;
            case "command_setcolor":
                setColor(myAST);
                break;
            case "command_setposition":
                setPosition(myAST);
                break;

            default:
                break;
        }
    }

    void setAmbient(ShapeAmbient ambient) {
        this.ambient = ambient;

    }

    void createShape(AST myAST) throws SemanticException {
        Shape miShape = new Shape();
        switch (myAST.getChild(0).getToken().getType()) {
            case "shape_circle":
                miShape = new Circle();
                break;
            case "shape_rectangle":
                miShape = new Rectangle();
                break;
            default:
                throw new SemanticException("Missing Shape Type");
        }
        miShape.setId(myAST.getChild(1).getToken().getValue());
        this.ambient.add(miShape);

    }

    void setColor(AST myAST) throws SemanticException {
        Shape miShape = this.ambient.get(myAST.getChild(2).getToken().getValue());
        Color miColor = new Color();
        miColor.setBlue(0);
        miColor.setGreen(0);
        miColor.setRed(255);
        miShape.setColor(miColor);
    }

    void setBase(AST myAST) throws SemanticException {
        Rectangle miShape;
        int base = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        String shapeID = myAST.getChild(2).getToken().getValue();

        miShape = (Rectangle) this.ambient.get(shapeID);
        miShape.setBase(base);
    }

    private void setHeight(AST myAST) {
        Rectangle miShape;
        int height = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        String shapeID = myAST.getChild(2).getToken().getValue();

        miShape = (Rectangle) this.ambient.get(shapeID);
        miShape.setHeight(height);
    }

    private void setRadius(AST myAST) {
        Circle miShape;
        int radius = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        String shapeID = myAST.getChild(2).getToken().getValue();

        miShape = (Circle) this.ambient.get(shapeID);
        miShape.setRadius(radius);
    }

    private void setPosition(AST myAST) {
        Rectangle miShape;
        int positionX = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        int positionY = Integer.parseInt(myAST.getChild(1).getToken().getValue());

        String shapeID = myAST.getChild(2).getToken().getValue();

        miShape = (Rectangle) this.ambient.get(shapeID);
    }

}