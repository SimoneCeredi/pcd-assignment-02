package pcd.assignment.model;

public abstract class AbstractModel implements Model {
    private int ni;
    private int maxl;
    private int n;

    public AbstractModel(int ni, int maxl, int n) {
        this.ni = ni;
        this.maxl = maxl;
        this.n = n;
    }

    @Override
    public int getNi() {
        return ni;
    }

    @Override
    public void setNi(int ni) {
        this.ni = ni;
    }

    @Override
    public int getMaxl() {
        return maxl;
    }

    @Override
    public void setMaxl(int maxl) {
        this.maxl = maxl;
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public void setN(int n) {
        this.n = n;
    }
}
