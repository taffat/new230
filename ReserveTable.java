/**
* ReserveTable.java
* Creates a model table to be used by the ReservedController
* @Director Chaye Novak (902037)
* Copyright: No copyright
* @version 1.0
*/

public class ReserveTable {
    String title;
    String year;
    String available;

    /**
     * Constructs a new ReservedTable object.
     * @param title The title of the reserved item.
     * @param year The year of the reserved item.
     * @param available The availability of the reserved item.
     */
    public ReserveTable(String title, String year, String available) {
        this.title = title;
        this.available = available;
        this.year = year;
    }

    /**
     * Returns the year of the reserved item.
     * @return year of the reserved item.
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the reserved item.
     * @param year new year of reserved item.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns the title of the reserved item.
     * @return title of the reserved item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the reserved item.
     * @param title new title of reserved item.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the availability of the reserved item.
     * @return availability of the reserved item.
     */
    public String getAvailable() {
        return available;
    }

    /**
     * Sets the availability of the reserved item.
     * @param available new availability of reserved item.
     */
    public void setAvailable(String available) {
        this.available = available;
    }
}
