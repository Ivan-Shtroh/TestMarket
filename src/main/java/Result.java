public class Result {

      private Criterias criterias;
      private  Customer [] results;

    public Result(Criterias criterias, Customer[] results) {
        this.criterias = criterias;
        this.results = results;
    }

    public Criterias getCriterias() {
        return criterias;
    }

    public Customer[] getResults() {
        return results;
    }
}
