package org.gakendor.ubpdaily.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Dizzay on 12/28/2017.
 */

public class IDRUtils {

    public static final String toRupiah(double harga){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//        DecimalFormat kursIndonesia = new DecimalFormat("#");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(harga).replaceAll("\\,00", "");
    }

    public static final String toAccounting(double harga){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//        DecimalFormat kursIndonesia = new DecimalFormat("#");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(harga).replaceAll("\\,00", "");
    }
}
