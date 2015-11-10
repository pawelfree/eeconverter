package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Stores information about participant of EE system
 *
 * @author paweldudek
 */
public class EeParticipant {

    public static final String FILE_MASK = "BUrrrrmm.Xdd";    
    
    private final String participantNumber;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private final String headquartersNumber;

    public LocalDate getValidTo() {
        return validTo;
    }

    private EeParticipant(String participantNumber, LocalDate validFrom, LocalDate validTo, String headquartersNumber) {
        this.participantNumber = participantNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.headquartersNumber = headquartersNumber;
    }
    
    @Override
    public String toString() {
        return "EuroElixir participant"
                .concat("\n\tParticipant number : ".concat(participantNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tParticipant headquarters number : ").concat(headquartersNumber);
    }

    public static EeParticipant getInstance(String line) {

        return new EeParticipant(line.substring(0, 3),
                LocalDate.parse(line.subSequence(3,11), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(11, 19).trim().isEmpty() ? null : LocalDate.parse(line.substring(11,19), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(21,29));
    }

}
