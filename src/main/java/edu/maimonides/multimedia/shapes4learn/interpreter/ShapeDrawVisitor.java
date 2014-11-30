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
import java.util.Iterator;

/**
 *Implements astVisitor to run commands in the shapeambient.
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
            case "command_setradius":
                setRadius(myAST);
                break;
            case "command_setposition":
                setPosition(myAST);
                break;
            case "command_clear":
                clearShapeAmbient();
                break;
            default:
                break;
        }
    }
/**
 * Set the ambient where the commands will be shapes will be draw 
 * @param ambient 
 */
    void setAmbient(ShapeAmbient ambient) {
        this.ambient = ambient;

    }
/**
 * Creates a shape in the ambient
 * @param myAST
 * @throws SemanticException 
 */
    void createShape(AST myAST) throws SemanticException {
        Shape miShape;
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
/**
 * Set the color to a shape
 * @param myAST
 * @throws SemanticException 
 */
    void setColor(AST myAST) throws SemanticException {

        String shapeID = myAST.getChild(3).getToken().getValue();
        shapeExists(shapeID);

        Shape miShape = this.ambient.get(shapeID);
        String colorR = myAST.getChild(0).getToken().getValue();
        String colorG = myAST.getChild(1).getToken().getValue();
        String colorB = myAST.getChild(2).getToken().getValue();

        Color miColor = new Color();
        miColor.setBlue(Integer.parseInt(colorB, 16));
        miColor.setGreen(Integer.parseInt(colorG, 16));
        miColor.setRed(Integer.parseInt(colorR, 16));
        miShape.setColor(miColor);
    }

    /**
     * Set a base of the rectangle
     * @param myAST
     * @throws SemanticException 
     */
    void setBase(AST myAST) throws SemanticException {
        Rectangle miShape = new Rectangle();
        int base = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        String shapeID = myAST.getChild(2).getToken().getValue();
        shapeExists(shapeID);

        if (!(this.ambient.get(shapeID).getClass().equals(miShape.getClass()))) {
            throw new SemanticException("Semantic exception: In first grade you learn that only rectangles shapes do have base,think about it.");
        }

        miShape = (Rectangle) this.ambient.get(shapeID);
        miShape.setBase(base);
    }
/**
 * Set height of the rectangle shape
 * @param myAST
 * @throws SemanticException 
 */
    private void setHeight(AST myAST) throws SemanticException {

        String shapeID = myAST.getChild(2).getToken().getValue();
        shapeExists(shapeID);

        Rectangle miShape;
        int height = Integer.parseInt(myAST.getChild(0).getToken().getValue());

        miShape = (Rectangle) this.ambient.get(shapeID);
        miShape.setHeight(height);
    }
/**
 * Set the radius of the circle
 * @param myAST
 * @throws SemanticException 
 */
    private void setRadius(AST myAST) throws SemanticException {
        String shapeID = myAST.getChild(1).getToken().getValue();
        shapeExists(shapeID);

        Circle miShape = new Circle();
        int radius;
        radius = Integer.parseInt(myAST.getChild(0).getToken().getValue());

        if (!(this.ambient.get(shapeID).getClass().equals(miShape.getClass()))) {
            throw new SemanticException("Semantic exception: Only circle shapes do have radius, go back to first grade!");
        }
        miShape = (Circle) this.ambient.get(shapeID);
        miShape.setRadius(radius);
    }

    /**
     * Set position of the shape
     *
     * @param myAST
     */
    private void setPosition(AST myAST) throws SemanticException {
        Shape miShape;
        int positionX = Integer.parseInt(myAST.getChild(0).getToken().getValue());
        int positionY = Integer.parseInt(myAST.getChild(1).getToken().getValue());

        String shapeID = myAST.getChild(2).getToken().getValue();
        shapeExists(shapeID);
        miShape = this.ambient.get(shapeID);
        miShape.setX(positionX);
        miShape.setY(positionY);
    }

    /**
     * Clear the Shape Ambient. Removes all Shapes.
     */
    private void clearShapeAmbient() {
        for (Shape sss : this.ambient.shapes()) {
            this.ambient.remove(sss.getId());
        }

    }

    /**
     * ShapeExists throws a semantic exception if can't find the shape id in the
     * ambient.
     *
     * @param shapeID
     * @throws SemanticException
     */
    private void shapeExists(String shapeID) throws SemanticException {
        if (!this.ambient.contains(shapeID)) {
            throw new SemanticException("Semantic exception: Can't find the shape named '" + shapeID + "' in the ambient, but you have the power.");
        }
    }

}
