package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import pl.pd.eeconverter.Iban2BicDirectoryItem;
import pl.pd.eeconverter.kir.Institution;

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
                LocalDate.parse(line.subSequence(8, 16), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(16, 24).trim().isEmpty() ? null : LocalDate.parse(line.substring(16, 24), DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    @Override
    public List<Iban2BicDirectoryItem> getIban2BicDirectoryItem(List<Institution> institutions, List<SctParticipant> sctParticipants) {
        List<SctParticipant> lst = sctParticipants.stream()
                .filter(sctParticipant -> (sctParticipant.getParticipantNumber().compareToIgnoreCase(participantNumber) == 0))
                .collect(Collectors.toList());

        return filter(lst).stream()
                .map(sctParticipant -> new Iban2BicDirectoryItem(sctParticipant.getInstitutionName(institutions),
                        sctParticipant.getBic(),
                        sctParticipant.getBic().substring(4, 6),
                        participantNumber, sctParticipant.getValidFrom(), sctParticipant.getValidTo()))
                .collect(Collectors.toList());

    }

    private List<SctParticipant> filter(List<SctParticipant> lst) {
        List<SctParticipant> list = lst.stream()
                .filter(item -> item.isMainBic())
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            return lst.stream().collect(Collector.of(
                    CollectorList::new,
                    CollectorList::add,
                    CollectorList::addAll,
                    CollectorList::finish)
            );
        } else {
            return list;
        }
    }

}
