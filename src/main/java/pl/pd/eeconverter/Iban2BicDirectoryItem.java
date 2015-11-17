package pl.pd.eeconverter;

/**
 *
 * @author PawelDudek
 */
public class Iban2BicDirectoryItem {

    private String name = "";

    private String bic = "";

    private String countryCode = "";

    private String knr = "";

    private Iban2BicDirectoryItem() {
    }

    public Iban2BicDirectoryItem(String name, String bic, String countryCode, String knr) {
        this.name = name;
        this.bic = bic;
        this.countryCode = countryCode;
        this.knr = knr;
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
                .concat("\",\"SEPA\"");
        //.concat(System.lineSeparator());
    }

}
