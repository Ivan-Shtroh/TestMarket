import java.math.BigDecimal;
import java.time.LocalDate;

public class Purchases {
    private String product;
    private LocalDate localDate;
    private BigDecimal sumExpensesProduct;


    public Purchases() {
    }

    @Override
    public String toString() {
        return "Purchases{" +
                ", product='" + product + '\'' +
                ", localDate=" + localDate +
                ", sumExpensesProduct=" + sumExpensesProduct +
                '}';
    }

    public BigDecimal getSumExpensesProduct() {
        return sumExpensesProduct;
    }

    public void setSumExpensesProduct(BigDecimal sumExpensesProduct) {
        this.sumExpensesProduct = sumExpensesProduct;
    }



    public String getProduct() {
        return product;
    }

    public void setProduct(String product_id) {
        this.product = product_id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }


}