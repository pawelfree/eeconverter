package pl.pd.eeconverter.files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.pd.eeconverter.SepaDirectoryItem;

/**
 *
 * @author paweldudek
 */
public class SctParticipant {

    public static final String FILE_MASK = "BKrrrrmm.Xdd";

    private final String participantBic;

    private final String participantNumber;

    private final LocalDate validFrom;

    private final LocalDate validTo;

    private final String mainBicIndicator;

    private final int sctIndicator;

    public int getSctIndicator() {
        return sctIndicator;
    }

    private SctParticipant(String participantBic, String participantNumber, LocalDate validFrom, LocalDate validTo,
            String mainBicIndicator, String sctIndicator) {
        this.participantBic = participantBic;
        this.participantNumber = participantNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.mainBicIndicator = mainBicIndicator;
        this.sctIndicator = Integer.parseInt(sctIndicator);
    }

    @Override
    public String toString() {
        return "EuroElixir SCT participant"
                .concat("\n\tParticipant BIC : ").concat(participantBic)
                .concat("\n\tParticipant number : ".concat(participantNumber))
                .concat("\n\tValid from : ").concat(validFrom.toString())
                .concat("\n\tValid to   : ").concat(Objects.isNull(validTo) ? "" : validTo.toString())
                .concat("\n\tMain BIC : ").concat(mainBicIndicator)
                .concat("\n\tSCT indicator : ").concat(String.valueOf(sctIndicator));
    }

    public static SctParticipant getInstance(String line) {

        return new SctParticipant(
                line.substring(0, 11),
                line.substring(11, 19),
                LocalDate.parse(line.subSequence(19, 27), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(27, 35).trim().isEmpty() ? null : LocalDate.parse(line.substring(27, 35), DateTimeFormatter.ofPattern("yyyyMMdd")),
                line.substring(35, 36),
                line.substring(36, 37)
        );
    }

    public List<SepaDirectoryItem> getDirectoryItem(Stream<EeDirectParticipant> directs, Stream<EeIndirectParticipant> indirects, Stream<EeReplacement> replacements) {
        int sourceId;
        List<SepaDirectoryItem> list = new ArrayList<>();

        List<EeDirectParticipant> di = directs.filter(item -> item.getParticipantNumber().equalsIgnoreCase(this.participantNumber)).collect(Collectors.toList());
        sourceId = SourceId.KIR_Direct.ordinal();
        for (EeDirectParticipant participant : di) {
            List<Dates> dates = getValidityDates(new Dates(participant.getValidFrom(), participant.getValidTo()));
            for (Dates date : dates) {
                list.add(new SepaDirectoryItem(participantBic, participantBic, participantBic, "", sourceId, date.from, date.to, null));
            }
        }

        List<EeIndirectParticipant> in = indirects.filter(item -> item.getParticipantNumber().equalsIgnoreCase(this.participantNumber)).collect(Collectors.toList());
        sourceId = SourceId.KIR_Indirect.ordinal();
        //TODO functional + code reuse above
        for (EeIndirectParticipant participant : in) {
            List<Dates> dates = getValidityDates(new Dates(participant.getValidFrom(), participant.getValidTo()));
            for (Dates date : dates) {
                list.add(new SepaDirectoryItem(participantBic, participantBic, participantBic, "", sourceId, date.from, date.to, null));
            }
        }

        return list;
    }

    /**
     * returning list to avoid returning null
     *
     * @param ee
     * @return
     */
    private List<Dates> getValidityDates(Dates ee) {
        List<Dates> dates = new ArrayList<>();

        if (Objects.isNull(validTo) && Objects.isNull(ee.to)) {
            //1. sct_do==null & ee_do==null
            if (validFrom.isAfter(ee.from) || validFrom.isEqual(ee.from)) {
                //1.1 sct_od >= ee_od => sct_od, null    
                dates.add(new Dates(validFrom, null));
            } else {
                //1.2 sct_od <  ee_od =>  ee_od, null
                dates.add(new Dates(ee.from, null));
            }
            ////koniec 1
        } else if (Objects.nonNull(validTo) && Objects.isNull(ee.to)) {
            //2. sct_do!=null & ee_do==null
            if (validFrom.isAfter(ee.from) || validFrom.isEqual(ee.from)) {
                //2.1 sct_od >= ee_od => sct_od ,sct_do
                dates.add(new Dates(validFrom, validTo));
            } else //2.2 sct_od <  ee_od =>  
            {
                if (validTo.isAfter(ee.from) || validTo.isEqual(ee.from)) {
                    //2.2.1               sct_do >= ee_od => ee_od, sct_do
                } else {
                    //2.2.2               sct_do <  ee_od => null, null
                    //do nothing
                }
            }
            ////koniec 2
        } else if (Objects.isNull(validTo) && Objects.nonNull(ee.to)) {
            //3. sct_do==null & ee_do!=null
            if (validFrom.isAfter(ee.from) || validFrom.isEqual(ee.from)) {
                //3.1 sct_od >= ee_od => 
                if (validFrom.isAfter(ee.to)) {
                    //3.1.1               sct_od >  ee_do => null, null
                    //do nothing
                } else {
                    //3.1.2               sct_od <= ee_do => sct_od, ee_do
                    dates.add(new Dates(validFrom, ee.to));
                }
            } else {
                //3.2 sct_od < ee_od => ee_od, ee_do
                dates.add(new Dates(ee.from, ee.to));
            }
            ////koniec 3
        } else if (Objects.nonNull(validTo) && Objects.nonNull(ee.to)) {
            //4. sct_do!=null & ee_do!=null
            if (validFrom.isAfter(ee.from) || validFrom.isEqual(ee.from)) {
                //4.1 sct_od >= ee_od
                if (validFrom.isAfter(ee.to)) {
                    //4.1.1               sct_od >  ee_do => null, null
                    //do nothing
                } else //4.1.2               sct_od <= ee_do
                 if (validTo.isAfter(ee.to) || validTo.isEqual(ee.to)) {
                        //4.1.2.1                           sct_do >= ee_do => sct_od, ee_do
                        dates.add(new Dates(validFrom, ee.to));
                    } else {
                        //4.1.2.2                           sct_do <  ee_do => sct_od, sct_do
                        dates.add(new Dates(validFrom, validTo));
                    }
            } else //4.2 sct_od <  ee_od
            {
                if (validTo.isBefore(ee.from)) {
                    //4.2.1               sct_do <  ee_od => null, null
                    //do nothing
                } else //4.2.2               sct_do >= ee_od
                if (validTo.isAfter(ee.to) || validTo.isEqual(ee.to)) {
                    //4.2.2.1                           sct_do >= ee_do => ee_od, ee_do
                    dates.add(new Dates(ee.from, ee.to));
                } else {
                    //4.2.2.2                           sct_do <  ee_do => ee_od, sct_do
                    dates.add(new Dates(ee.from, validTo));
                } ////koniec 4
            }
        }

        return dates;
    }

    private class Dates {

        public LocalDate from;
        public LocalDate to;

        public Dates(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }

    }

}
