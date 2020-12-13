package analyzers;
import analyzers.CustomAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.AxiomaticF2LOG;
import org.apache.lucene.search.similarities.AxiomaticF1EXP;
import org.apache.lucene.search.similarities.AxiomaticF1LOG;
import org.apache.lucene.search.similarities.AxiomaticF2EXP;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;

public class SelectAnalyzerSimilarity {
	
	public static Analyzer getAnalyzer(int analyzerNumber) {
		Analyzer analyzer = null;
		CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
		
		if (analyzerNumber == 1) {
			analyzer = new CustomAnalyzer(stopwords, 4);
		} else if(analyzerNumber == 2) {
	    	 analyzer = new EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
		} else if(analyzerNumber == 3) {
	         analyzer = new StandardAnalyzer();
		} else if(analyzerNumber == 4) {
	         analyzer = new KeywordAnalyzer();
		} else if(analyzerNumber == 5) {
	         analyzer = new SimpleAnalyzer();
		}  else if(analyzerNumber == 6) {
	         analyzer = new StopAnalyzer(stopwords);
		} else if(analyzerNumber == 7) {
	         analyzer = new WhitespaceAnalyzer();
		}
		return analyzer;
	}
	
	public static Similarity getSimilarity(int similarityNumber) {
		Similarity sim = null;
		
		if (similarityNumber == 1) {
			sim = new BM25Similarity();
		} 
		else if(similarityNumber == 2) {
	    	 sim = new ClassicSimilarity();
		} 
		else if(similarityNumber == 3) {
	         sim = new LMDirichletSimilarity();
		} 
	 	else if(similarityNumber == 4) {
             sim = new AxiomaticF2LOG();
		} 
	 	else if(similarityNumber == 5) {
	        sim = new AxiomaticF2EXP();
		} 
	 	else if(similarityNumber == 6) {
	        sim = new AxiomaticF1LOG();
		} 
	 	else if(similarityNumber == 7) {
	        sim = new AxiomaticF1EXP();
		} 
	 	else if(similarityNumber == 8) {
	        sim = new BooleanSimilarity();
		} 
	 	else if(similarityNumber == 9) {
	        sim = new LMJelinekMercerSimilarity(0.1f);
		} 
		return sim;
	}

	
}
