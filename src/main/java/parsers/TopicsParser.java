package parsers;

import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class TopicsParser {

    public static ArrayList<String> getDocuments() throws IOException, ParseException {
        ArrayList<String> topicsList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String fileName = "corpus/topics";
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            System.out.println("The path to the Query File  maybe wrong, please check again.");
            System.exit(0);
        }
        int j = 1;

        String line = bufferedReader.readLine();

        while (line != null) {
            if (line.startsWith("<top>")) {
                line = bufferedReader.readLine();
                String content = "";

                while (line != null && !line.startsWith("</top>")) {
                    if (line.startsWith("<num>")) {
                    } else if (line.startsWith("<title>")) {
                        content += " " + line.substring(8);
                    }
                    else if(line.startsWith("<narr>") || line.startsWith("<desc>")){

                    }

                        else {
                        content += " " + line;
                    }
                    line = bufferedReader.readLine();
                }
                content = content.trim();
                content = content.replace("\n", "  ");
                content = content.replace("<desc>", " ");
                content = content.replace("<narr>", " ");
                topicsList.add(content);

            }
            line = bufferedReader.readLine();
        }

        return topicsList;

    }

}



//    Query query = parser.parse(content);
//    // The new query object created is then used to searched the index and a List of ScoreDoc objects are created
//    TopDocs results = is.search(query, mr);
//    ScoreDoc[] hits = results.scoreDocs;
//
//// Then a for loop goes over the hits array to get the score of the query related to each document in the index along with the document id
//                for (int i = 0; i < hits.length; i++) {
//        org.apache.lucene.document.Document hitDoc = is.doc(hits[i].doc);
//        bw.write(j + " Q0 " + hitDoc.get("docno") + " " + (i + 1) + " " + hits[i].score + " Standard" + "\n");
//        }
//        j++;