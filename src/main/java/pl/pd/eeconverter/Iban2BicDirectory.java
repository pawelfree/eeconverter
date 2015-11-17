package pl.pd.eeconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author PawelDudek
 */
public class Iban2BicDirectory {

    private final List<Iban2BicDirectoryItem> items;

    public Iban2BicDirectory() {
        items = new ArrayList<>();
    }

    public void add(Iban2BicDirectoryItem item) {
        items.add(item);
    }
    
    public void addAll(List<Iban2BicDirectoryItem> items) {
        this.items.addAll(items);
    }
    
    public void clear() {
        this.items.clear();
    }

    public List<String> getLines() {
        return items.stream()
                .map(item -> item.getLine())
                .collect(Collectors.toList());
    }    
    
}
