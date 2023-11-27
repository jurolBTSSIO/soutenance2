package fr.cda.exception;

import fr.cda.tool.Util;
import org.apache.logging.log4j.Level;

public class SaisieIncorrectException extends Exception{
    public SaisieIncorrectException() {
        Util.logger.log(Level.ERROR, "Saisie incorrecte!");
    }
}
