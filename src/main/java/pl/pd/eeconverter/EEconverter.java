package pl.pd.eeconverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import pl.pd.eeconverter.euroelixir.EeReplacement;
import pl.pd.eeconverter.euroelixir.IEeParticipant;
import pl.pd.eeconverter.euroelixir.SctParticipant;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public class EEconverter {

    public static void main(String[] args) {

        EEFiles instance = EEFiles.getInstance();

        if (args.length < 2) {
            System.out.println("Niewłaściwa liczba parametrów:\n\t"
                    + "pierwszy - data zbiorów bazowych (RRRRMMDD)\n\t"
                    + "drugi - data zbioru EACHA (RRMMDDrrmmdd)\n\t");

//                    "trzeci - (opcjonalny) względna ścieżka folderu ze zbiorami bazowmi\n\t"
//                    "czwarty - (opcjonalny) względna ścieżka do folderu zbiorów wynikowych"
            System.out.println("Parametry wywołania : ".concat(Arrays.toString(args)));

        } else if (instance.verifyParams(args[0], args[1])) {

            Constants.DATE_EURO_ELIXIR = args[0];
            Constants.DATE_EACHA = args[1];

            int len = args.length;
            if (len >= 3) {
                Constants.INPUT_FOLDER = args[2];
                instance.setSubfolder("zbiory bazowe");
            }
            if (len >= 4) {
                Constants.OUTPUT_FOLDER = args[3];
            }

            if (instance.verifyFilesExist()) {

//TODO collector zamiast przetwarzania listy - metoda filter                 
//TODO output folder
//TODO replacements
//TODO code reuse eefiles-read 
//TODO REMEMBER w nowym elixirze beda inne zbiory i inne kodowanie znakow (UTF-8)
//        instance.readParticipants("20151015").forEach(System.out::println);        
//        instance.readSctParticipants(DATE_EURO_ELIXIR).forEach(System.out::println); 
                try {
                    List<IEeParticipant> directs = instance.readDirectParticipants().collect(Collectors.toList());
                    List<IEeParticipant> indirects = instance.readIndirectParticipants().collect(Collectors.toList());
                    List<EeReplacement> replacements = instance.readReplacements().collect(Collectors.toList());
                    List<Institution> institutions = instance.readInstitutions().collect(Collectors.toList());
                    List<SctParticipant> sctParticipants = instance.readSctParticipants().collect(Collectors.toList());

//                    SepaDirectory dir = new SepaDirectory();
//
//                    instance.readEachaParticipants()
//                            .forEach(participant -> dir.add(participant.getDirectoryItem()));
//
//                    instance.writeFile("EA".concat(Constants.DATE_EACHA).concat(".txt"), dir.getLines());
//
//                    dir.clear();
//
//                    sctParticipants.stream()
//                            .filter(participant -> participant.getSctIndicator() > Constants.SCT)
//                            .forEach(participant -> dir.addAll(participant.getDirectoryItems(directs.stream(), indirects.stream(),
//                                    replacements.stream(), institutions)));
//
//                    instance.readDirectStep2Participants()
//                            .forEach(participant -> dir.add(participant.getDirectoryItem()));
//
//                    instance.readIndirectStep2Participants()
//                            .filter(participant -> !participant.getRepresentativeBic().contains(Constants.NBP_BIC))
//                            .forEach(participant -> dir.add(participant.getDirectoryItem()));
//
//                    instance.writeFile("ZB".concat(Constants.DATE_EURO_ELIXIR).concat(".txt"), dir.getLines());

                    Iban2BicDirectory idir = new Iban2BicDirectory();

                    directs.stream()
                            //.filter(participant -> participant.isSctParticipant(sctParticipants))
                            .forEach(participant -> idir.addAll(participant.getIban2BicDirectoryItem(institutions, sctParticipants)));

//                    indirects.stream()
//                            .filter(participant -> participant.isSctParticipant(sctParticipants))
//                            .forEach(participant -> idir.addAll(participant.getIban2BicDirectoryItem(institutions, sctParticipants)));
                    
                    instance.writeFile("I2B".concat(Constants.DATE_EURO_ELIXIR).concat(".txt"), idir.getLines());

                } catch (IOException ex) {
                    System.out.println("Coś poszło nie tak podczas generowania plików. " + ex.getLocalizedMessage());
                }

            } else {
                System.out.println("Nie znaleziono plików wejściowych.");
            }
        } else {
            System.out.println("Niewłaściwy format parametrów.");
        }
    }

}
