package com.skillkit.utils;

/**
 * Created by Allan on 20/09/2015.
 */
import static com.skillkit.utils.Constants.*;
public class StringValidations {

    public Boolean validateUsername(String username){

        if ((username != null)||(!(username.isEmpty()))){
            if(!((username.contains(EMPTY_SPACE)) ||(username.contains(EXCLAMATION_MARK))
               ||(username.contains(SLASH)) ||(username.contains(PLUS))
               ||(username.contains(OPEN)) || (username.contains(OPEN2))
               ||(username.contains(CLOSE)) || (username.contains(CLOSE2))
               ||(username.contains(HASH)) || (username.contains(PIPE))
               ||(username.contains(AT)) || (username.contains(MULT))
               ||(username.contains(GREAT)) || (username.contains(LESS))
               ||(username.contains(SYMB1)) || (username.contains(SYMB2))
               ||(username.contains(DOLAR)) || (username.contains(PORCENT))
               ||(username.contains(ADMIRATION_MARK)) || (username.contains(DOT_AND_COMA))
               ||(username.contains(EQUAL_KEY)))){
                return true;
            }
        }
        return false;
    }
}
