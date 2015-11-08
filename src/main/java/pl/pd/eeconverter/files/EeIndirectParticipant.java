package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author paweldudek
 */
public class EeIndirectParticipant {
    
    public static final String FILE_MASK = "OKrrrrmm.Xdd";
    
    private final String participantNumber;

    private final String representativeNumber;    
    
    private final LocalDate validFrom;

    private final LocalDate validTo;

    private EeIndirectParticipant(String participantNumber, String representativeNumber, LocalDate validFrom, LocalDate validTo) {
        this.participantNumber = participantNumber;
        this.representativeNumber = representativeNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
    
    @Override
    public String toString() {
        return "EuroElixir indirect participant"
                .concat("\n\tParticipant number : ".concat(participantNumber))
                .concat("\n\tRepresentative number : ".concat(representativeNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString());
    }

    public static EeIndirectParticipant getInstance(String line) {

        return new EeIndirectParticipant(line.substring(0, 8),
                line.substring(8,16),
                LocalDate.parse(line.subSequence(16,24), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(24, 32).trim().isEmpty() ? null : LocalDate.parse(line.substring(24,32), DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}
