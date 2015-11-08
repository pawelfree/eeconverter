package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author paweldudek
 */
public class EeReplacement {
    
    public static final String FILE_MASK = "OZrrrrmm.Xdd";
    
    private final String takenOverNumber;

    private final String takerNumber;    
    
    private final LocalDate validFrom;

    private final LocalDate validTo;

    private EeReplacement(String takenOverNumber, String takerNumber, LocalDate validFrom, LocalDate validTo) {
        this.takenOverNumber = takenOverNumber;
        this.takerNumber = takerNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
    
    @Override
    public String toString() {
        return "EuroElixir replacements"
                .concat("\n\tTaken over number : ".concat(takenOverNumber))
                .concat("\n\tTaker number : ".concat(takerNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString());
    }

    public static EeReplacement getInstance(String line) {

        return new EeReplacement(line.substring(0, 8),
                line.substring(8,16),
                LocalDate.parse(line.subSequence(16,24), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(24, 32).trim().isEmpty() ? null : LocalDate.parse(line.substring(24,32), DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}
