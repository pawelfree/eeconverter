package pl.pd.eeconverter.euroelixir;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author PawelDudek
 */
public class CollectorList {

    private final List<SctParticipant> list;

    public CollectorList() {
        list = new ArrayList<>();
    }

    public boolean add(SctParticipant e) {
        if (!list.contains(e)) {
            return list.add(e);
        } else {
            return false;
        }
    }

    private Stream<SctParticipant> stream() {
        return list.stream();
    }

    public CollectorList addAll(CollectorList l) {
        list.addAll(l.stream()
                .filter(item -> !list.contains(item))
                .collect(Collectors.toList()));
        return this;
    }

    public List<SctParticipant> finish() {
        return list;
    }
}
