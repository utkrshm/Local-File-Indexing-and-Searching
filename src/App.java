import java.util.*;

public class App {
    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine();

        // Load all files from the "docs" directory
        String directoryPath = "docs"; // or absolute path
        List<Document> loadedDocs = DocumentLoader.loadDocumentsFromDirectory(directoryPath);

        if (loadedDocs.isEmpty()) {
            System.out.println("No documents loaded. Exiting...");
            return;
        }

        // Add them to the engine
        for (Document doc : loadedDocs) {
            engine.addDocument(doc);
        }

        // Take continual input from the user for queries
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\nEnter search term (or 'exit'): ");
            String query = sc.nextLine();
            if (query.equalsIgnoreCase("exit")) break;

            List<Document> results = engine.search(query);
            if (results.isEmpty()) {
                System.out.println("No matches found.");
            } else {
                System.out.println("Matched Documents:");
                for (Document doc : results) {
                    System.out.println("Doc " + doc.getId() + ": " +
                        doc.getContent().substring(0, Math.min(80, doc.getContent().length())) + "...");
                }
            }
        }

        sc.close();
    }
}
