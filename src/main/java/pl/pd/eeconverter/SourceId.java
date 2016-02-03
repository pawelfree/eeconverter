package pl.pd.eeconverter;

/**
 *
 * @author paweldudek
 */
public enum SourceId {
    
    Manual(10,""),
    KIR_Direct(20,"EES"),
    KIR_Indirect(20,"EES"),
    EACHA(40,"EEM"),
    SEPA_Direct(30,"EEM"),
    SEPA_Indirect(30,"EEM"); 
    
    int priority;
    String systemId;
    
    SourceId(int priority, String systemId) {
        this.priority = priority;
        this.systemId = systemId;
    }
    
    public int priority() {
        return priority;
    }
    
    public String systemId() {
        return systemId;
    }
}
