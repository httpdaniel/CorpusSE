package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

    public static ArrayList<Document> getDocuments() throws IOException {

        // Path for LATimes documents
        String path = "corpus/latimes";

        // Create array list for parsed documents to be stored to
        ArrayList<Document> parsedDocs = new ArrayList<>();

        // For every file in the LATimes folder
        File folder = new File(path);
        File[] fileList = folder.listFiles();

        assert fileList != null;
        for (File file : fileList) {
            org.jsoup.nodes.Document content = Jsoup.parse(file,null,"");

            Elements docs = content.select("DOC");

            for(Element doc: docs) {
                String docNo, headline, text;
                docNo = (doc.select("DOCNO").text());
                headline = (doc.select("HEADLINE").select("P").text());
                text = (doc.select("TEXT").select("P").text());
                parsedDocs.add(createDocument(docNo, headline, text));
            }

        }

        return parsedDocs;
    }

    public static Document createDocument(String docNo, String headline, String text) {

        // Create new Lucene document with passed in parameters
        Document document = new Document();
        document.add(new TextField("DocNum", docNo, Field.Store.YES));
        document.add(new TextField("Headline", headline, Field.Store.YES));
        document.add(new TextField("Content", text, Field.Store.YES));

        // Return Lucene document
        return document;
    }

}
