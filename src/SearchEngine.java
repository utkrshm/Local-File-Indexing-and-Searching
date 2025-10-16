import java.util.*;

public class SearchEngine {
    private final Map<Integer, Document> documents = new HashMap<>();
    private final InvertedIndex invertedIndex = new InvertedIndex();

    // Optional: Store term frequencies for TF-IDF later
    private final Map<Integer, Map<String, Integer>> termFrequencies = new HashMap<>();

    // --- Indexing ---
    public void addDocument(Document doc) {
        documents.put(doc.getId(), doc);
        String[] tokens = tokenize(doc.getContent());
        Map<String, Integer> tf = new HashMap<>();

        for (String token : tokens) {
            token = token.toLowerCase();
            invertedIndex.add(token, doc.getId());
            tf.put(token, tf.getOrDefault(token, 0) + 1);
        }
        termFrequencies.put(doc.getId(), tf);
    }

    private String[] tokenize(String text) {
        return text.split("[^a-zA-Z]+");
    }

    // --- Basic Keyword Search ---
    public List<Document> search(String query) {
        query = query.toLowerCase();
        Set<Integer> resultIds = invertedIndex.search(query);
        List<Document> results = new ArrayList<>();
        for (int id : resultIds) {
            results.add(documents.get(id));
        }
        return results;
    }

    public Map<Integer, Map<String, Integer>> getTermFrequencies() {
        return termFrequencies;
    }

    public Map<Integer, Document> getDocuments() {
        return documents;
    }
}
