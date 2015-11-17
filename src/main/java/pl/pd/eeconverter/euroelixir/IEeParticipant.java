package pl.pd.eeconverter.euroelixir;

import java.time.LocalDate;

/**
 *
 * @author paweldudek
 */
public interface IEeParticipant {

    public LocalDate getValidFrom();

    public LocalDate getValidTo();
    
    public String getParticipantNumber();

}
