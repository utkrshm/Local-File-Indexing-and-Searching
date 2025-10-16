import java.util.*;
import java.util.regex.*;

public class SearchEngine {
    // term → docId → positions
    private Map<String, Map<Integer, List<Integer>>> invertedIndex = new HashMap<>();
    private Map<Integer, Document> documents = new HashMap<>();
    private static final Pattern WORD_PATTERN = Pattern.compile("\\w+");

    public void indexDocuments(List<Document> docs) {
        for (Document doc : docs) {
            documents.put(doc.getId(), doc);
            indexDocument(doc);
        }
    }

    private void indexDocument(Document doc) {
        Matcher matcher = WORD_PATTERN.matcher(doc.getContent().toLowerCase());
        int position = 0;

        while (matcher.find()) {
            String term = matcher.group();
            invertedIndex
                .computeIfAbsent(term, k -> new HashMap<>())
                .computeIfAbsent(doc.getId(), k -> new ArrayList<>())
                .add(position);
            position++;
        }
    }

    // Regular word query (existing functionality)
    public List<SearchResult> search(String query) {
        if (query.contains("\"")) {
            return phraseSearch(query);
        } else {
            return termSearch(query);
        }
    }

    private List<SearchResult> termSearch(String query) {
        Set<Integer> results = new HashSet<>();
        for (String term : query.toLowerCase().split("\\s+")) {
            if (invertedIndex.containsKey(term)) {
                results.addAll(invertedIndex.get(term).keySet());
            }
        }

        List<SearchResult> output = new ArrayList<>();
        for (int docId : results) {
            Document doc = documents.get(docId);
            output.add(new SearchResult(doc.getFilePath(), doc.getContent()));
        }

        return output;
    }

    // Phrase query logic: "deep learning model"
    private List<SearchResult> phraseSearch(String query) {
        String phrase = query.replaceAll("\"", "").toLowerCase().trim();
        String[] terms = phrase.split("\\s+");
        if (terms.length == 0) return Collections.emptyList();

        Map<Integer, List<Integer>> firstTermDocs = invertedIndex.get(terms[0]);
        if (firstTermDocs == null) return Collections.emptyList();

        List<Integer> matchingDocs = new ArrayList<>();

        for (int docId : firstTermDocs.keySet()) {
            boolean allMatch = true;
            for (int i = 1; i < terms.length; i++) {
                Map<Integer, List<Integer>> nextTermDocs = invertedIndex.get(terms[i]);
                if (nextTermDocs == null || !nextTermDocs.containsKey(docId)) {
                    allMatch = false;
                    break;
                }

                // Check positional adjacency
                boolean phraseFound = false;
                for (int pos : firstTermDocs.get(docId)) {
                    if (nextTermDocs.get(docId).contains(pos + i)) {
                        phraseFound = true;
                        break;
                    }
                }

                if (!phraseFound) {
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) {
                matchingDocs.add(docId);
            }
        }

        List<SearchResult> output = new ArrayList<>();
        for (int docId : matchingDocs) {
            Document doc = documents.get(docId);
            output.add(new SearchResult(doc.getFilePath(), doc.getContent()));
        }

        return output;
    }
}
