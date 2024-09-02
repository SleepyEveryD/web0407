package beans;

import java.util.Date;

public class Photo {
    private int id_image;
    private String title;
    private Date uploadDate;
    private String descritpion;
    private String path;
    private String id_user;

    public Photo(int id_image, String title, Date upload_date, String descritpion, String path, String id_user) {
        this.id_image = id_image;
        this.title = title;
        this.uploadDate = upload_date;
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
        this.uploadDate = uploadDate;
    }

    public int getId_image() {
        return id_image;
    }

    public void setId_image(int id_image) {
        this.id_image = id_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
