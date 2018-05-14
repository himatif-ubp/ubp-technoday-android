package org.gakendor.ubpdaily.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Dizzay on 12/28/2017.
 */

public class StringUtils {

    public static final boolean isNullOrEmpty(String value){
        if(value == null || value.isEmpty() || value.equals("") || value.equals("-")){
            return true;
        }else {
            return false;
        }
    }
}
