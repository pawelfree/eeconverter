package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import pl.pd.eeconverter.Iban2BicDirectoryItem;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public abstract class EeParticipant {

    public abstract LocalDate getValidFrom();

    public abstract LocalDate getValidTo();

    public abstract String getParticipantNumber();
    
    public abstract String getRepresentativeNumber();

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

    public List<Iban2BicDirectoryItem> getIban2BicDirectoryItem(List<Institution> institutions, 
            List<SctParticipant> sctParticipants,
            String representativeNumber,
            String participantNumber) {
        List<SctParticipant> lst = sctParticipants.stream()
                .filter(sctParticipant -> (sctParticipant.getParticipantNumber().compareToIgnoreCase(representativeNumber) == 0))
                .collect(Collectors.toList());

        return filter(lst).stream()
                .map(sctParticipant -> new Iban2BicDirectoryItem(sctParticipant.getInstitutionName(institutions),
                        sctParticipant.getBic(),
                        sctParticipant.getBic().substring(4, 6),
                        participantNumber, sctParticipant.getValidFrom(), getValidTo()))
                .collect(Collectors.toList());
    }    
    
}
