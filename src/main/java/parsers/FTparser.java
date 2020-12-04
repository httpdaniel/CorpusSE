package parsers;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class FTparser {
	
	
	public static ArrayList<org.apache.lucene.document.Document> ParseFT() throws Exception
	{
		
		ArrayList<org.apache.lucene.document.Document> luceneDocs = new ArrayList<org.apache.lucene.document.Document>();
		
		//Parse in the content, docno and title elements to the DocToIndex object
	    for (DocToIndex doc: getElements("corpus/ft/")) {
	    	
	    	luceneDocs.add(createLuceneDoc(doc));

	    }
	   
	    
	    
	    //For testing
	    /*
	    for (org.apache.lucene.document.Document docs: luceneDocs) {
		    for (String content:docs.getValues("Content")) {
		    	System.out.println(content);
		    }
	    }
	    */
	    
	    
	    return luceneDocs;
	}
	
	
	public static ArrayList<DocToIndex> getElements(String path)  throws Exception{
	
		
	ArrayList<DocToIndex> output = new ArrayList<DocToIndex>();
	

	
	File file = new File(path);
	String[] directories = file.list(new FilenameFilter() {
	  @Override
	  public boolean accept(File current, String name) {
	    return new File(current, name).isDirectory();
	  }
	});
	
	
	
	for (String fileDir: directories) {
	
		 File folder = new File(path + fileDir);
		 File[] fileList = folder.listFiles();
	  
	    
	    assert fileList != null;
	    for (File inputFile: fileList) {
	    	Document doc = Jsoup.parse(inputFile, "UTF-8");	
	        Elements textFields = doc.getElementsByTag("Text");
	        Elements titleFields = doc.getElementsByTag("Headline");
	        Elements noFields = doc.getElementsByTag("DOCNO");
	        
	        DocToIndex docOut = new DocToIndex(textFields, titleFields, noFields);
	        output.add(docOut);
	    }
	}
    
    
	
    return output;
    
	}
	
	
	public static org.apache.lucene.document.Document createLuceneDoc(DocToIndex elements) throws Exception{
	
		
		org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
		
	    for (Element textField : elements.textFields) {
	        doc.add(new TextField("Content",textField.text(), Field.Store.YES));
	     }
		
	    for (Element titleField : elements.titleFields) {
	        doc.add(new TextField("Title",titleField.text(), Field.Store.YES));
	     }
	    
	    for (Element noField : elements.noFields) {
	        doc.add(new TextField("DocNo",noField.text(), Field.Store.YES));
	     }
	    
	    


	    
	    return doc;
	}
	

	

}
