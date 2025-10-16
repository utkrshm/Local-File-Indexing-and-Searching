import java.util.*;

public class InvertedIndex {
    private final Map<String, Set<Integer>> index = new HashMap<>();

    public void add(String term, int docId) {
        index.computeIfAbsent(term, k -> new HashSet<>()).add(docId);
    }

    public Set<Integer> search(String term) {
        return index.getOrDefault(term.toLowerCase(), Collections.emptySet());
    }

    public Map<String, Set<Integer>> getIndex() {
        return index;
    }
}
