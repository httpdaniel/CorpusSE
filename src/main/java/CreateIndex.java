import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.ConcurrentMergeScheduler;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import parsers.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CreateIndex {

    // Directory where the search index will be saved
    private static final String INDEX_DIRECTORY = "index";

    public static void main(String[] args) throws IOException, InterruptedException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

        // Set of stop words for engine to ignore
        CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);

        // Create custom analyzer
        Analyzer analyzer = new CustomAnalyzer(stopwords);

        // Set up IndexWriter config
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        ConcurrentMergeScheduler cms = new ConcurrentMergeScheduler();
        cms.setMaxMergesAndThreads(4, 4);
        config.setMergeScheduler(cms);
        config.setUseCompoundFile(false);

        // Create iwriter
        IndexWriter iwriter = new IndexWriter(directory, config);

        ExecutorService exe = Executors.newFixedThreadPool(4);
        int tasks = 4;
        CountDownLatch latch = new CountDownLatch(tasks);

        String[] parsers = {"Fbis", "FRegisterParser", "FTparser", "LATimesParser"};

        System.out.print("Indexing documents...");
        for (int i = 0; i < tasks; i++) {
            exe.submit(new IndexTask(parsers[i], latch, iwriter));
        }

        latch.await();
        exe.shutdown();

        iwriter.close();

        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        String[] content = new String[]{ "Content","Title", "DocNo"};
        QueryParser parser = new MultiFieldQueryParser(content, analyzer);

        parser.setAllowLeadingWildcard(true);


        ArrayList<String> topics = new ArrayList<String>();

        System.out.println("Topics are being extracted...");
        topics = TopicsParser.getDocuments();

        System.out.println("Index search is starting now");
        CorpusSearch.search(topics, parser, isearcher);


        ireader.close();

        directory.close();

    }

    static class IndexTask implements Runnable {

        private CountDownLatch latch;
        private String docLoaderClassName;
        private IndexWriter iwriter;

        public IndexTask(String loader, CountDownLatch latch, IndexWriter indexWriter) {
            this.docLoaderClassName = loader;
            this.latch = latch;
            this.iwriter = indexWriter;
        }

        public void run(){

            try {
                if (docLoaderClassName.equals("LATimesParser")) {
                    LATimesParser.indexDocuments(this.iwriter);
                    System.out.println("LA");
                }
                if (docLoaderClassName.equals("Fbis")) {
                    Fbis.indexDocuments(this.iwriter);
                    System.out.println("Fbis");
                }
                if (docLoaderClassName.equals("FTparser")) {
                    FTparser.indexDocuments(this.iwriter);
                    System.out.println("FT");
                }
                if (docLoaderClassName.equals("FRegisterParser")) {
                    FRegisterParser.indexDocuments(this.iwriter);
                    System.out.println("FR");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("----");
                latch.countDown();
            }
        }

    }
}
