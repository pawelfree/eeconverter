package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author paweldudek
 */
public class EeDirectParticipant implements IEeParticipant {

    public static final String FILE_MASK = "OUrrrrmm.Xdd";    
    
    private final String participantNumber;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private EeDirectParticipant(String participantNumber, LocalDate validFrom, LocalDate validTo) {
        this.participantNumber = participantNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    @Override
    public String getParticipantNumber() {
        return participantNumber;
    }

    @Override
    public LocalDate getValidFrom() {
        return validFrom;
    }

    @Override
    public LocalDate getValidTo() {
        return validTo;
    }
    
    @Override
    public String toString() {
        return "EuroElixir direct participant"
                .concat("\n\tParticipant number : ".concat(participantNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString());
    }
    
    public static EeDirectParticipant getInstance(String line) {
        return new EeDirectParticipant(line.substring(0, 8),
                LocalDate.parse(line.subSequence(8,16), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(16, 24).trim().isEmpty() ? null : LocalDate.parse(line.substring(16,24), DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}
