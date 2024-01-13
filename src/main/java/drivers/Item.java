package drivers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Primary Item class for generating Item objects within the inventory management system.
 *
 * <d>
 *  The {@code Item} class represents an item with various attributes such as SKU, item name, category,
 *  brand, item size, unit, color, type, and description. It provides getters and setters for each attribute
 *  along with JavaFX properties for seamless integration with JavaFX applications.
 * </d>
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 *
 * @version 1.0
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

    /**
     * No-Argument Constructfor for a new {@code Item} object with default values for each attribute.
     */
    public Item(){
        this(null, null, null, null, 0, null, null, null, null);
    }

    /**
     * Constructs a new {@code Item} with the specified values for each attribute.
     *
     * @param SKU         the Stock Keeping Unit (SKU) of the item
     * @param item        the name of the item
     * @param category    the category to which the item belongs
     * @param brand       the brand of the item
     * @param itemsize    the size of the item
     * @param unit        the unit in which the item is measured
     * @param color       the color of the item
     * @param type        the type or classification of the item
     * @param description a description or additional information about the item
     */
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

    /**
     * Getter method for SKU property.
     * @return SKU of specified item.
     */
    public String getSKU() {
        return SKU.get();
    }

    /**
     * Returns StringProperty for the respective SKU of the item.
     * @return SKU of specified item.
     */
    public StringProperty SKUProperty() {
        return SKU;
    }

    /**
     * Setter method for SKU property.
     * @param SKU SKU value.
     */
    public void setSKU(String SKU) {
        this.SKU.set(SKU);
    }

    /**
     * Getter method for the Item's Name
     * @return Item's name
     */
    public String getItem() {
        return item.get();
    }

    /**
     * Returns the StringProperty representing category.
     * @return
     */
    public StringProperty itemProperty() {
        return item;
    }

    /**
     * Setter method for Item's Name
     * @return Item's Name
     */
    public void setItem(String item) {
        this.item.set(item);
    }

    /**
     * Getter Method for the category property of the Item.
     * @return the Item's category
     */
    public String getCategory() {
        return category.get();
    }

    /**
     * Returns the StringProperty representing the Item's category.
     * @return Item's Category.
     */
    public StringProperty categoryProperty() {
        return category;
    }

    /**
     * Setter Method for the Item's category.
     * @param category Item's Category.
     */
    public void setCategory(String category) {
        this.category.set(category);
    }

    /**
     * Getter method for the Item's brand.
     * @return Item's brand
     */
    public String getBrand() {
        return brand.get();
    }

    /**
     * Returns the representing StringPropety of the Item's Brand.
     * @return Item's Brand
     */
    public StringProperty brandProperty() {
        return brand;
    }

    /**
     * Setter method for the Item's Brand
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    /**
     * Gets the size of the item.
     *
     * @return the size of the item
     */
    public int getItemsize() {
        return itemsize.get();
    }

    /**
     * Returns the IntegerProperty representing the size of the item.
     *
     * @return the item size IntegerProperty
     */
    public IntegerProperty itemsizeProperty() {
        return itemsize;
    }

    /**
     * Sets the size of the item.
     *
     * @param itemsize the new item size value
     */
    public void setItemsize(int itemsize) {
        this.itemsize.set(itemsize);
    }

    /**
     * Gets the unit in which the item is measured.
     *
     * @return the unit of measurement for the item
     */
    public String getUnit() {
        return unit.get();
    }

    /**
     * Returns the StringProperty representing the unit of measurement for the item.
     *
     * @return the unit StringProperty
     */
    public StringProperty unitProperty() {
        return unit;
    }

    /**
     * Sets the unit in which the item is measured.
     *
     * @param unit the new unit value
     */
    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    /**
     * Gets the color of the item.
     *
     * @return the color of the item
     */
    public String getColor() {
        return color.get();
    }

    /**
     * Returns the StringProperty representing the color of the item.
     *
     * @return the color StringProperty
     */
    public StringProperty colorProperty() {
        return color;
    }

    /**
     * Sets the color of the item.
     *
     * @param color the new color value
     */
    public void setColor(String color) {
        this.color.set(color);
    }

    /**
     * Gets the type or classification of the item.
     *
     * @return the type or classification of the item
     */
    public String getType() {
        return type.get();
    }

    /**
     * Returns the StringProperty representing the type or classification of the item.
     *
     * @return the type StringProperty
     */
    public StringProperty typeProperty() {
        return type;
    }

    /**
     * Sets the type or classification of the item.
     *
     * @param type the new type value
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * Getter Method for the Item's Description
     * @return Item's Description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Returns the representative StringProperty for the Item's Description
     * @return Descriptions StringProperty
     */
    public StringProperty desciptionProperty(){
        return description;
    }

    /**
     * Setter Method for the Item's Description.
     * @param description Specified Description
     */
    public void setDescription(String description) {
        this.description.set(description);
    }


}
