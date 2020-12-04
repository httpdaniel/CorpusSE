import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CorpusSearch {
    public static void search(ArrayList<String> topics, QueryParser parser, IndexSearcher isearcher) throws IOException, ParseException {
        int j = 1;

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Corpus/ParsedTopics"));
        BufferedWriter br = new BufferedWriter(new FileWriter("Corpus/Results"));
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
                System.out.print(j + " Q0 " + hitDoc.get("DocNo") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
                br.write(j + " Q0 " + hitDoc.get("DocNo") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
            }
            j++;
        }


        bufferedWriter.flush();
        br.flush();
        br.close();
        bufferedWriter.close();
    }
}
