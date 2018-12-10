public class ReqAndResTable {
    String title;
    String year;
    String resourcetype;
    String available;

    public ReqAndResTable(String title, String year, String resourcetype, String available) {
        this.title = title;
        this.available = available;
        this.year = year;
        this.resourcetype = resourcetype;
    }

    public String getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
