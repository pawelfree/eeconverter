package pl.pd.eeconverter;

import pl.pd.eeconverter.step2.Step2IndirectParticipant;
import pl.pd.eeconverter.step2.Step2DirectParticipant;
import pl.pd.eeconverter.eacha.EachaParticipant;
import pl.pd.eeconverter.euroelixir.SctParticipant;
import pl.pd.eeconverter.euroelixir.EeDirectParticipant;
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
import java.util.stream.Stream;
import pl.pd.eeconverter.kir.Institution;

/**
 *
 * @author paweldudek
 */
public class EEFiles {

    private static final String NBP_BIC = "NBPLPLPW";

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
                replaceDateEacha(EachaParticipant.FILE_MASK),
                replaceDateElixir(Step2DirectParticipant.FILE_MASK),
                replaceDateElixir(Step2IndirectParticipant.FILE_MASK),
                replaceDateElixir(SctParticipant.FILE_MASK),
                replaceDateElixir(EeIndirectParticipant.FILE_MASK),
                replaceDateElixir(EeDirectParticipant.FILE_MASK),
                replaceDateElixir(Institution.FILE_MASK)));

        return lst.stream().allMatch(item -> Files.exists(Paths.get(path.toString(), item)));
    }

    public Stream<EachaParticipant> readEachaParticipants() throws IOException {
        return readFile(replaceDateEacha(EachaParticipant.FILE_MASK))
                .map(item -> EachaParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<Step2DirectParticipant> readDirectStep2Participants() throws IOException {
        return readFile(replaceDateElixir(Step2DirectParticipant.FILE_MASK))
                .map(item -> Step2DirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    /**
     * Excluded banks for which representative BIC is NBPLPLPW
     *
     * @return
     */
    public Stream<Step2IndirectParticipant> readIndirectStep2Participants() throws IOException {
        return readFile(replaceDateElixir(Step2IndirectParticipant.FILE_MASK))
                .map(item -> Step2IndirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()))
                .filter(participant -> !participant.getRepresentativeBic().contains(NBP_BIC));
    }

    /**
     * Returns stream of SCT Participants file for which SCT flag is > 0
     *
     * @param sepa if participant is member of SCT schema
     * @return
     * @throws java.io.IOException
     */
    public Stream<SctParticipant> readSctParticipants(boolean sepa) throws IOException {
        return readFile(replaceDateElixir(SctParticipant.FILE_MASK))
                .map(item -> SctParticipant.getInstance(item))
                .filter(item -> sepa ? item.getSctIndicator() > 0 : true)
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<EeReplacement> readReplacements() throws IOException {
        return readFile(replaceDateElixir(EeReplacement.FILE_MASK))
                .map(item -> EeReplacement.getInstance(item));
    }

    public Stream<EeDirectParticipant> readDirectParticipants() throws IOException {
        return readFile(replaceDateElixir(EeDirectParticipant.FILE_MASK))
                .map(item -> EeDirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<EeIndirectParticipant> readIndirectParticipants() throws IOException {
        return readFile(replaceDateElixir(EeIndirectParticipant.FILE_MASK))
                .map(item -> EeIndirectParticipant.getInstance(item))
                .filter(participant -> Objects.isNull(participant.getValidTo()) ? true : !participant.getValidTo().isBefore(LocalDate.now()));
    }

    public Stream<Institution> readInstitutions() throws IOException {
        return readFile(replaceDateElixir(Institution.FILE_MASK))
                .map(item -> Institution.getInstance(item));
    }

    private Stream<String> readFile(String filename) throws IOException {
        Path path = Paths.get("", subfolder, filename);
        return Files.lines(path, Charset.forName("CP852"));
    }

    private String replaceDateElixir(String mask) {
        return mask.replace("rrrrmm", Constants.DATE_EURO_ELIXIR.substring(0, 6))
                .replace("dd", Constants.DATE_EURO_ELIXIR.substring(6, 8));
    }

    private String replaceDateEacha(String mask) {
        return mask.replace("rrmmdd", Constants.DATE_EACHA.substring(0, 6)).replace("RRMMDD", Constants.DATE_EACHA.substring(6, 12));
    }

    public void writeFile(String filename, List<String> list) throws IOException {
        Files.write(Paths.get("", filename), list);
    }
}
