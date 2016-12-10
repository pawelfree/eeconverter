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
        this.cutoffTime = Objects.isNull(cutoffTime) ? LocalTime.of(23, 59) : cutoffTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.bic);
        hash = 97 * hash + Objects.hashCode(this.routingBic);
        hash = 97 * hash + Objects.hashCode(this.bankName);
        hash = 97 * hash + this.source;
        hash = 97 * hash + Objects.hashCode(this.validFrom);
        hash = 97 * hash + Objects.hashCode(this.validTo);
        hash = 97 * hash + Objects.hashCode(this.cutoffTime);
        hash = 97 * hash + Objects.hashCode(this.systemId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SepaDirectoryItem other = (SepaDirectoryItem) obj;
        if (this.source != other.source) {
            return false;
        }
        if (!Objects.equals(this.bic, other.bic)) {
            return false;
        }
        if (!Objects.equals(this.routingBic, other.routingBic)) {
            return false;
        }
        if (!Objects.equals(this.bankName, other.bankName)) {
            return false;
        }
        if (!Objects.equals(this.systemId, other.systemId)) {
            return false;
        }
        if (!Objects.equals(this.validFrom, other.validFrom)) {
            return false;
        }
        if (!Objects.equals(this.validTo, other.validTo)) {
            return false;
        }
        return Objects.equals(this.cutoffTime, other.cutoffTime);
    }
    
    public SepaDirectoryItem copyToBic8SepaDirectoryItem() {
        return new SepaDirectoryItem(bic.substring(0,8), routingBic, bankName, source, systemId, validFrom, validTo, cutoffTime);
    }

    public String getBic() {
        return bic;
    }

    public boolean isBicXxx() {
        if (bic.length() == 11) {
            return bic.substring(8).equalsIgnoreCase("XXX");
        } else {
            return false;
        }
    }
    
    public boolean isBicAbc() {
        return !isBic8() && !isBicXxx();
    }
    
    public boolean isBic8() {
        return bic.length() == 8;
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
