/**
 * ResourceTable.java
 * Creates a model table to be used by all resources
 * @author Chaye Novak (902037)
 * Copyright: No copyright
 * @version 1.0
*/
public class ResourceTable {

	// Entities for more than one table
	private int resourceId;
	private int year;
	int copies;
	private String resourceType;
	private String language;
	// Entities specifically for BookTable
	private String author;
	private String genre;
	private String isbn;
	private String title;
	private String publisher;	
	// Entities specifically for DvdTable
	private String director;
	private int runtime;
	// Entities specifically for LaptopTable
	private String manufacturer;
	private String model;
	private String OS;
	
	public ResourceTable(int resourceId, int year, String resourceType, String language, String author, String isbn,
			String title, String publisher, String genre, String director, int runtime, String manufacturer, String model, String OS, int copies) {
		//super();
		this.resourceId = resourceId;
		this.year = year;
		this.resourceType = resourceType;
		this.language = language;
		this.author = author;
		this.isbn = isbn;
		this.title = title;
		this.publisher = publisher;
		this.genre = genre;
		this.director = director;
		this.runtime = runtime;
		this.manufacturer = manufacturer;
		this.model = model;
		this.OS = OS;
		this.copies = copies;
		
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String OS) {
		this.OS = OS;
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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
