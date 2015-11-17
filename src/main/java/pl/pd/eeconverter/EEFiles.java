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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public class EEFiles {

    private static final Logger LOGGER = Logger.getLogger(EEFiles.class.getCanonicalName());

    private static final String NBP_BIC = "NBPLPLPW";

    private static final int NUMBER_OF_REQUIRED_FILES = 8;

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

    public boolean verifyParams(String param1, String param2) {
        String regex = "[0-9]{2}((0[1-9])|(1[0-2]))([0-2]\\d|(3[0|1]))";

        return Objects.nonNull(param1)
                && Objects.nonNull(param2)
                && param1.matches("^20".concat(regex).concat("$"))
                && param2.matches("^".concat(regex).concat(regex).concat("$"));
    }

    public boolean verifyFilesExist() {

        Path path = Constants.INPUT_FOLDER.isEmpty() ? Paths.get("") : Paths.get("", Constants.INPUT_FOLDER);

        List<String> lst = new ArrayList<>(Arrays.asList(
                replaceDate(EachaParticipant.FILE_MASK, Constants.DATE_EACHA.substring(0, 6), Constants.DATE_EACHA.substring(6, 12)),
                replaceDate(Step2DirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(Step2IndirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(SctParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(EeParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(EeIndirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(EeDirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR),
                replaceDate(Institution.FILE_MASK, Constants.DATE_EURO_ELIXIR)));

        return lst.stream().allMatch(item -> Files.exists(Paths.get(path.toString(), item)));
    }

    public Stream<EachaParticipant> readEachaParticipants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(EachaParticipant.FILE_MASK, Constants.DATE_EACHA.substring(0, 6), Constants.DATE_EACHA.substring(6, 12))))
                .map(item -> EachaParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<Step2DirectParticipant> readDirectStep2Participants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(Step2DirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> Step2DirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    /**
     * Excluded banks for which representative BIC is NBPLPLPW
     *
     * @param rrrrmmdd
     * @return
     */
    public Stream<Step2IndirectParticipant> readIndirectStep2Participants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(Step2IndirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> Step2IndirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()))
                .filter(participant -> !participant.getRepresentativeBic().contains(NBP_BIC));
    }

    /**
     * Returns stream of SCT Participants file for which SCT flag is > 0
     *
     * @return
     */
    public Stream<SctParticipant> readSctParticipants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(SctParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> SctParticipant.getInstance(item))
                .filter(item -> item.getSctIndicator() > 0)
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<EeReplacement> readReplacements() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(EeReplacement.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> EeReplacement.getInstance(item));
    }

    public Stream<EeParticipant> readParticipants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(EeParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> EeParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<EeDirectParticipant> readDirectParticipants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(EeDirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> EeDirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<EeIndirectParticipant> readIndirectParticipants() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(EeIndirectParticipant.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> EeIndirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<Institution> readInstitutions() throws IOException {
        return readFile(Paths.get("", subfolder, replaceDate(Institution.FILE_MASK, Constants.DATE_EURO_ELIXIR)))
                .map(item -> Institution.getInstance(item));
    }

    private Stream<String> readFile(Path path) throws IOException {
        return Files.lines(path, Charset.forName("CP852"));
    }

    private String replaceDate(String mask, String rrrrmmdd) {
        return mask.replace("rrrrmm", rrrrmmdd.substring(0, 6))
                .replace("dd", rrrrmmdd.substring(6, 8));
    }

    private String replaceDate(String mask, String rrmmdd, String RRMMDD) {
        return mask.replace("rrmmdd", rrmmdd).replace("RRMMDD", RRMMDD);
    }

    public void writeFile(String filename, List<String> list) {
        try {
            Files.write(Paths.get("", filename), list);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
