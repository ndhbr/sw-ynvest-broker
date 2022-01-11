package de.ndhbr.ynvest.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        return this.shares;
    }

    /**
     * Returns instance of share by isin
     *
     * @param isin Stock identifier
     * @return share
     */
    public Optional<Share> getShareByIsin(String isin) {
        return this.shares.stream().filter(
                s -> s.getIsin().equals(isin)
        ).findFirst();
    }

    /**
     * Returns amount of shares for this stock
     *
     * @param isin Stock identifier
     * @return quantity
     */
    public int getShareQuantity(String isin) {
        Optional<Share> share;
        int result = 0;

        if (this.shares == null) {
            return result;
        }

        share = getShareByIsin(isin);

        if (share.isPresent()) {
            result = share.get().getQuantity();
        }

        return result;
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

    public void updateShare(Share share) {
        if (this.shares == null) {
            this.insertShare(share);
        }

        for (Share s : this.shares) {
            if (s.getIsin().equals(share.getIsin())) {
                int index = this.shares.indexOf(s);
                if (index > -1) {
                    this.shares.set(index, share);
                }
                break;
            }
        }
    }
}
