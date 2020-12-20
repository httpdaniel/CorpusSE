package parsers;

import org.apache.lucene.document.Document;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * This Class Takes care of the parsing of the Federal Register Dataset.
 * It makes use of Jsop Primarily to do the parsing. It also parses only the Doc and Text and removes all the
 * tags using the Jsoup.clean function. It finally sends a list of docs back to the CreateIndex class.
 */
public class FRegisterParser {

    public static void indexDocuments(IndexWriter iwriter) throws IOException {
        System.out.println("Started Federal Register  Parser ...");

        String fileName = "corpus/fr94";

        // Create skeleton for documents
        String id, content;
        id  = content = "";
        Document document = new Document();
        Field idField = new TextField("DocNo", id, Field.Store.YES);
        Field contentField = new TextField("Content", content, Field.Store.YES);
        document.add(idField);
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

                    id = doc.select("DOCNO").text();
                    content = doc.select("TEXT").text();
                    content =Jsoup.clean(content, Whitelist.none());
                     content = content.replaceAll("[^a-zA-Z0-9]+"," ");

                    if(content.contains("\n")) {
                        content = content.replaceAll("\n", "").trim();
                    }

                    // Create a Lucene document
                    idField.setStringValue(id);
                    contentField.setStringValue(content);
                    iwriter.addDocument(document);
                }
            }
        }
    }
}
