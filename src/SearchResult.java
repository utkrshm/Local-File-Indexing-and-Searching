public class SearchResult {
    private int docId;
    private String snippet;

    public SearchResult(int docId, String snippet) {
        this.docId = docId;
        this.snippet = snippet.length() > 120 ? snippet.substring(0, 120) + "..." : snippet;
    }

    public String toString() {
        return "Doc " + docId + ": " + snippet;
    }
}
