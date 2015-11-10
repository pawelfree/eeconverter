/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
