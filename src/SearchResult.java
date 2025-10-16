public class SearchResult implements Comparable<SearchResult> {
    private String filePath;
    private String snippet;
    private double score;

    public SearchResult(String filePath, String snippet, double score) {
        this.filePath = filePath;
        this.snippet = snippet.length() > 120 ? snippet.substring(0, 120) + "..." : snippet;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("Score: %.4f | File: %s \n%s", score, filePath, snippet);
    }

    @Override
    public int compareTo(SearchResult o) {
        return Double.compare(o.score, this.score); // descending order
    }
}
