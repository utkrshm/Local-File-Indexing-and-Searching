public class SearchResult {
    private String filePath;
    private String snippet;

    public SearchResult(String filePath, String snippet) {
        this.filePath = filePath;
        this.snippet = snippet.length() > 120 ? snippet.substring(0, 120) + "..." : snippet;
    }

    @Override
    public String toString() {
        return "Matched in: " + filePath + "\n" + snippet;
    }
}
