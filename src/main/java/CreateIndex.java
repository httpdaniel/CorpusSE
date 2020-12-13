import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.ConcurrentMergeScheduler;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import analyzers.SelectAnalyzerSimilarity;
import parsers.*;
import analyzers.CustomAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CreateIndex {

    // Directory where the search index will be saved
    private static final String INDEX_DIRECTORY = "index";

    public static void main(String[] args) throws IOException, InterruptedException {

        // Set of stop words for engine to ignore
        CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);

    	//Select Analyzer
        //Analyzer analyzer = SelectAnalyzerSimilarity.getAnalyzer(2);
    	//Select Similarity
    	/** 1: BM25Similarity
    	 *  2: ClassicSimilarity
    	 *  3: LMDirichletSimilarity
    	 */

        // Create custom analyzer
        Analyzer analyzer = new CustomAnalyzer(stopwords, 3);

        // Set up IndexWriter config
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //Similarity sim = SelectAnalyzerSimilarity.getSimilarity(1);
        config.setSimilarity(SelectAnalyzerSimilarity.getSimilarity(1));
        config.setUseCompoundFile(false);

        // Create threads for indexing
        ConcurrentMergeScheduler cms = new ConcurrentMergeScheduler();
        cms.setMaxMergesAndThreads(4, 4);
        config.setMergeScheduler(cms);

        // Create iwriter
        IndexWriter iwriter = new IndexWriter(directory, config);

        ExecutorService es = Executors.newFixedThreadPool(4);
        int parseTasks = 4;
        CountDownLatch latch = new CountDownLatch(parseTasks);

        // Parser classes
        String[] parsers = {"Fbis", "FRegisterParser", "FTparser", "LATimesParser"};

        System.out.print("Indexing documents...\n");
        for (int i = 0; i < parseTasks; i++) {
            es.submit(new ParseTask(parsers[i], latch, iwriter));
        }

        latch.await();
        es.shutdown();

        iwriter.close();
        directory.close();

    }

    static class ParseTask implements Runnable {

        private CountDownLatch latch;
        private String docLoaderClassName;
        private IndexWriter iwriter;

        public ParseTask(String loader, CountDownLatch latch, IndexWriter indexWriter) {
            this.docLoaderClassName = loader;
            this.latch = latch;
            this.iwriter = indexWriter;
        }

        public void run(){

            try {
                if (docLoaderClassName.equals("LATimesParser")) {
                    LATimesParser.indexDocuments(this.iwriter);
                    System.out.println("LATimes - Complete");
                }
                if (docLoaderClassName.equals("Fbis")) {
                    Fbis.indexDocuments(this.iwriter);
                    System.out.println("FBIS - Complete");
                }
                if (docLoaderClassName.equals("FTparser")) {
                    FTparser.indexDocuments(this.iwriter);
                    System.out.println("FTimes - Complete");
                }
                if (docLoaderClassName.equals("FRegisterParser")) {
                    FRegisterParser.indexDocuments(this.iwriter);
                    System.out.println("FR - Complete");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }

    }
}
