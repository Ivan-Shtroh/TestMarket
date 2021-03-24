public class Main {
    public static void main(String[] args) {

        Operations op1 = new Operations();
        op1.operation("input-search.json", "output-search.json", "search");
        Operations op2 = new Operations();
        op2.operation("input-stat.json", "output-stat.json", "stat");
    }
}
