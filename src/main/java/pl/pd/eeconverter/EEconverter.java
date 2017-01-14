package pl.pd.eeconverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import pl.pd.eeconverter.euroelixir.EeParticipant;
import pl.pd.eeconverter.euroelixir.SctParticipant;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public class EEconverter {

    public static void main(String[] args) throws IOException {

        EEFiles instance = EEFiles.getInstance();
//                    "pierwszy - (opcjonalny) względna ścieżka folderu ze zbiorami bazowmi\n\t"
//                    "drugi - (opcjonalny) względna ścieżka do folderu zbiorów wynikowych"
        if (args.length == 1) {
            Constants.INPUT_FOLDER = args[0];
            instance.setInfolder(Constants.INPUT_FOLDER);

        } else if (args.length == 2) {
            
            Constants.INPUT_FOLDER = args[0];
            instance.setInfolder(Constants.INPUT_FOLDER);

            Constants.OUTPUT_FOLDER = args[1];
            instance.setOutfolder(Constants.OUTPUT_FOLDER);
        }

        if (instance.verifyEachaFileExist()) {
            try {
                SepaDirectory dir = new SepaDirectory();
                //EACHA directory
                instance.readEachaParticipants()
                        .forEach(participant -> dir.add(participant.getDirectoryItem()));

                instance.writeFile("EA".concat(Constants.DATE_EACHA), dir.getLines());
                System.out.println("Plik EA".concat(Constants.DATE_EACHA).concat(" został wygenerowany."));
            } catch (IOException ex) {
                System.out.println("Coś poszło nie tak podczas generowania pliku EA. " + ex.getLocalizedMessage());
            }
        } else {
            System.out.println("Nie znaleziono plików wejściowych EACHA.");
        }

        if (instance.verifyElixirFilesExist()) {

            try {
                List<EeParticipant> directs = instance.readDirectParticipants().collect(Collectors.toList());
                List<EeParticipant> indirects = instance.readIndirectParticipants().collect(Collectors.toList());
                List<Institution> institutions = instance.readInstitutions().collect(Collectors.toList());
                List<SctParticipant> sctParticipants = instance.readSctParticipants().collect(Collectors.toList());

                SepaDirectory dir = new SepaDirectory();

                //SEPA directory

                sctParticipants.stream()
                        .filter(participant -> participant.getSctIndicator() > Constants.SCT)
                        .forEach(participant -> dir.addAll(participant.getDirectoryItems(directs.stream(), indirects.stream(), institutions)));

                instance.readDirectStep2Participants()
                        .forEach(participant -> dir.add(participant.getDirectoryItem()));

                instance.readIndirectStep2Participants()
                        .filter(participant -> !participant.getRepresentativeBic().contains(Constants.NBP_BIC))
                        .forEach(participant -> dir.add(participant.getDirectoryItem()));

                dir.clearBicXxx();

                instance.writeFile("ZB".concat(Constants.DATE_EURO_ELIXIR), dir.getLines());
                System.out.println("Plik ZB".concat(Constants.DATE_EURO_ELIXIR).concat(" został wygenerowany."));
                
                //IBAN 2 BIC directory
                Iban2BicDirectory idir = new Iban2BicDirectory();

                directs.addAll(indirects);

                directs.stream()
                        .forEach(participant -> idir.addAll(participant.getIban2BicDirectoryItem(institutions,
                                sctParticipants,
                                participant.getRepresentativeNumber(),
                                participant.getParticipantNumber())));

                instance.writeFile("I2B".concat(Constants.DATE_EURO_ELIXIR), idir.getLines());
                System.out.println("Plik I2B".concat(Constants.DATE_EURO_ELIXIR).concat(" został wygenerowany."));
                
            } catch (IOException ex) {
                System.out.println("Coś poszło nie tak podczas generowania plików I2B lub ZB. " + ex.getLocalizedMessage());
            }

        } else {
            System.out.println("Nie znaleziono plików wejściowych elixir.");
        }
    }

}
