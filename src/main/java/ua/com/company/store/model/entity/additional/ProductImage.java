package ua.com.company.store.model.entity.additional;

import java.util.Comparator;

/**
 * Created by Владислав on 13.12.2017.
 */
public class ProductImage{
    private int id;
    private String title;
    private String description;
    private int  price;
    private String pathImage;

    public ProductImage() {
    }

    public ProductImage(int id, String title, String description, int price, String pathImage) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.pathImage = pathImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "price=" + price +
                '}';
    }
}
