public class Document {
    private int id;
    private String content;
    private String filePath;

    public Document(int id, String content, String filePath) {
        this.id = id;
        this.content = content;
        this.filePath = filePath;
    }

    public int getId() { 
        return id; 
    }
    
    public String getContent() { 
        return content; 
    }
    
    public String getFilePath() { 
        return filePath; 
    }
}
