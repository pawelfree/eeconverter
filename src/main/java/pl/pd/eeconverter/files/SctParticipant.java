package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import pl.pd.eeconverter.SepaDirectoryItem;

/**
 *
 * @author paweldudek
 */
public class SctParticipant {
   
    public static final String FILE_MASK = "BKrrrrmm.Xdd";    
    
    private final String participantBic;
    
    private final String participantNumber;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private final String mainBicIndicator;
    
    private final int sctIndicator;

    public int getSctIndicator() {
        return sctIndicator;
    }

    private SctParticipant(String participantBic, String participantNumber, LocalDate validFrom, LocalDate validTo, 
            String mainBicIndicator, String sctIndicator) {
        this.participantBic = participantBic;
        this.participantNumber = participantNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.mainBicIndicator = mainBicIndicator;
        this.sctIndicator = Integer.parseInt(sctIndicator);
    }
    
    @Override
    public String toString() {
        return "EuroElixir SCT participant"
                .concat("\n\tParticipant BIC : ").concat(participantBic)
                .concat("\n\tParticipant number : ".concat(participantNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tMain BIC : ").concat(mainBicIndicator)
                .concat("\n\tSCT indicator : ").concat(String.valueOf(sctIndicator));
    }

    public static SctParticipant getInstance(String line) {

        return new SctParticipant(
                line.substring(0, 11),
                line.substring(11,19),
                LocalDate.parse(line.subSequence(19,27), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(27, 35).trim().isEmpty() ? null : LocalDate.parse(line.substring(27,35), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(35,36),
                line.substring(36,37)
        );
    }
    
        public SepaDirectoryItem getDirectoryItem() {
            return new SepaDirectoryItem(participantBic, "", 1, validFrom,validTo, null);
    }
}
