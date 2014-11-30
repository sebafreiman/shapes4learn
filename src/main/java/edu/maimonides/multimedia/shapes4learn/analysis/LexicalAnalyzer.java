package edu.maimonides.multimedia.shapes4learn.analysis;

import edu.maimonides.multimedia.shapes4learn.model.Color;
import java.util.LinkedList;
import java.util.List;

import edu.maimonides.multimedia.shapes4learn.model.Token;
import org.apache.commons.lang.StringUtils;

/**
 * This class is responsible for the first part of the interpreter: lexical
 * analysis. It will be implemented by the students to perform the proper
 * operations.
 *
 * @author Matias Giorgio
 *
 */
public class LexicalAnalyzer {

    public List<Token> analyze(String code) throws LexicalException {
        List<Token> tokens = new LinkedList<>();
        String[] codeLines = code.split("\\s+");
        for (String tken : codeLines) {
            Token newToken = new Token();
            newToken.setValue(tken);

            //for color_def lexical analysis
            if (newToken.matchType("color_def")) {
                newToken = new Token();
                tken = tken.substring(1);
                newToken.setValue(tken.substring(0, 2));
                newToken.setType("color_def_red");
                tokens.add(newToken);

                newToken = new Token();

                newToken.setValue(tken.substring(2, 4));
                newToken.setType("color_def_green");
                tokens.add(newToken);

                newToken = new Token();
                newToken.setValue(tken.substring(4, 6));
                newToken.setType("color_def_blue");

            }
            tokens.add(newToken);
            if (newToken.getType().equals("")) {
                throw new LexicalException("Lexical exception: Not a valid lexeme:\"" + tken + "\"");

            }
        }
        return tokens;
    }
}
