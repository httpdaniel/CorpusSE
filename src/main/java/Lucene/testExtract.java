package Lucene;

import java.io.IOException;
import java.text.ParseException;

public class testExtract {

    public static void  main(String Args[]) throws IOException  {
//        Extraction.indexDatasetTest("Corpus/fr94");

        Extraction.scoreQueryTest("Corpus/topics");
    }

}