package drivers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Item class containing generation properties and methods
 */
public class Item {

    private final StringProperty SKU;
    private final StringProperty item;
    private final StringProperty category;
    private final StringProperty brand;
    private final IntegerProperty itemsize;
    private final StringProperty unit;
    private final StringProperty color;
    private final StringProperty type;
    private final StringProperty description;

    public Item(){
        this(null, null, null, null, 0, null, null, null, null);
    }

    public Item(String SKU, String item, String category, String brand, int itemsize, String unit, String color, String type, String description){
        this.SKU = new SimpleStringProperty(SKU);
        this.item = new SimpleStringProperty(item);
        this.category = new SimpleStringProperty(category);
        this.brand = new SimpleStringProperty(brand);
        this.itemsize = new SimpleIntegerProperty(itemsize);
        this.unit = new SimpleStringProperty(unit);
        this.color = new SimpleStringProperty(color);
        this.type = new SimpleStringProperty(type);
        this.description = new SimpleStringProperty(description);
    }

    public String getSKU() {
        return SKU.get();
    }

    public StringProperty SKUProperty() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU.set(SKU);
    }

    public String getItem() {
        return item.get();
    }

    public StringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public int getItemsize() {
        return itemsize.get();
    }

    public IntegerProperty itemsizeProperty() {
        return itemsize;
    }

    public void setItemsize(int itemsize) {
        this.itemsize.set(itemsize);
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty desciptionProperty(){
        return description;
    }
}
