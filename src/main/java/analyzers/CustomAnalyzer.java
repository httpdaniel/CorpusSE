package analyzers;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;

// https://www.baeldung.com/lucene-analyzers

public class CustomAnalyzer extends StopwordAnalyzerBase {

	private int ngrams;

	public CustomAnalyzer(CharArraySet stopwords, int _ngrams) {
        super(stopwords);
        ngrams = _ngrams;
    }


    public CharArraySet getStopwords() {
        return stopwords;
    }

    public int getNgrams() {
        return ngrams;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();

        // Create token stream and add filters
        TokenStream filter = new LowerCaseFilter(tokenizer);

        filter = new PorterStemFilter(filter);
        filter = new StopFilter(filter, stopwords);
        filter = new EnglishMinimalStemFilter(filter);
 	    filter = new NGramTokenFilter(filter, 1);

        return new TokenStreamComponents(tokenizer, filter);
    }
}