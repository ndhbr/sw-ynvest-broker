package de.ndhbr.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Portfolio {
    @Id
    private long portfolioId;
    @ElementCollection
    private List<Share> shares;

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<Share> getShares() {
        return shares;
    }

    public void setShares(List<Share> shares) {
        this.shares = shares;
    }

    public void insertShare(Share share) {
        if (this.shares == null) {
            this.shares = new ArrayList<Share>();
        }
        this.shares.add(share);
    }
}
