package parsers;


import org.jsoup.select.Elements;

public class DocToIndex {
	
	Elements textFields;
	Elements titleFields;
	Elements noFields;
	
	public DocToIndex(Elements _textFields, Elements _titleFields, Elements _noFields) {
		
		textFields = _textFields;
		titleFields = _titleFields;
		noFields = _noFields;
	}

}
