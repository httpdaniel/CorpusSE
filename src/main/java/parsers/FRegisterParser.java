package parsers;

import org.apache.lucene.document.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


public class FRegisterParser {

    public static ArrayList<Document> getDocuments() throws IOException {
        String fileName = "corpus/fr94";
        ArrayList<Document> parsedDocs = new ArrayList<>();


        File file = new File(fileName);
        File[] folders = file.listFiles(file12 -> file12.isDirectory());
        for (File folder : folders) {
            File[] currentFiles = folder.listFiles(file1 -> file1.isFile());
            for (File currentFile : currentFiles) {
                org.jsoup.nodes.Document document = Jsoup.parse(currentFile, null);
                Elements docs = document.select("DOC");

                for (Element currentDoc : docs) {
                    String text = Jsoup.clean(currentDoc.select("TEXT").text(), Whitelist.none());
                   // System.out.print(text + "\n\n\n\n\n\n\n\n");
                    parsedDocs.add(createDocument(currentDoc.select("DOCNO").text(), text, currentDoc.select("DDCTITLE").text()));
                }
            }

        }
        return parsedDocs;
    }
    public static Document createDocument(String docNo, String headline, String text) {

        // Create new Lucene document with passed in parameters
        Document document = new Document();
        document.add(new TextField("DocNo", docNo, Field.Store.YES));
        document.add(new TextField("Title", headline, Field.Store.YES));
        document.add(new TextField("Content", text, Field.Store.YES));

        // Return Lucene document
        return document;
    }
}
