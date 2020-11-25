package Lucene;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.awt.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import javax.swing.*;


public class Extraction {
    public static void indexDataset(String fileName, IndexWriter iw) throws IOException {

        File file = new File(fileName);
        File[] folders = file.listFiles(file12 -> file12.isDirectory());
        for (File folder : folders){
            File[] currentFiles = folder.listFiles(file1 -> file1.isFile());
            for(File currentFile : currentFiles){
                Document document = Jsoup.parse(currentFile, null );
                Elements docs =  document.select("DOC");
                for(Element currentDoc: docs) {
//                    System.out.print("This is the document number ========> " + currentDoc.select("DOCNO").text() + "\n");

                    //System.out.print("This is the document number ========> " + doc.select("TEXT").text() + "\n");\Document doc = new Document();
                    org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

                    doc.add(new StringField("docno", currentDoc.select("DOCNO").text(), Field.Store.YES));
                    doc.add(new TextField("text", currentDoc.select("TEXT").text(), Field.Store.YES));
                    doc.add(new TextField("title", currentDoc.select("DDCTITLE").text(), Field.Store.YES));


                    //we are indexing the document immediately as it gives a performance improvement.
                     iw.addDocument(doc);

                }
            }

        }
    }
    public static void indexDatasetTest(String fileName) throws IOException {
        File file = new File(fileName);
        File[] folders = file.listFiles(file12 -> file12.isDirectory());
        for (File folder : folders){
            File[] currentFiles = folder.listFiles(file1 -> file1.isFile());
            for(File currentFile : currentFiles){
                Document document = Jsoup.parse(currentFile, null );
                Elements docs =  document.select("DOC");
                for(Element currentDoc: docs) {

                    //System.out.print("This is the document number ========> " + doc.select("TEXT").text() + "\n");\Document doc = new Document();
                    org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

                    doc.add(new StringField("docno", currentDoc.select("DOCNO").text(), Field.Store.YES));
                    doc.add(new TextField("text", currentDoc.select("TEXT").text(), Field.Store.YES));
                    doc.add(new TextField("title", currentDoc.select("DDCTITLE").text(), Field.Store.YES));


                    //we are indexing the document immediately as it gives a performance improvement.
                   // iw.addDocument(doc);

                }
            }

        }

        //System.out.println("hi");
    }

    public static void scoreQuery(String fileName, QueryParser parser, IndexSearcher is, BufferedWriter bw, int mr) throws IOException, ParseException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));}
        catch (Exception e){
            System.out.println("The path to the Query File  maybe wrong, please check again.");
            System.exit(0);
        }
        int j =1;

        String line = bufferedReader.readLine();

        while (line!= null ){
            if(line.startsWith("<top>")){
                line = bufferedReader.readLine();
                String content = "";

                while(line!= null && !line.startsWith("</top>")){
                    if(line.startsWith("<num>")){}
                    else if(line.startsWith("<title>")){
                        content +=  line.substring(8);
                    }
                    else {
                        content+=line;
                    }
                    line = bufferedReader.readLine();
                }
                content = content.trim();
                content = content.replace("\n" , " ");
                Query query = parser.parse(content);
                // The new query object created is then used to searched the index and a List of ScoreDoc objects are created
                TopDocs results = is.search(query, mr);
                ScoreDoc[] hits = results.scoreDocs;

                // Then a for loop goes over the hits array to get the score of the query related to each document in the index along with the document id
                for(int i=0;i<hits.length;i++){
                    org.apache.lucene.document.Document hitDoc = is.doc(hits[i].doc);
                    bw.write(j + " Q0 " + hitDoc.get("docno") + " " + (i+1) + " " + hits[i].score + " Standard" + "\n" );
                }
                j++;
            }
            line = bufferedReader.readLine();
        }
    }

    public static void scoreQueryTest(String fileName) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));}
        catch (Exception e){
            System.out.println("The path to the Query File  maybe wrong, please check again.");
            System.exit(0);
        }

        String line = bufferedReader.readLine();

        while (line!= null ){
            if(line.startsWith("<top>")){
                line = bufferedReader.readLine();
                String query = "";

                while(line!= null && !line.startsWith("</top>")){
                        if(line.startsWith("<num>")){}
                        else if(line.startsWith("<title>")){
                            query +=  line.substring(8);
                        }
                        else {
                            query+=line;
                        }
                        line = bufferedReader.readLine();
                }
                System.out.print("\n" + query.trim());
            }
            line = bufferedReader.readLine();
        }

    }
}