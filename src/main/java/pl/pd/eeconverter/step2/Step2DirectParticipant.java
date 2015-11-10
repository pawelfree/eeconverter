package pl.pd.eeconverter.step2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import pl.pd.eeconverter.SepaDirectoryItem;
import pl.pd.eeconverter.SourceId;

/**
 *
 * @author paweldudek
 */
public class Step2DirectParticipant {
        
    public static final String FILE_MASK = "COrrrrmm.Xdd";

    private final String participantBic;

    private final String participantName;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private final String settlementBic;

    private final String status;

    public LocalDate getValidTo() {
        return validTo;
    }

    private Step2DirectParticipant(String participantBic, String participantName, LocalDate validFrom, LocalDate validTo,
            String settlementBic, String status) {
        this.participantBic = participantBic;
        this.participantName = participantName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.settlementBic = settlementBic;
        this.status = status;
    }

    @Override
    public String toString() {
        return "EuroElixir direct STEP 2 participant"
                .concat("\n\tParticipant BIC : ").concat(participantBic)                
                .concat("\n\tParticipant name : ".concat(participantName))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tSettlement BIC : ").concat(settlementBic)
                .concat("\n\tStatus : ").concat(status);
    }

    public static Step2DirectParticipant getInstance(String line) {
        return new Step2DirectParticipant(line.substring(0, 8),
                line.substring(8,58).trim(),
                LocalDate.parse(line.subSequence(58, 66), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(66, 74).trim().isEmpty() ? null : LocalDate.parse(line.substring(66, 74), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(74,85).trim(),
                line.substring(85,86)
        );
    }
    
    public SepaDirectoryItem getDirectoryItem() {
        return new SepaDirectoryItem(participantBic, participantName,SourceId.SEPA_Direct.ordinal(), validFrom,validTo, null);
    }
}