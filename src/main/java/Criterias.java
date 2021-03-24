import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Criterias {

    private String lastName;
    private String product;
    private Integer countBuy;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Integer countPassiveBuyer;

    @Override
    public String toString() {
        return "Criterias{" +
                "lastName='" + lastName + '\'' +
                ", product='" + product + '\'' +
                ", countBuy=" + countBuy +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", countPassiveBuyer=" + countPassiveBuyer +
                '}';
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getCountBuy() {
        return countBuy;
    }

    public void setCountBuy(Integer countBuy) {
        this.countBuy = countBuy;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getCountPassiveBuyer() {
        return countPassiveBuyer;
    }

    public void setCountPassiveBuyer(Integer countPassiveBuyer) {
        this.countPassiveBuyer = countPassiveBuyer;
    }

    public Criterias() {
    }

    public Criterias(String lastName) {
        this.lastName = lastName;
    }

    public Criterias(String product, Integer countBuy) {
        this.product = product;
        this.countBuy = countBuy;
    }

    public Criterias(BigDecimal maxPrice, BigDecimal minPrice) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Criterias(Integer countPassiveBuyer) {
        this.countPassiveBuyer = countPassiveBuyer;
    }




}
