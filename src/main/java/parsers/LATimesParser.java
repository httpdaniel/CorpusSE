package parsers;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

    public static void indexDocuments(IndexWriter iwriter) throws IOException {

        // Path for LATimes documents
        String path = "corpus/latimes";

        // Create skeleton for documents
        String id, headline, content;
        id = headline = content = "";
        Document document = new Document();
        Field idField = new TextField("DocNo", id, Field.Store.YES);
        Field titleField = new TextField("Title", headline, Field.Store.YES);
        Field contentField = new TextField("Content", content, Field.Store.YES);
        document.add(idField);
        document.add(titleField);
        document.add(contentField);

        // For every file in the LATimes folder
        File folder = new File(path);
        File[] fileList = folder.listFiles();

        // Iterate through files
        assert fileList != null;
        for (File file : fileList) {
            org.jsoup.nodes.Document docContent = Jsoup.parse(file, null, "");

            // Get all documents in file
            Elements documents = docContent.select("DOC");

            // Find ID, Headline, & Content
            for(Element doc: documents) {
                id = (doc.select("DOCNO").text());
                headline = (doc.select("HEADLINE").select("P").text());
                content = (doc.select("TEXT").select("P").text());
                // Create a Lucene document
                idField.setStringValue(id);
                titleField.setStringValue(headline);
                contentField.setStringValue(content);
                iwriter.addDocument(document);
            }

        }
    }
}
