package pl.pd.eeconverter;

import pl.pd.eeconverter.step2.Step2IndirectParticipant;
import pl.pd.eeconverter.step2.Step2DirectParticipant;
import pl.pd.eeconverter.eacha.EachaParticipant;
import pl.pd.eeconverter.euroelixir.SctParticipant;
import pl.pd.eeconverter.euroelixir.EeDirectParticipant;
import pl.pd.eeconverter.euroelixir.EeParticipant;
import pl.pd.eeconverter.euroelixir.EeIndirectParticipant;
import pl.pd.eeconverter.euroelixir.EeReplacement;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author paweldudek
 */
public class EEFiles {
    
    private static final Logger LOGGER = Logger.getLogger(EEFiles.class.getCanonicalName());
    
    private static final String NBP_BIC = "NBPLPLPW";
    
    private static final int NUMBER_OF_REQUIRED_FILES = 7;

    private String subfolder = "";
    
    private static EEFiles instance;
    
    private EEFiles() {
        
    }
   
    public static EEFiles getInstance() {
        if (null == instance) {
            return new EEFiles();
        } else {
            return instance;
        }
    }
    
    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }
    
    //TODO nie weryfikuje pliku EArrmmddRRMMDD
    public boolean verifyFilesExist(Path path, String rrrrmmdd) {

        Logger.getLogger(EEFiles.class.getName()).log(Level.INFO, "Current relative path is: ".concat(path.toAbsolutePath().toString()));
        
        final String regex = replaceDate(".*[B|O|C][U|K|Z|T|O]rrrrmm.[X]dd", rrrrmmdd);

        try {
            return Files.list(Paths.get(path.toString(),subfolder))
                        .filter(name -> name.toString().matches(regex))
                        .count() == NUMBER_OF_REQUIRED_FILES;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Stream<EachaParticipant> readEachaParticipants(String rrmmdd, String RRMMDD) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(EachaParticipant.FILE_MASK, rrmmdd, RRMMDD)))
                    .map(item -> EachaParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }
    
    public Stream<Step2DirectParticipant> readDirectStep2Participants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(Step2DirectParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> Step2DirectParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }

    /**
     * Excluded banks for which representative BIC is NBPLPLPW
     * 
     * @param rrrrmmdd
     * @return 
     */
    public Stream<Step2IndirectParticipant> readIndirectStep2Participants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(Step2IndirectParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> Step2IndirectParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()))
                    .filter(participant -> !participant.getRepresentativeBic().contains(NBP_BIC));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }    
    
    /**
     * Returns stream of SCT Participants file for which SCT flag is  > 0
     * 
     * @param rrrrmmdd
     * @return 
     */
    public Stream<SctParticipant> readSctParticipants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(SctParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> SctParticipant.getInstance(item))
                    .filter(item -> item.getSctIndicator() > 0)
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));            
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }    
    
    public Stream<EeReplacement> readReplacements(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(EeReplacement.FILE_MASK, rrrrmmdd)))
                    .map(item -> EeReplacement.getInstance(item));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }
        
    public Stream<EeParticipant> readParticipants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(EeParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> EeParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }
        
    public Stream<EeDirectParticipant> readDirectParticipants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(EeDirectParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> EeDirectParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }

    public Stream<EeIndirectParticipant> readIndirectParticipants(String rrrrmmdd) {
        try {
            return readFile(Paths.get("",subfolder,replaceDate(EeIndirectParticipant.FILE_MASK, rrrrmmdd)))
                    .map(item -> EeIndirectParticipant.getInstance(item))
                    .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
        } catch (IOException ex) {
            Logger.getLogger(EEFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Stream.empty();
    }
    
    private Stream<String> readFile(Path path) throws IOException {
        return Files.lines(path,Charset.forName("CP852"));
    }
    
    private String replaceDate(String mask, String rrrrmmdd) {
        return  mask.replace("rrrrmm", rrrrmmdd.substring(0, 6))
                    .replace("dd", rrrrmmdd.substring(6, 8));
    }
    
    private String replaceDate(String mask, String rrmmdd, String RRMMDD) {
        return mask.replace("rrmmdd", rrmmdd).replace("RRMMDD", RRMMDD);
    }
    
    public void writeFile(String filename, List<String> list) {
        try {
            Files.write(Paths.get("",filename), list);
        } catch (IOException ex) {
            Logger.getLogger(EEconverter.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
