package com.edelivery.store.models.singleton;

import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elluminati on 25-Dec-17.
 */

public class UpdateOrder {

    private static UpdateOrder updateOrder = new UpdateOrder();
    private ArrayList<Item> saveNewItem;
    @SerializedName("total_item_tax")
    @Expose
    private double cartOrderTotalTaxPrice;
    @SerializedName("total_cart_price")
    private double cartOrderTotalPrice;
    @SerializedName("total_specification_count")
    private int totalSpecificationCount;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("server_token")
    private String serverToken;
    @SerializedName("total_item_count")
    private int totalItemCount;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetails> orderDetails;

    private UpdateOrder() {
        orderDetails = new ArrayList<>();
        saveNewItem = new ArrayList<>();
    }

    public static UpdateOrder getInstance() {
        return updateOrder;
    }

    public double getCartOrderTotalTaxPrice() {
        return cartOrderTotalTaxPrice;
    }

    public void setCartOrderTotalTaxPrice(double cartOrderTotalTaxPrice) {
        this.cartOrderTotalTaxPrice = cartOrderTotalTaxPrice;
    }

    public int getTotalSpecificationCount() {
        return totalSpecificationCount;
    }

    public void setTotalSpecificationCount(int totalSpecificationCount) {
        this.totalSpecificationCount = totalSpecificationCount;
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

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getCartOrderTotalPrice() {
        return cartOrderTotalPrice;
    }

    public void setCartOrderTotalPrice(double cartOrderTotalPrice) {
        this.cartOrderTotalPrice = cartOrderTotalPrice;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails.clear();
        this.orderDetails.addAll(orderDetails);
    }

    public ArrayList<Item> getSaveNewItem() {
        return saveNewItem;
    }

    public void setSaveNewItem(Item saveNewItem) {
        this.saveNewItem.add(saveNewItem);
    }

    public void clearSaveNewItem() {
        this.saveNewItem.clear();
    }
}
