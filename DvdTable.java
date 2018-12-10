/**
* DvdTable.java
* Creates a model table to be used by the DvdController
* @director Chaye Novak (902037)
* Copyright: No copyright
* @version 1.0
*/

public class DvdTable {

		int resourceId;
		int year;
		int copies;
		String director;
		int runtime;
		String language;
		String Title;
		
		public DvdTable(int resourceId, String director, int runtime, String language, String Title, int year, int copies){
			super();
			this.resourceId = resourceId;
			this.runtime = runtime;
			this.language = language;
			this.director = director;
			this.Title = Title;
			this.year = year;
			this.copies	= copies;
		}
		
		
		public int getResourceId() {
			return resourceId;
		}
 
		public void setResourceId(int resourceId) {
			this.resourceId = resourceId;
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
		
		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}
		
		/**
		 * Gets the Laptop's title.
		 */
		public String getTitle() {
			return Title;
		}
		
		/**
		 * Sets the Laptop's title.
		 * @param Title New Title to be set..
		 */
		public void setTitle(String Title) {
			this.Title = Title;
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