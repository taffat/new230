import java.time.LocalDateTime;
import java.util.Date;

public class BorrowTable {
    String title;
    String year;
    String resourcetype;
    String dueDate;

    public BorrowTable(String title,String year,String resourcetype,String dueDate) {
        this.title = title;
        this.dueDate = dueDate;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
