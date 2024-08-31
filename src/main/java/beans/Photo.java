package beans;

import java.util.Date;

public class Photo {
    private int id_image;
    private String title;
    private Date upload_date;
    private String descritpion;
    private String path;
    private String id_user;

    public int getId_Image() { return id_image; }
    public void setId_Image(int id) { this.id_image = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getUploadDate() { return upload_date; }
    public void setUploadDate(Date upload_date) { this.upload_date = upload_date; }

    public String getDescription() { return descritpion; }
    public void setDescription(String descritpion) { this.descritpion = descritpion; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getId_User() { return id_user; }
    public void setId_User(String idUser) { this.id_user = idUser; }
}