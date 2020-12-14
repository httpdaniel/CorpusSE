package analyzers;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;

// https://www.baeldung.com/lucene-analyzers

public class CustomAnalyzer extends StopwordAnalyzerBase {

	private int ngrams;
	
	private final CharArraySet stemExclusionSet;
	
	  public CustomAnalyzer(CharArraySet stopwords, int _ngrams) {
		    this(stopwords, _ngrams, CharArraySet.EMPTY_SET);
		  }
	  
	public CustomAnalyzer(CharArraySet stopwords, int _ngrams,CharArraySet stemExclusionSet) {
        super(stopwords);
	    this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(stemExclusionSet));
        ngrams = _ngrams;
    }
	


    public CharArraySet getStopwords() {
        return stopwords;
    }

    public int getNgrams() {
        return ngrams;
    }

    @SuppressWarnings("resource")
	@Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();


       // filter = new PorterStemFilter(filter);
       // filter = new StopFilter(filter, stopwords);
        //filter = new EnglishMinimalStemFilter(filter);
 	    //filter = new NGramTokenFilter(filter, ngrams);
 	    
 	    final Tokenizer source = new StandardTokenizer();
 	    TokenStream filter = new EnglishPossessiveFilter(source);
 	   filter = new LowerCaseFilter(filter);
 	   filter = new StopFilter(filter, stopwords);
 	    if(!stemExclusionSet.isEmpty())
 	    	filter = new SetKeywordMarkerFilter(filter, stemExclusionSet);
 	   filter = new PorterStemFilter(filter);
 	    
 	  
 	   
        return new TokenStreamComponents(source, filter);
    }
}