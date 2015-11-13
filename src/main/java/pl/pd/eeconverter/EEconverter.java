package pl.pd.eeconverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import pl.pd.eeconverter.euroelixir.EeReplacement;
import pl.pd.eeconverter.euroelixir.IEeParticipant;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public class EEconverter {
    
    private static final String DATE_EURO_ELIXIR = "20151113";
    
    private static final String DATE_EACHA = "151116151122";
        
    public static void main(String[] args) {


        
        Path currentRelativePath = Paths.get("");

        EEFiles instance = EEFiles.getInstance();
        instance.setSubfolder("zbiory bazowe");
        if (instance.verifyFilesExist(currentRelativePath, DATE_EURO_ELIXIR)) {
            System.out.println("There are required files");
        } else {
            System.out.println("No files found");
        }

//TODO replacements
//TODO dwa pliki
//TODO nazwa plikow wyjsciowych
//TODO nazwa pliku EACHA
//TODO poprawic kontrole plikow
//TODO dodac zbior bankow
//TODO code reuse eefiles-read 
//TODO REMEMBER w nowym elixirze beda inne zbiory i inne kodowanie znakow (UTF-8)

//        instance.readParticipants("20151015").forEach(System.out::println);        
//        instance.readSctParticipants(DATE_EURO_ELIXIR).forEach(System.out::println); 

        List<IEeParticipant> directs = instance.readDirectParticipants(DATE_EURO_ELIXIR).collect(Collectors.toList());
        List<IEeParticipant> indirects = instance.readIndirectParticipants(DATE_EURO_ELIXIR).collect(Collectors.toList());
        List<EeReplacement> replacements = instance.readReplacements(DATE_EURO_ELIXIR).collect(Collectors.toList());
        List<Institution> institutions = instance.readInstitutions(DATE_EURO_ELIXIR).collect(Collectors.toList());
        
        SepaDirectory dir = new SepaDirectory();

        instance.readEachaParticipants(DATE_EACHA)
                .forEach(item -> dir.add(item.getDirectoryItem()));
        instance.writeFile("EA".concat(DATE_EACHA).concat(".txt"), dir.getLines());

        dir.clear();
                
        instance.readSctParticipants(DATE_EURO_ELIXIR)
                .forEach(item -> dir.addAll(item.getDirectoryItem(directs.stream(), indirects.stream(),
                        replacements.stream(), institutions)));

        instance.readDirectStep2Participants(DATE_EURO_ELIXIR)
                .forEach(item -> dir.add(item.getDirectoryItem()));
        
        instance.readIndirectStep2Participants(DATE_EURO_ELIXIR)
                .forEach(item -> dir.add(item.getDirectoryItem()));
        
        instance.writeFile("ZB".concat(DATE_EURO_ELIXIR).concat(".txt"), dir.getLines());

        dir.getLines().stream().forEach(System.out::print);
    }

}
