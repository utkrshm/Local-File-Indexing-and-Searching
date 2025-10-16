import java.util.*;

public class SearchResult implements Comparable<SearchResult> {
    private String filePath;
    private String snippet;
    private double score;

    private static final int CONTEXT_CHARS = 30; // number of chars before/after match

    public SearchResult(String filePath, String content, String query, double score) {
        this.filePath = filePath;
        this.snippet = buildContextualSnippet(content, query);
        this.score = score;
    }

    private String buildContextualSnippet(String content, String query) {
        String cleanQuery = query.replaceAll("\"", "").toLowerCase();
        String lowerContent = content.toLowerCase();

        List<String> snippets = new ArrayList<>();
        int index = 0;

        while ((index = lowerContent.indexOf(cleanQuery, index)) != -1) {
            int start = Math.max(0, index - CONTEXT_CHARS);
            int end = Math.min(content.length(), index + cleanQuery.length() + CONTEXT_CHARS);

            String snippet = content.substring(start, index)
                    + "**" + content.substring(index, index + cleanQuery.length()) + "**"
                    + content.substring(index + cleanQuery.length(), end) + "\n";

            snippets.add(snippet.trim());
            index += cleanQuery.length();
        }

        // Join multiple matches with ellipsis
        return String.join(" ... ", snippets);
    }

    @Override
    public String toString() {
        return String.format("Score: %.4f | File: %s\n%s", score, filePath, snippet);
    }

    @Override
    public int compareTo(SearchResult o) {
        return Double.compare(o.score, this.score); // descending order
    }
}
