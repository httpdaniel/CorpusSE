import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import parsers.FRegisterParser;
import parsers.Fbis;
import parsers.LATimesParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import parsers.TopicsParser;


public class CreateIndex {

    // Directory where the search index will be saved
    private static final String INDEX_DIRECTORY = "index";

    public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

        // Analyzer that is used to process TextField
        Analyzer analyzer = new StandardAnalyzer();

        // Store index on disk
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // Set open mode to create new index
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter iwriter = new IndexWriter(directory, config);

         //ArrayList to store documents after parsing
        ArrayList<Document> documents = FRegisterParser.getDocuments();
        documents.addAll(Fbis.getDocuments());
        documents.addAll(LATimesParser.getDocuments());
        documents.addAll(Fbis.getDocuments());
         //Save documents to index
        iwriter.addDocuments(documents);
        iwriter.close();


        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        String[] content = new String[]{ "TEXT","HEADLINE", "DOCNO"};
       QueryParser parser = new MultiFieldQueryParser(content, analyzer);
        // Commit changes and close

        parser.setAllowLeadingWildcard(true);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Corpus/ParsedTopics"));
        BufferedWriter br = new BufferedWriter(new FileWriter("Corpus/Results"));

        ArrayList<String> topics = new ArrayList<String>();
        int j = 1;
        topics = TopicsParser.getDocuments();

        for(String topic: topics){
            bufferedWriter.write("New Parsed Topic ----------------------------- *");
            bufferedWriter.newLine();
            bufferedWriter.write(topic);
            bufferedWriter.newLine();
            bufferedWriter.write("End of Topic ============================ *");
            bufferedWriter.newLine();

            System.out.print(j);
            Query query = parser.parse(QueryParserBase.escape(topic));
            TopDocs results = isearcher.search(query, 30);
            ScoreDoc[] hits = results.scoreDocs;

                for (int i = 0; i < hits.length; i++) {
                        org.apache.lucene.document.Document hitDoc = isearcher.doc(hits[i].doc);
                        System.out.print(j + " Q0 " + hitDoc.get("DOCNO") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
                        br.write(j + " Q0 " + hitDoc.get("DOCNO") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
                }
            j++;
        }

        bufferedWriter.flush();
        br.flush();
        br.close();
        bufferedWriter.close();
        ireader.close();
        directory.close();

    }
}
