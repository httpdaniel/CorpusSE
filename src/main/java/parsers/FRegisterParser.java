package parsers;

import org.apache.lucene.document.Document;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FRegisterParser {

    public static void indexDocuments(IndexWriter iwriter) throws IOException {

        String fileName = "corpus/fr94";

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

        File[] directories = new File(fileName).listFiles(File::isDirectory);
        assert directories != null;
        for (File directory : directories) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
                Elements documents = d.select("DOC");
                for (Element doc : documents) {
                    headline = doc.select("PARENT").text();

                    id = doc.select("DOCNO").text();

                    content = doc.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
                    if(content.contains("\n")) {
                        content = content.replaceAll("\n", "").trim();
                    }

                    // Create a Lucene document
                    idField.setStringValue(id);
                    titleField.setStringValue(headline);
                    contentField.setStringValue(content);
                    System.out.println(idField + " " + titleField);
                    iwriter.addDocument(document);
                }
            }
        }
    }
}
