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


public class FRegisterParser {

    public static void indexDocuments(IndexWriter iwriter) throws IOException {

        String fileName = "corpus/fr94";

        File[] directories = new File(fileName).listFiles(File::isDirectory);
        String id,content,headline;
        assert directories != null;
        for (File directory : directories) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
                Elements documents = d.select("DOC");
                for (Element doc : documents) {
                    headline = doc.select("PARENT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());

                    id = doc.select("DOCNO").text();
                    content = doc.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
                    if(content.contains("\n"))
                        content = content.replaceAll("\n","").trim();

                    Document document = new Document();
                    document.add(new TextField("DocNo", id, Field.Store.YES));
                    document.add(new TextField("Content", content, Field.Store.YES));
                    document.add(new TextField("Title", headline, Field.Store.YES));
                    iwriter.addDocument(document);
                }
            }
        }
    }
}
