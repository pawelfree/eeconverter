package pl.pd.eeconverter.kir;

/**
 *
 * @author paweldudek
 */
public class Institution {
    public static final String FILE_MASK = "BArrrrmm.0dd";    
    
    private final String institutionNumber;
    
    private final String institutionName;

    private Institution(String institutionNumber, String institutionName) {
        this.institutionNumber = institutionNumber;
        this.institutionName = institutionName;
    }

    public String getInstitutionNumber() {
        return institutionNumber;
    }

    public String getInstitutionName() {
        return institutionName;
    }
    
    @Override
    public String toString() {
        return "EuroElixir participant"
                .concat("\n\tInstitution number : ".concat(institutionNumber))
                .concat("\n\tInstitution name : ".concat(institutionName));
    }

    public static Institution getInstance(String line) {

        return new Institution(line.substring(0, 3),
                line.substring(3,38).trim().concat(" ").concat(line.substring(38,73).trim()));
    }
}
