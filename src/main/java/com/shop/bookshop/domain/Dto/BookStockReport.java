package com.shop.bookshop.domain.Dto;

public class BookStockReport {
    private String title;
    private int quantityInStock;
    private long quantitySold;

    public BookStockReport(String title, int quantityInStock, long quantitySold) {
        this.title = title;
        this.quantityInStock = quantityInStock;
        this.quantitySold = quantitySold;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(long quantitySold) {
        this.quantitySold = quantitySold;
    }
}
