package pl.pd.eeconverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public void add(SepaDirectoryItem item) {
        if (Objects.nonNull(item))
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

    /*
    * zamiana BIC+XXX na BIC
     */
    public void clearBicXxx() {
        Set<SepaDirectoryItem> set = new HashSet<>(items.stream().filter(item -> item.isBicAbc() || item.isBic8()).collect(Collectors.toSet()));
        set.addAll(items.stream().filter(item -> item.isBicXxx()).map(item -> item.copyToBic8SepaDirectoryItem()).collect(Collectors.toSet()));
        items = new ArrayList<>(set);
    }
}
