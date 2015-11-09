package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import pl.pd.eeconverter.SepaDirectoryItem;

/**
 *
 * @author paweldudek
 */
public class Step2IndirectParticipant {
    
    public static final String FILE_MASK = "CUrrrrmm.Xdd";
    
    private final String participantBic;

    private final String participantName;

    private final LocalDate validFrom;

    private final LocalDate validTo;
    
    private final String representativeBic;

    private final String settlementBic;

    private final String status;

    private Step2IndirectParticipant(String participantBic, String participantName, LocalDate validFrom, LocalDate validTo,
            String representativeBic, String settlementBic, String status) {
        this.participantBic = participantBic;
        this.participantName = participantName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.representativeBic = representativeBic;
        this.settlementBic = settlementBic;
        this.status = status;
    }

    @Override
    public String toString() {
        return "EuroElixir indirect STEP 2 participant"
                .concat("\n\tParticipant BIC : ").concat(participantBic)                
                .concat("\n\tParticipant name : ".concat(participantName))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tRepresentative BIC : ").concat(representativeBic)
                .concat("\n\tSettlement BIC : ").concat(settlementBic)
                .concat("\n\tStatus : ").concat(status);
    }

    public static Step2IndirectParticipant getInstance(String line) {
        return new Step2IndirectParticipant(line.substring(0, 11).trim(),
                line.substring(11,61).trim(),
                LocalDate.parse(line.subSequence(61, 69), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(69, 77).trim().isEmpty() ? null : LocalDate.parse(line.substring(69, 77), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(77,85).trim(),
                line.substring(85,96).trim(),
                line.substring(96,97)
        );
    }
    
    public SepaDirectoryItem getDirectoryItem() {
        return new SepaDirectoryItem(participantBic, representativeBic, settlementBic, participantName, SourceId.SEPA_Indirect.ordinal(), validFrom,validTo, null);        
    }
    
}
