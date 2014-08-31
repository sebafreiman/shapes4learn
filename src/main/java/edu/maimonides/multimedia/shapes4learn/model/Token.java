package edu.maimonides.multimedia.shapes4learn.model;

import edu.maimonides.multimedia.shapes4learn.analysis.LexicalException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This empty class represents a Token generated by the lexical analyzer. It
 * will be implemented by the students according to the language and interpreter
 * requirements.
 *
 * @author Matias Giorgio
 *
 */
public class Token {

    String value;
    String type;

    public Token(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public Token() {
        this.type="";
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) throws LexicalException {
        if (value.matches("create|setcolor|setbase|setheight|setradius|setposition")) {
            this.setType("command");
        } else if (value.matches("circle|rectangle")) {
            this.setType("shape_type");
        } else if (value.matches("in")) {
            this.setType("connector_in");
        } else if (value.matches("shape")) {
            this.setType("connector_shape");
        } else if (value.matches("(\\+|-|\\*|/|[0-9])*")) {
            this.setType("expression");
        } else if (value.matches("\\(")) {
            this.setType("parenthesis_open");
        } else if (value.matches("\\)")) {
            this.setType("parenthesis_close");
        } else if (value.matches("^#(([A-F|a-f|0-9]{2}){3})")) {
            this.setType("color_def");
        } else if (value.matches("[a-z|A-Z]{2,}")) {
            this.setType("identifier");
        } else if (value.matches(";")) {
            this.setType("command_end");
        } else if (value.matches(",")) {
            this.setType("expression_separator");
        }
        this.value = value;
    }

}
