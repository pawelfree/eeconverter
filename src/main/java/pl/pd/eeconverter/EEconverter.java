package pl.pd.eeconverter;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author paweldudek
 */
public class EEconverter {

    public static void main(String[] args) {

        Path currentRelativePath = Paths.get("");

        // TODO code application logic here
        EEFiles instance = EEFiles.getInstance();
        instance.setSubfolder("zbiory bazowe");
        if (instance.verifyFilesExist(currentRelativePath, "20151015")) {
            System.out.println("There are required files");
        } else {
            System.out.println("No files found");
        }

//        instance.readParticipants("20151015").forEach(System.out::println);
//        instance.readDirectParticipants("20151015").forEach(System.out::println);
//        instance.readIndirectParticipants("20151015").forEach(System.out::println);        
//        instance.readReplacements("20151015").forEach(System.out::println);         
//        instance.readSctParticipants("20151015").forEach(System.out::println);         
//        instance.readDirectStep2Participants("20151015").forEach(System.out::println);
//        instance.readIndirectStep2Participants("20151015").forEach(System.out::println);

//        instance.readEachaParticipants("151015","151015").forEach(System.out::println);

        SepaDirectory dir = new SepaDirectory();
        
        instance.readSctParticipants("20151015")
                .forEach(item -> dir.add(item.getDirectoryItem()));
                
//        instance.readDirectStep2Participants("20151015")
//                .forEach(item -> dir.add(item.getDirectoryItem()));
//        
//        instance.readIndirectStep2Participants("20151015")
//                .forEach(item -> dir.add(item.getDirectoryItem()));
//        
//        try {
//            Files.write(Paths.get("","test.txt"), dir.getLines());
//        } catch (IOException ex) {
//            Logger.getLogger(EEconverter.class.getName()).log(Level.SEVERE, null, ex);
//        }

        
        dir.getLines().stream().forEach(System.out::println);
    }

    //read EACHA participants - EArrmmddEEMMDD
}
