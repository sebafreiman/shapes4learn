package edu.maimonides.multimedia.shapes4learn.analysis;

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

        //Pattern (Javadoc): Creamos la Regex#
        //Mattcher: chequeamos una string contra un Regex.
        String[] codeLines = code.split("\\s");
        //StringUtils.splitPreserveAllTokens(code, ";");
        
        //Tipos de token: TokenFunciones,TokenID, TokenColorFed, TolenOpAris,Token(, Token)....Token;
        //usar StringUtils para no perder el ';'
        //regex a{0,5} para la clausura kleane pero para la letra 'a' 6 veces.
        //StringUtils;
        //mipatron.compile("[ ;\n]");
        for (String tken : codeLines) {
            Token newToken = new Token();
            newToken.setValue(tken);
            tokens.add(newToken);
            System.out.println( newToken.getType()+ " "  + newToken.getValue());
            if(newToken.getType().equals(""))
            {
                throw new LexicalException("Not a valid lexeme:\"" + tken +"\"");
                        
            }
        }
        return tokens;

        //sintacticio: logica embebida en el codigo
        //semantico:
        //el size no puede ser menor que 0.
        //optimizacion de codigo
        //generacion de codigo, creacion en shape
        //recorrer AST,
    }
}
