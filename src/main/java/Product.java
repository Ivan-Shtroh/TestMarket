import java.math.BigDecimal;

public class Product {
    private int product_id;
    private String product;
    private BigDecimal price;

    public Product(int product_id, String product, BigDecimal price) {
        this.product_id = product_id;
        this.product = product;
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
