import java.util.*;

public class App {
    public static void main(String[] args) {
        List<Document> docs = DocumentLoader.loadDocumentsFromDirectory("docs");
        SearchEngine engine = new SearchEngine();
        engine.indexDocuments(docs);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter query (or 'exit'): ");
            String q = sc.nextLine();
            if (q.equalsIgnoreCase("exit")) break;

            List<SearchResult> results = engine.search(q);
            if (results.isEmpty()) {
                System.out.println("No results found.");
            } else {
                results.forEach(System.out::println);
            }
        }
        sc.close();
    }
}
