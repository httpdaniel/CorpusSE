package analyzers;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;

// https://www.baeldung.com/lucene-analyzers

// Preliminary Custom analyzer without English Possessive Filter

public class CustomAnalyzer extends StopwordAnalyzerBase {

    public CustomAnalyzer(CharArraySet stopwords) {
        super(stopwords);
    }

    public CharArraySet getStopwords() {
        return stopwords;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();

        // Create token stream and add filters
        TokenStream filter = new LowerCaseFilter(tokenizer);

        filter = new PorterStemFilter(filter);
        filter = new StopFilter(filter, stopwords);
        filter = new EnglishMinimalStemFilter(filter);

        return new TokenStreamComponents(tokenizer, filter);
    }
}