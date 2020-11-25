import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CreateIndex {

    // Directory where the search index will be saved
    private static final String INDEX_DIRECTORY = "index";

    public static void main(String[] args) throws IOException {

        // Analyzer that is used to process TextField
        Analyzer analyzer = new StandardAnalyzer();

        // Store index on disk
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // Set open mode to create new index
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter iwriter = new IndexWriter(directory, config);

        // ArrayList to store documents after parsing
        ArrayList<Document> documents = getDocuments();

        // Save documents to index
        iwriter.addDocuments(documents);

        // Commit changes and close
        iwriter.close();
        directory.close();

    }

    public static ArrayList<Document> getDocuments() {

        // Path for documents
        String cranPath = "cran/cran.all.1400";

        // Create array list for parsed documents to be stored to
        ArrayList<Document> docs = new ArrayList<>();


        // Return list of documents
        return docs;
    }

    public static Document createDocument() {

        // Create new Lucene document with passed in parameters
        Document document = new Document();

        // Return Lucene document
        return document;
    }
}
