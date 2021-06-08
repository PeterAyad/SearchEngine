package SearchEngine;

public class Records {
	private String title,url,paragraphs;
    public Records()
    {
    	title="Empty";
    	url="#";
    	paragraphs="......";
    }
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(String paragraphs) {
		this.paragraphs = paragraphs;
	}

	
}
