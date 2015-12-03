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
    private final String systemId;

    public SepaDirectoryItem(String bic, String routingBic, String bankName, int source, String systemId, LocalDate validFrom, LocalDate validTo, LocalTime cutoffTime) {
        this.bic = bic;
        this.routingBic = routingBic;
        this.bankName = bankName;
        this.source = source;
        this.systemId = systemId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cutoffTime = Objects.isNull(cutoffTime) ? LocalTime.of(23,59) : cutoffTime;
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
        return validFrom.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

    public String getValidTo() {
        return Objects.isNull(validTo) ? "" : validTo.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

    public String getCutoffTime() {
        return cutoffTime.format(DateTimeFormatter.ofPattern(Constants.HOUR_FORMAT));
    }

    public String getRoutingBic() {
        return Objects.isNull(routingBic) ? "" : routingBic;
    }
    
    public String getSystemId() {
        return systemId;
    }

    public String getLine() {
        return "\"".concat(getBic())
                .concat("\",\"")
                .concat(getRoutingBic())
                .concat("\",\"")
                .concat(getBankName().trim())
                .concat("\",\"")
                .concat(getSource())
                .concat("\",\"")
                .concat(getSystemId())
                .concat("\",\"")                
                .concat(getValidFrom())
                .concat("\",\"")
                .concat(getValidTo())
                .concat("\",\"")
                .concat(getCutoffTime())
                .concat("\"");
            //.concat(System.lineSeparator());
    }
}
