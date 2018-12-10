/**
* RequestTable.java
* Creates a model table to be used by the RequestsController
* @Director Chaye Novak (902037)
* Copyright: No copyright
* @version 1.0
*/

public class RequestTable {
    String title;
    String year;
    String available;

    /**
     * Constructs a new RequestTable object.
     * @param title The title of the requested item.
     * @param year The year of the requested item.
     * @param available The availability of the requested item.
     */
    public RequestTable(String title, String year, String available) {
        this.title = title;
        this.available = available;
        this.year = year;
    }

    /**
     * Returns the year of the requested item.
     * @return year of the requested item.
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the requested item.
     * @param year new year of requested item.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns the title of the requested item.
     * @return title of the requested item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the requested item.
     * @param title new title of requested item.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the availability of the requested item.
     * @return availability of the requested item.
     */
    public String getAvailable() {
        return available;
    }
    
    /**
     * Sets the availability of the requested item.
     * @param available new availability of requested item.
     */
    public void setAvailable(String available) {
        this.available = available;
    }
}
