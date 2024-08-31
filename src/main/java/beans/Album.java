package beans;
import java.util.Date;

public class Album {
    private int id_album;
    private String title;
    private Date creation_date;

    private String username;

    public Album(int id_album, String title, Date creation_date, String username) {
        this.id_album = id_album;
        this.title = title;
        this.creation_date = creation_date;
        this.username = username;
    }

    public Album() {

    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getCreationDate() { return creation_date; }
    public void setCreationDate(Date creation_date) { this.creation_date = creation_date; }

    public void setId_User(String username) { this.username = username; }

}