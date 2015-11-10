package pl.pd.eeconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author paweldudek
 */
public class SepaDirectory {

    private List<SepaDirectoryItem> items;

    public SepaDirectory() {
        items = new ArrayList<>();
    }

    public List<SepaDirectoryItem> getItems() {
        return items;
    }

    public void setItems(List<SepaDirectoryItem> items) {
        this.items = items;
    }

    public void add(SepaDirectoryItem item) {
        items.add(item);
    }
    
    public void addAll(List<SepaDirectoryItem> items) {
        this.items.addAll(items);
    }

    public List<String> getLines() {
        return items.stream()
                .map(item -> mapper(item))
                .collect(Collectors.toList());
    }

    private String mapper(SepaDirectoryItem item) {
        if (Objects.isNull(item)) {
            return "";
        }
        return "".concat(item.getBic())
                .concat(",")
                .concat(item.getBankName().trim())
                .concat(",")
                .concat(item.getSource())
                .concat(",")
                .concat(item.getValidFrom())
                .concat(",")
                .concat(item.getValidTo())
                .concat(System.lineSeparator());
    }
}
