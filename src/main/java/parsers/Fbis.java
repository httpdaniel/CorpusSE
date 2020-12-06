package parsers;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;

public class Fbis {

	public static void indexDocuments(IndexWriter iwriter) {

		// Path for FBIS documents
		String path = "corpus/fbis";

		// For every file in the FBIS folder
		File folder = new File(path);
		File[] fileList = folder.listFiles();

		int i = 0;
		try {
			assert fileList != null;
			for (File files : fileList) {
				org.jsoup.nodes.Document d = Jsoup.parse(files, null, "");
	            Elements documents = d.select("DOC");

	            for (Element document : documents) {
					String id, headline, content;
					id = document.select("DOCNO").text();
					headline = document.select("TI").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
					content = document.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());

					Document doc = new Document();
					doc.add(new TextField("DocNo", id, Field.Store.YES));
					doc.add(new TextField("Title", headline, Field.Store.YES));
					doc.add(new TextField("Content", content, Field.Store.YES));
					iwriter.addDocument(doc);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Documents parsed: " + i);
		}
	}

}
