package com.evanhayes.evanhayes.models.Images;

import com.evanhayes.evanhayes.models.Category.Category;
import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public Images() {}

    public Images(String aPath) {
        this.path = aPath;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
