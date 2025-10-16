import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class DocumentLoader {

    public static List<Document> loadDocumentsFromDirectory(String dirPath) {
        List<Document> documents = new ArrayList<>();
        File folder = new File(dirPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Directory not found: " + dirPath);
            return documents;
        }

        File[] files = folder.listFiles((d, name) -> name.endsWith(".txt") || name.endsWith(".pdf"));
        if (files == null || files.length == 0) {
            System.err.println("No PDF/TXT files found in: " + dirPath);
            return documents;
        }

        int id = 1;
        for (File file : files) {
            try {
                String content = extractText(file);
                if (content != null && !content.trim().isEmpty()) {
                    documents.add(new Document(id++, content));
                    System.out.println("Loaded: " + file.getName());
                }
            } catch (Exception e) {
                System.err.println("Error reading " + file.getName() + ": " + e.getMessage());
            }
        }
        return documents;
    }

    private static String extractText(File file) throws IOException {
        if (file.getName().endsWith(".txt")) {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

        } else if (file.getName().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        }
        return null;
    }
}
