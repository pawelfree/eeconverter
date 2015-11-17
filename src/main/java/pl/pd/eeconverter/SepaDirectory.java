package pl.pd.eeconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author paweldudek
 */
public class SepaDirectory {

    private final List<SepaDirectoryItem> items;

    public SepaDirectory() {
        items = new ArrayList<>();
    }

    public List<SepaDirectoryItem> getItems() {
        return items;
    }

    public void add(SepaDirectoryItem item) {
        items.add(item);
    }
    
    public void addAll(List<SepaDirectoryItem> items) {
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
