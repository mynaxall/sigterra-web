package itomy.sigterra.domain;

import itomy.sigterra.domain.Visitor;

public class TopDomain {
    private Visitor visitor;
    private long clicks;

    public TopDomain() {}

    public TopDomain(Visitor visitor, long clicks) {
        this.visitor = visitor;
        this.clicks = clicks;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }
}
