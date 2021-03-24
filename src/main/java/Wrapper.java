public class Wrapper {
    private Criterias [] criterias;

    public Wrapper(Criterias[] criterias) {
        this.criterias = criterias;
    }

    public Wrapper() {
    }

    public Criterias[] getCriterias() {
        return criterias;
    }

    public void setCriterias(Criterias[] criterias) {
        this.criterias = criterias;
    }
}
