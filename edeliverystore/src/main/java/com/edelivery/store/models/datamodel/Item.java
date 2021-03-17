package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Item implements Parcelable, Comparable<Item> {


    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;
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
    @SerializedName("total_item_tax")
    @Expose
    private double totalItemTax;
    @SerializedName("item_tax")
    @Expose
    private double itemTax;
    @SerializedName("item_price_without_offer")
    @Expose
    private double itemPriceWithoutOffer;
    @SerializedName("offer_message_or_percentage")
    @Expose
    private String offerMessageOrPercentage;
    @SerializedName("note_for_item")
    @Expose
    private String noteForItem;
    @SerializedName("total_item_price")
    @Expose
    private double totalItemAndSpecificationPrice;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_price")
    @Expose
    private double itemPrice;
    @SerializedName("productDescription")
    @Expose
    private String productDescription;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("server_token")
    @Expose
    private String serverToken;
    @SerializedName("is_visible_in_store")
    @Expose
    private boolean isVisibleInStore;
    @SerializedName("image_url")
    @Expose
    private List<String> imageUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_item_in_stock")
    @Expose
    private boolean isItemInStock;
    @SerializedName("no_of_order")
    @Expose
    private int noOfOrder;
    @SerializedName("is_default")
    @Expose
    private boolean isDefault;
    @SerializedName("specifications")
    @Expose
    private ArrayList<ItemSpecification> specifications;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("product_id")
    @Expose
    private String productId;
    private String productName;

    @SerializedName("name")
    @Expose
    private Object name;

    @SerializedName("details")
    @Expose
    private Object details;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("total_specification_price")
    @Expose
    private double totalSpecificationPrice;
    @SerializedName("specifications_unique_id_count")
    @Expose
    private int specifications_unique_id_count;
    @SerializedName("unique_id")
    @Expose
    private Integer uniqueId;
    private boolean isArrayData;

    public Item() {
    }

    protected Item(Parcel in) {
        sequenceNumber = in.readLong();
        tax = in.readDouble();
        totalPrice = in.readDouble();
        totalTax = in.readDouble();
        totalSpecificationTax = in.readDouble();
        totalItemTax = in.readDouble();
        itemTax = in.readDouble();
        itemPriceWithoutOffer = in.readDouble();
        offerMessageOrPercentage = in.readString();
        noteForItem = in.readString();
        totalItemAndSpecificationPrice = in.readDouble();
        itemName = in.readString();
        quantity = in.readInt();
        itemId = in.readString();
        itemPrice = in.readDouble();
        productDescription = in.readString();
        storeId = in.readString();
        serverToken = in.readString();
        isVisibleInStore = in.readByte() != 0;
        imageUrl = in.createStringArrayList();
        createdAt = in.readString();
        isItemInStock = in.readByte() != 0;
        noOfOrder = in.readInt();
        isDefault = in.readByte() != 0;
        specifications = in.createTypedArrayList(ItemSpecification.CREATOR);
        updatedAt = in.readString();
        price = in.readDouble();
        instruction = in.readString();
        productId = in.readString();
        productName = in.readString();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.details = !isArrayData ? in.readString() : in.createStringArrayList();
        id = in.readString();
        totalSpecificationPrice = in.readDouble();
        specifications_unique_id_count = in.readInt();
        if (in.readByte() == 0) {
            uniqueId = null;
        } else {
            uniqueId = in.readInt();
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(sequenceNumber);
        dest.writeDouble(tax);
        dest.writeDouble(totalPrice);
        dest.writeDouble(totalTax);
        dest.writeDouble(totalSpecificationTax);
        dest.writeDouble(totalItemTax);
        dest.writeDouble(itemTax);
        dest.writeDouble(itemPriceWithoutOffer);
        dest.writeString(offerMessageOrPercentage);
        dest.writeString(noteForItem);
        dest.writeDouble(totalItemAndSpecificationPrice);
        dest.writeString(itemName);
        dest.writeInt(quantity);
        dest.writeString(itemId);
        dest.writeDouble(itemPrice);
        dest.writeString(productDescription);
        dest.writeString(storeId);
        dest.writeString(serverToken);
        dest.writeByte((byte) (isVisibleInStore ? 1 : 0));
        dest.writeStringList(imageUrl);
        dest.writeString(createdAt);
        dest.writeByte((byte) (isItemInStock ? 1 : 0));
        dest.writeInt(noOfOrder);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeTypedList(specifications);
        dest.writeString(updatedAt);
        dest.writeDouble(price);
        dest.writeString(instruction);
        dest.writeString(productId);
        dest.writeString(productName);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
            dest.writeString(String.valueOf(details));
        } else {
            dest.writeStringList((List<String>) name);
            dest.writeStringList((List<String>) details);
        }
        dest.writeString(id);
        dest.writeDouble(totalSpecificationPrice);
        dest.writeInt(specifications_unique_id_count);
        if (uniqueId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(uniqueId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
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

    public double getTotalItemTax() {
        return totalItemTax;
    }

    public void setTotalItemTax(double totalItemTax) {
        this.totalItemTax = totalItemTax;
    }

    public double getItemTax() {
        return itemTax;
    }

    public void setItemTax(double itemTax) {
        this.itemTax = itemTax;
    }

    public double getItemPriceWithoutOffer() {
        return itemPriceWithoutOffer;
    }

    public void setItemPriceWithoutOffer(double itemPriceWithoutOffer) {
        this.itemPriceWithoutOffer = itemPriceWithoutOffer;
    }

    public String getOfferMessageOrPercentage() {
        return offerMessageOrPercentage;
    }

    public void setOfferMessageOrPercentage(String offerMessageOrPercentage) {
        this.offerMessageOrPercentage = offerMessageOrPercentage;
    }

    public int getSpecifications_unique_id_count() {
        return specifications_unique_id_count;
    }

    public void setSpecifications_unique_id_count(int specifications_unique_id_count) {
        this.specifications_unique_id_count = specifications_unique_id_count;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getNoteForItem() {
        return noteForItem;
    }

    public void setNoteForItem(String noteForItem) {
        this.noteForItem = noteForItem;
    }

    public boolean isIsVisibleInStore() {
        return isVisibleInStore;
    }

    public void setIsVisibleInStore(boolean isVisibleInStore) {
        this.isVisibleInStore = isVisibleInStore;
    }

    public int getSpecificationsUniqueIdCount() {
        return specifications_unique_id_count;
    }

    public void setSpecificationsUniqueIdCount(int specifications_unique_id_count) {
        this.specifications_unique_id_count = specifications_unique_id_count;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getTotalItemAndSpecificationPrice() {
        return totalItemAndSpecificationPrice;
    }

    public void setTotalItemAndSpecificationPrice(double totalItemAndSpecificationPrice) {
        this.totalItemAndSpecificationPrice = totalItemAndSpecificationPrice;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isItemInStock() {
        return isItemInStock;
    }

    public void setItemInStock(boolean itemInStock) {
        isItemInStock = itemInStock;
    }

    public int getNoOfOrder() {
        return noOfOrder;
    }

    public void setNoOfOrder(int noOfOrder) {
        this.noOfOrder = noOfOrder;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public ArrayList<ItemSpecification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(ArrayList<ItemSpecification> specifications) {
        this.specifications = specifications;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        if (name instanceof String) {
            return String.valueOf(name);
        } else {
            return Utilities.getDetailStringFromList((ArrayList<String>) name,
                    Language.getInstance().
                            getStoreLanguageIndex());

        }


    }

    public List<String> getNameList() {
        return ((ArrayList<String>) name);
    }

    public String getDetails() {
        if (details instanceof String) {
            return String.valueOf(details);
        } else {
            return Utilities.getDetailStringFromList((ArrayList<String>) details,
                    Language.getInstance().
                            getStoreLanguageIndex());

        }

    }

    public List<String> getDetailsList() {
        return ((ArrayList<String>) details);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalSpecificationPrice() {
        return totalSpecificationPrice;
    }

    public void setTotalSpecificationPrice(double totalSpecificationPrice) {
        this.totalSpecificationPrice = totalSpecificationPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isVisibleInStore() {
        return isVisibleInStore;
    }

    public void setVisibleInStore(boolean visibleInStore) {
        isVisibleInStore = visibleInStore;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNameList(List<String> name) {
        this.name = name;
    }

    public void setDetailsList(List<String> details) {
        this.details = details;
    }


    public void setName(String name) {

    }

    public void setDetails(String detail) {

    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int compareTo(Item o) {
        return this.sequenceNumber > o.getSequenceNumber() ? 1 : -1;
    }
}