package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartProductItems implements Parcelable {


    public static final Creator<CartProductItems> CREATOR = new Creator<CartProductItems>() {
        @Override
        public CartProductItems createFromParcel(Parcel source) {
            return new CartProductItems(source);
        }

        @Override
        public CartProductItems[] newArray(int size) {
            return new CartProductItems[size];
        }
    };
    @SerializedName("tax")
    @Expose
    private double tax;
    @SerializedName("total_price")
    @Expose
    private double totalPrice;
    @SerializedName("total_tax")
    @Expose
    private double totalTax;
    @SerializedName("total_specification_tax")
    @Expose
    private double totalSpecificationTax;
    @SerializedName("item_tax")
    @Expose
    private double itemTax;
    @SerializedName("total_item_tax")
    @Expose
    private double totalItemTax;
    @SerializedName("note_for_item")
    private String itemNote;
    @SerializedName("total_item_price")
    private double totalItemAndSpecificationPrice;
    @SerializedName("item_details")
    private Item productItemsItem;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_price")
    @Expose
    private double itemPrice;
    @SerializedName("total_specification_price")
    @Expose
    private double totalSpecificationPrice;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("specifications")
    @Expose
    private List<ItemSpecification> specifications;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("image_url")
    @Expose
    private List<String> imageUrl;

    public CartProductItems() {
    }

    protected CartProductItems(Parcel in) {
        this.totalPrice = in.readDouble();
        this.totalTax = in.readDouble();
        this.totalSpecificationTax = in.readDouble();
        this.itemTax = in.readDouble();
        this.totalItemTax = in.readDouble();
        this.itemNote = in.readString();
        this.totalItemAndSpecificationPrice = in.readDouble();
        this.productItemsItem = in.readParcelable(Item.class.getClassLoader());
        this.details = in.readString();
        this.itemName = in.readString();
        this.itemPrice = in.readDouble();
        this.totalSpecificationPrice = in.readDouble();
        this.quantity = in.readInt();
        this.itemId = in.readString();
        this.specifications = in.createTypedArrayList(ItemSpecification.CREATOR);
        this.uniqueId = in.readInt();
        this.imageUrl = in.createStringArrayList();
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getTotalSpecificationTax() {
        return totalSpecificationTax;
    }

    public void setTotalSpecificationTax(double totalSpecificationTax) {
        this.totalSpecificationTax = totalSpecificationTax;
    }

    public double getItemTax() {
        return itemTax;
    }

    public void setItemTax(double itemTax) {
        this.itemTax = itemTax;
    }

    public double getTotalItemTax() {
        return totalItemTax;
    }

    public void setTotalItemTax(double totalItemTax) {
        this.totalItemTax = totalItemTax;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public List<ItemSpecification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ItemSpecification> specifications) {
        this.specifications = specifications;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getTotalSpecificationPrice() {
        return totalSpecificationPrice;
    }

    public void setTotalSpecificationPrice(double totalSpecificationPrice) {
        this.totalSpecificationPrice = totalSpecificationPrice;
    }

    public double getTotalItemAndSpecificationPrice() {
        return totalItemAndSpecificationPrice;
    }

    public void setTotalItemAndSpecificationPrice(double totalItemAndSpecificationPrice) {
        this.totalItemAndSpecificationPrice = totalItemAndSpecificationPrice;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Item getProductItemsItem() {
        return productItemsItem;
    }

    public void setProductItemsItem(Item productItemsItem) {
        this.productItemsItem = productItemsItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.totalTax);
        dest.writeDouble(this.totalSpecificationTax);
        dest.writeDouble(this.itemTax);
        dest.writeDouble(this.totalItemTax);
        dest.writeString(this.itemNote);
        dest.writeDouble(this.totalItemAndSpecificationPrice);
        dest.writeParcelable(this.productItemsItem, flags);
        dest.writeString(this.details);
        dest.writeString(this.itemName);
        dest.writeDouble(this.itemPrice);
        dest.writeDouble(this.totalSpecificationPrice);
        dest.writeInt(this.quantity);
        dest.writeString(this.itemId);
        dest.writeTypedList(this.specifications);
        dest.writeInt(this.uniqueId);
        dest.writeStringList(this.imageUrl);
    }
}