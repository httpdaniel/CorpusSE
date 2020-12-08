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

public class SelectAnalyzer {
	
	public static Analyzer getAnalyzer(int analyzerNumber) {
		Analyzer analyzer = null;
		CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);

		if (analyzerNumber == 1) {
			analyzer = new CustomAnalyzer(stopwords);
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

}
