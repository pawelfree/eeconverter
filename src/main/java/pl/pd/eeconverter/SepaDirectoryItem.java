package pl.pd.eeconverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author paweldudek
 */
public class SepaDirectoryItem {

    private final String bic;
    private final String routingBic;
    private final String bankName;
    private final int source;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final LocalTime cutoffTime;

    public SepaDirectoryItem(String bic, String routingBic, String bankName, int source, LocalDate validFrom, LocalDate validTo, LocalTime cutoffTime) {
        this.bic = bic;
        this.routingBic = routingBic;
        this.bankName = bankName;
        this.source = source;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cutoffTime = cutoffTime;
    }

    public String getBic() {
        return bic;
    }

    public String getBankName() {
        return bankName;
    }

    public String getSource() {
        return String.valueOf(source);
    }

    public String getValidFrom() {
        return validFrom.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String getValidTo() {
        return Objects.isNull(validTo) ? "" : validTo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String getCutoffTime() {
        return Objects.isNull(cutoffTime) ? "" : cutoffTime.format(DateTimeFormatter.ofPattern("hhmm"));
    }
    
    public String getRoutingBic() {
        return Objects.isNull(routingBic) ? "" : routingBic;
    }
       
}
