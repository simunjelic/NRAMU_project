package ba.sum.fsre.nramu_project.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post {

    public String author;
    public String breed;
    public String description;
    public String phone;
    public String picture;
    public String title;

    public Post() {
    }

    public Post(String author, String breed, String description, String phone, String picture, String title) {
        this.author = author;
        this.breed = breed;
        this.description = description;
        this.phone = phone;
        this.picture = picture;
        this.title = title;
    }
}
