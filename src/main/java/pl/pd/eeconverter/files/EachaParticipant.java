package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    private EachaParticipant(String participantBic, String participantName, LocalDate validFrom, LocalDate validTo,
            LocalTime cutoffHour, String executionTime, String settlementSystemId) {
        this.participantBic = participantBic;
        this.participantName = participantName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cutoffHour = cutoffHour;
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
        return new EachaParticipant(line.substring(0, 11),
                line.substring(11,151),
                LocalDate.parse(line.subSequence(151, 159), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(159, 167).trim().isEmpty() ? null : LocalDate.parse(line.substring(159, 167), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(167, 171).trim().isEmpty() ? null : LocalTime.parse(line.substring(167, 171), DateTimeFormatter.ofPattern("hhmm")),
                line.substring(171,172),
                line.substring(172,180)
        );
    }    
    
}
