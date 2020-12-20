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
		System.out.println("Started FBIS Index");
		// Path for FBIS documents
		String path = "corpus/fbis";

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

		// For every file in the FBIS folder
		File folder = new File(path);
		File[] fileList = folder.listFiles();

		int i = 0;
		try {
			assert fileList != null;
			for (File files : fileList) {
				org.jsoup.nodes.Document d = Jsoup.parse(files, null, "");
	            Elements documents = d.select("DOC");

	            for (Element doc : documents) {
					id = doc.select("DOCNO").text();
					headline = doc.select("TI").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
					content = doc.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());

					// Create a Lucene document
					idField.setStringValue(id);
					titleField.setStringValue(headline);
					contentField.setStringValue(content);
					iwriter.addDocument(document);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Documents parsed: " + i);
		}
	}

}
