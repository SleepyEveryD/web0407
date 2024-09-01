package beans;
import java.sql.Timestamp;

public class Comment {
    private int id_comment;
    private String username;
    private int id_image;
    private String text;

    private Timestamp timestamp;

    public int getId_Comment() { return id_comment; }
    public void setId_Comment(int id) { this.id_comment = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getId_Image() { return id_image; }
    public void setId_Image(int image) { this.id_image = image; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Timestamp getTimestamp() {return timestamp;}
    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}
}
