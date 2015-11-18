package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;
import java.util.List;
import pl.pd.eeconverter.Iban2BicDirectoryItem;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public interface IEeParticipant {

    public LocalDate getValidFrom();

    public LocalDate getValidTo();
    
    public String getParticipantNumber();
    
    public List<Iban2BicDirectoryItem> getIban2BicDirectoryItem(List<Institution> institutions, List<SctParticipant> sctParticipants);

}
