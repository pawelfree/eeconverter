package pl.pd.eeconverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author PawelDudek
 */
public class Iban2BicDirectoryItem {

    private String name = "";

    private String bic = "";

    private String countryCode = "";

    private String knr = "";
    
    private LocalDate validFrom;
    
    private LocalDate validTo;

    private Iban2BicDirectoryItem() {
    }

    public Iban2BicDirectoryItem(String name, String bic, String countryCode, String knr, LocalDate validFrom, LocalDate validTo) {
        this.name = name;
        this.bic = bic;
        this.countryCode = countryCode;
        this.knr = knr;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
    
    public String getValidFrom() {
        return validFrom.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

    public String getValidTo() {
        return Objects.isNull(validTo) ? "" : validTo.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }
    
    public String getLine() {
        return "\"".concat(name.trim())
                .concat("\",\"")
                .concat(countryCode)
                .concat("\",\"")
                .concat(bic)
                .concat("\",\"")
                .concat(bic)
                .concat("\",\"")
                .concat(knr)
                .concat("\",\"SEPA\",\"")
                .concat(getValidFrom())
                .concat("\",\"")
                .concat(getValidTo())
                .concat("\"");                
        //.concat(System.lineSeparator());
    }

}
