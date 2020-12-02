package parsers;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.util.ArrayList;

public class Fbis {

	public static ArrayList<Document> getDocuments() {
		ArrayList<Document> parsedDocuments = new ArrayList<Document>();
		String docno,text,title;
		File[] file = new File("corpus/fbis/").listFiles();
		int i = 0;
		try {
			for (File files : file) {
				org.jsoup.nodes.Document d = Jsoup.parse(files, null, "");
	            Elements documents = d.select("doc");

	            for (Element document : documents) {
	                docno = document.select("docno").text();
	                text = document.select("text").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
	                title = document.select("ti").text().replaceAll("[^a-zA-Z ]", "".toLowerCase());
	                
	                Document doc = new Document();
	                doc.add(new TextField("DOCNO", docno, Field.Store.YES));
	                doc.add(new TextField("HEADLINE", title, Field.Store.YES));
	                doc.add(new TextField("TEXT", text, Field.Store.YES));
	                parsedDocuments.add(doc);
			}
		}
	}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Documents parsed: " + i);
		}
		return parsedDocuments;
	}

}
