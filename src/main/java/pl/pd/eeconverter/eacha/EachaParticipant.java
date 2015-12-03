package pl.pd.eeconverter.eacha;

import pl.pd.eeconverter.SourceId;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import pl.pd.eeconverter.Constants;
import pl.pd.eeconverter.SepaDirectoryItem;

/**
 *
 * @author paweldudek
 */
public class EachaParticipant {

    public static final String FILE_MASK = "EArrmmddRRMMDD";

    private final String participantBic;

    private final String participantName;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private final LocalTime cutoffHour;

    private final String executionTime;

    private final String settlementSystemId;

    public LocalDate getValidTo() {
        return validTo;
    }

    private EachaParticipant(String participantBic, String participantName, LocalDate validFrom, LocalDate validTo,
            LocalTime cutoffHour, String executionTime, String settlementSystemId) {
        this.participantBic = participantBic;
        this.participantName = participantName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cutoffHour = Objects.isNull(cutoffHour) ? LocalTime.of(23,59) : cutoffHour;
        this.executionTime = executionTime;
        this.settlementSystemId = settlementSystemId;
    }

    @Override
    public String toString() {
        return "EuroElixir EACHA participant"
                .concat("\n\tParticipant BIC : ").concat(participantBic)
                .concat("\n\tParticipant name : ".concat(participantName))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tCut off hour : ").concat(cutoffHour.toString())
                .concat("\n\tExecution time : ").concat(executionTime)
                .concat("\n\tExternal settlement system : ").concat(settlementSystemId);
    }

    public static EachaParticipant getInstance(String line) {
        return new EachaParticipant(line.substring(0, 11).trim(),
                line.substring(11, 151).trim(),
                LocalDate.parse(line.subSequence(151, 159), DateTimeFormatter.ofPattern(Constants.OF_DATE)),
                line.substring(159, 167).trim().isEmpty() ? null : LocalDate.parse(line.substring(159, 167), DateTimeFormatter.ofPattern(Constants.OF_DATE)),
                line.substring(167, 171).trim().isEmpty() ? null : LocalTime.parse(line.substring(167, 171), DateTimeFormatter.ofPattern("HHmm")),
                line.substring(171, 172),
                line.substring(172, 180)
        );
    }

    public SepaDirectoryItem getDirectoryItem() {
        return new SepaDirectoryItem(participantBic, 
                "", 
                participantName, 
                SourceId.EACHA.priority(),
                SourceId.EACHA.systemId(),
                validFrom, 
                validTo, 
                cutoffHour);
    }
}
