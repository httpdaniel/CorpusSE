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

		// Path for FinancialTimes documents
		String path = "corpus/ft";

		File[] directories = new File(path).listFiles(File::isDirectory);

		assert directories != null;
		for (File folder : directories) {
			File[] files = folder.listFiles();
			assert files != null;
			for (File file : files) {
				org.jsoup.nodes.Document d = Jsoup.parse(file, null, "");
				Elements documents = d.select("DOC");

				for (Element document : documents) {
					String id, content, headline;

					id = document.select("DOCNO").text();
					headline = document.select("HEADLINE").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
					content = document.select("TEXT").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());

					Document doc = new Document();
					doc.add(new TextField("DocNo", id, Field.Store.YES));
					doc.add(new TextField("Title", headline, Field.Store.YES));
					doc.add(new TextField("Content", content, Field.Store.YES));
					iwriter.addDocument(doc);
				}
			}
		}
	}

}
