package parsers;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class FTparser {
	public static void indexDocuments(IndexWriter iwriter) throws IOException {
		System.out.println("Started Financial Times  Parser ...");

		// Path for FinancialTimes documents
		String path = "corpus/ft";

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

		//Get the directories with the documents
		File[] directories = new File(path).listFiles(File::isDirectory);

		assert directories != null;
		//iterate through each directory
		for (File folder : directories) {
			File[] files = folder.listFiles();
			assert files != null;
			//iterate through each file
			for (File file : files) {
				org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
				Elements documents = d.select("DOC");
				//Get the element from each doc and convert to lucene doc
				for (Element doc : documents) {

					id = doc.select("DOCNO").text();
					headline = doc.select("HEADLINE").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
					content = doc.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
					// Create a Lucene document
					idField.setStringValue(id);
					titleField.setStringValue(headline);
					contentField.setStringValue(content);
					iwriter.addDocument(document);
				}
			}
		}
	}

}
