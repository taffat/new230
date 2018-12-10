/**
 * BookTable.java
 * Creates a model table to be used by the bookController
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class BookTable {

	int resourceId;
	int year;
	int copies;
	String author;
	String genre;
	String isbn;
	String language;
	String Title;
	String Publisher;
	
	
	
	
	public BookTable (int resourceId, String Author, String genre, String isbn, String language, String Title, String Publisher, int year, int copies){
		this.resourceId = resourceId;
		this.author = Author;
		this.genre = genre;
		this.isbn = isbn;
		this.language = language;
		this.Title = Title;
		this.Publisher = Publisher;
		this.year = year;
		this.copies = copies;
	}
	
	
	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String Title) {
		this.Title = Title;
	}
	
	public String getPublisher() {
		return Publisher;
	}
	
	public void setPublisher(String Publisher) {
		this.Publisher = Publisher;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}
}
