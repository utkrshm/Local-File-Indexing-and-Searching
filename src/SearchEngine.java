import java.util.*;
import java.util.regex.*;

public class SearchEngine {
    private Map<String, Map<Integer, List<Integer>>> invertedIndex = new HashMap<>();
    private Map<Integer, Document> documents = new HashMap<>();
    private Map<Integer, Integer> docLengths = new HashMap<>();
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
        int length = 0;

        while (matcher.find()) {
            String term = matcher.group();
            invertedIndex
                .computeIfAbsent(term, k -> new HashMap<>())
                .computeIfAbsent(doc.getId(), k -> new ArrayList<>())
                .add(position);
            position++;
            length++;
        }

        docLengths.put(doc.getId(), length);
    }

    public List<SearchResult> search(String query) {
        boolean phrase = query.contains("\"");
        Map<Integer, Double> scores;

        if (phrase) {
            List<Integer> matchingDocs = phraseSearchIds(query);
            scores = computeTfIdfScores(query, matchingDocs);
        } else {
            List<Integer> matchingDocs = termSearchIds(query);
            scores = computeTfIdfScores(query, matchingDocs);
        }

        List<SearchResult> results = new ArrayList<>();
        for (int docId : scores.keySet()) {
            Document doc = documents.get(docId);
            results.add(new SearchResult(doc.getFilePath(), doc.getContent(), scores.get(docId)));
        }

        Collections.sort(results);
        return results;
    }

    // Return list of matching doc IDs (terms only)
    private List<Integer> termSearchIds(String query) {
        Set<Integer> resultIds = new HashSet<>();
        for (String term : query.toLowerCase().split("\\s+")) {
            if (invertedIndex.containsKey(term)) {
                resultIds.addAll(invertedIndex.get(term).keySet());
            }
        }
        return new ArrayList<>(resultIds);
    }

    // Return list of matching doc IDs (phrase)
    private List<Integer> phraseSearchIds(String query) {
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

            if (allMatch) matchingDocs.add(docId);
        }

        return matchingDocs;
    }

    // Compute TF-IDF scores for a set of documents
    private Map<Integer, Double> computeTfIdfScores(String query, List<Integer> docIds) {
        Map<Integer, Double> scores = new HashMap<>();
        String[] terms = query.replaceAll("\"", "").toLowerCase().split("\\s+");
        int N = documents.size();

        for (int docId : docIds) {
            double score = 0.0;
            for (String term : terms) {
                Map<Integer, List<Integer>> postings = invertedIndex.get(term);
                if (postings != null && postings.containsKey(docId)) {
                    double tf = postings.get(docId).size() / (double) docLengths.get(docId);
                    double idf = Math.log((N + 1.0) / (postings.size() + 1.0)) + 1; // smoothed
                    score += tf * idf;
                }
            }
            scores.put(docId, score);
        }
        return scores;
    }
}
