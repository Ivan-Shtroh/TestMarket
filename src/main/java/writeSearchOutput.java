public class writeSearchOutput {
    private String operation;
    private Result [] results;

    public String getOperation() {
        return operation;
    }

    public Result[] getResults() {
        return results;
    }

    public writeSearchOutput(String operation, Result[] results) {
        this.operation = operation;
        this.results = results;
    }
}
