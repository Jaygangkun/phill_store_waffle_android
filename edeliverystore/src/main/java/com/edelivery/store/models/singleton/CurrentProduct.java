package com.edelivery.store.models.singleton;

import com.edelivery.store.models.datamodel.Product;

import java.util.ArrayList;

/**
 * Created by elluminati on 08-Jun-17.
 */

public class CurrentProduct {
    private ArrayList<Product> productDataList;

    private static CurrentProduct currentProduct = new CurrentProduct();

    private CurrentProduct() {
        productDataList = new ArrayList<>();
    }

    public static CurrentProduct getInstance() {
        return currentProduct;
    }

    public ArrayList<Product> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(ArrayList<Product> productDataList) {
        this.productDataList.clear();
        this.productDataList.addAll(productDataList);
    }

}
