package beans;

import java.util.Date;

public class Photo {
    private int id_image;
    private String title;
    private Date upload_date;
    private String descritpion;
    private String path;
    private String id_user;

    public Photo(int id_image, String title, Date upload_date, String descritpion, String path, String id_user) {
        this.id_image = id_image;
        this.title = title;
        this.upload_date = upload_date;
        this.descritpion = descritpion;
        this.path = path;
        this.id_user = id_user;
    }

    public Photo() {
    }

    public Photo(int id, String path) {
        this.id_image = id;
        this.path = path;
    }


    public Photo(int id, String path, String title, Date uploadDate) {
        this.id_image = id;
        this.path = path;
        this.title = title;
        this.upload_date = uploadDate;
    }

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