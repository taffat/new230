/**
* LaptopTable.java
* Creates a model table to be used by the LapController
* @manufacturer Chaye Novak (902037)
* Copyright: No copyright
* @version 1.0
*/

public class LaptopTable {

		int resourceId;
		int year;
		int copies;
		String manufacturer;
		String model;
		String OS;
		String Title;
		
		/**
		 * Constructs a new LaptopTable object.
		 * @param resourceId ID of the laptop.
		 * @param manufacturer Manufacturer of laptop.
		 * @param model the laptop's model.
		 * @param OS the operating system of the laptop
		 */
		public LaptopTable(int resourceId, String manufacturer, String model, String OS, String Title, int year, int copies) {
			this.resourceId = resourceId;
			this.model = model;
			this.OS = OS;
			this.manufacturer = manufacturer;
			this.Title = Title;
			this.year = year;
			this.copies = copies;
		}
		
		/**
		 * Retrieves the laptop's ID.
		 * @return Laptop ID.
		 */
		public int getResourceId() {
			return resourceId;
		}
 
		/**
		 * Sets the new resource ID. 
		 * @param resourceId New ID to be set.
		 */
		public void setResourceId(int resourceId) {
			this.resourceId = resourceId;
		}

		/**
		 * Retrieves the laptop's manufacturer.
		 * @return Laptop manufacturer.
		 */
		public String getManufacturer() {
			return manufacturer;
		}

		/**
		 * Sets the new resource manufacturer.
		 * @param resourceId New manufacturer to be set.
		 */
		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		/**
		 * Retrieves the laptop's model.
		 * @return Laptop model.
		 */
		public String getModel() {
			return model;
		}

		/**
		 * Sets the new resource model.
		 * @param resourceId New model to be set.
		 */
		public void setModel(String model) {
			this.model = model;
		}
		
		/**
		 * Retrieves the laptop's Operating system.
		 * @return Laptop OS.
		 */
		public String getOS() {
			return OS;
		}

		/**
		 * Sets the new resource operating system.
		 * @param resourceId New operating system to be set.
		 */
		public void setOS(String OS) {
			this.OS = OS;
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
