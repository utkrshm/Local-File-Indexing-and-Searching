import java.util.*;

public class App {
    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine();

        // Sample documents
        engine.addDocument(new Document(1, "The quick brown fox jumps over the lazy dog"));
        engine.addDocument(new Document(2, "The quick red fox leaps over the sleepy cat"));
        engine.addDocument(new Document(3, "Java collections are powerful and flexible"));

        // Simple search
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter search term: ");
        String query = sc.nextLine();

        List<Document> results = engine.search(query);
        if (results.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Matched Documents:");
            for (Document doc : results) {
                System.out.println("Doc " + doc.getId() + ": " + doc.getContent());
            }
        }

        sc.close();
    }
}
