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
        System.out.println("The datasets are being extracted");
        ArrayList<Document> documents = FRegisterParser.getDocuments();
        documents.addAll(Fbis.getDocuments());
        documents.addAll(LATimesParser.getDocuments());
        documents.addAll(Fbis.getDocuments());


         //Save documents to index
        System.out.print("The Datasets are being indexed...");
        iwriter.addDocuments(documents);
        iwriter.close();


        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        String[] content = new String[]{ "Content","Title", "DocNo"};
       QueryParser parser = new MultiFieldQueryParser(content, analyzer);
        // Commit changes and close

        parser.setAllowLeadingWildcard(true);


        ArrayList<String> topics = new ArrayList<String>();

        System.out.println("Topics are being extracted...");
        topics = TopicsParser.getDocuments();

        System.out.println("Index search is starting now");
        CorpusSearch.search(topics, parser, isearcher);


        ireader.close();

        directory.close();

    }
}
