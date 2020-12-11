import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import analyzers.SelectAnalyzerSimilarity;
import parsers.TopicsParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CorpusSearch {

    // Directory where the search index is saved
    private static final String INDEX_DIRECTORY = "index";

    // Maximum number of returned results
    private static final int MAX_RESULTS = 1000;

    public static void main(String[] args) throws IOException, java.text.ParseException, ParseException {

        // Open folder that contains search index
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        
      //Select Similarity
    	/** 1: BM25Similarity
    	 *  2: ClassicSimilarity
    	 *  3: LMDirichletSimilarity
    	 */
        Similarity sim = SelectAnalyzerSimilarity.getSimilarity(1);

        // Create objects to read and search across index
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        isearcher.setSimilarity(SelectAnalyzerSimilarity.getSimilarity(1));

        // Set of stop words for engine to ignore
        //CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);

        // Create custom analyzer
        //Analyzer analyzer = new CustomAnalyzer(stopwords);

      //Select Analyzer
    	/** 1: CustomAnalyzer
    	 *  2: EnglishAnalyzer
    	 *  3: StandarAnalyzer
    	 *  4: KeywordAnalyzer
    	 *  5: SimpleAnalyzer
    	 *  6: StopAnalyzer
    	 *  7: WhitespaceAnalyzer
    	 */
    	Analyzer analyzer = SelectAnalyzerSimilarity.getAnalyzer(2);
        
        // Booster to add weight to more important fields
//        HashMap<String, Float> boost = new HashMap<>();
//        boost.put("DocNo", 0.1f);
//        boost.put("Title", 0.65f);
//        boost.put("Content", 0.35f);

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"DocNo", "Title", "Content"}, analyzer);
        queryParser.setAllowLeadingWildcard(true);

        // Get queries
        ArrayList<String> queries;
        queries = TopicsParser.getDocuments();

        // Start searching
        System.out.println("Index search is starting now");
        search(queries, queryParser, isearcher);

        ireader.close();
        directory.close();

    }
    public static void search(ArrayList<String> topics, QueryParser parser, IndexSearcher isearcher) throws IOException, ParseException {

        BufferedWriter topicWriter = new BufferedWriter(new FileWriter("corpus/ParsedTopics.txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter("corpus/Results.txt"));

        int j = 401;
        for(String topic: topics){
            topicWriter.write("New Parsed Topic ----------------------------- *");
            topicWriter.newLine();
            topicWriter.write(topic);
            topicWriter.newLine();
            topicWriter.write("End of Topic ============================ *");
            topicWriter.newLine();

            Query query = parser.parse(QueryParserBase.escape(topic));

            // Score documents
            ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;

            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.print(j + " Q0 " + hitDoc.get("DocNo") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
                resultWriter.write(j + " Q0 " + hitDoc.get("DocNo") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
            }
            j++;
        }


        topicWriter.flush();
        topicWriter.close();
        resultWriter.flush();
        resultWriter.close();
    }
}
