import java.math.BigDecimal;

public class writeStatOutput {
    private String operation;
    private int totalDays;
    private Customer [] customers;
    private BigDecimal totalExpenses;
    private BigDecimal avgExpenses;

    public writeStatOutput(String operation, int totalDays, Customer[] customers, BigDecimal totalExpenses, BigDecimal avgExpenses) {
        this.operation = operation;
        this.totalDays = totalDays;
        this.customers = customers;
        this.totalExpenses = totalExpenses;
        this.avgExpenses = avgExpenses;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getAvgExpenses() {
        return avgExpenses;
    }

    public void setAvgExpenses(BigDecimal avgExpenses) {
        this.avgExpenses = avgExpenses;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public Customer[] getCustomers() {
        return customers;
    }

    public void setCustomers(Customer[] customers) {
        this.customers = customers;
    }
}
