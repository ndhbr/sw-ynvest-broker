package de.ndhbr.ynvest.entity;

import de.ndhbr.ynvest.entity.util.SingleIdEntity;
import de.ndhbr.ynvest.enumeration.CustomerType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Customer extends SingleIdEntity<String> implements UserDetails {
    @Id
    @Column(length = 100)
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType = CustomerType.ROLE_NOT_VERIFIED;
    @NotNull
    private Date registeredOn;
    @Embedded
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    private BankAccount bankAccount;
    @OneToOne(orphanRemoval = true)
    private Portfolio portfolio;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    @OrderBy("placedOn DESC")
    private List<StockOrder> stockOrders;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<StockOrder> getOrders() {
        return stockOrders;
    }

    public void setOrders(List<StockOrder> orders) {
        this.stockOrders = orders;
    }

    public void addOrder(StockOrder stockOrder) {
        if (this.stockOrders == null) {
            this.stockOrders = new ArrayList<>();
        }
        this.stockOrders.add(stockOrder);
    }

    @Override
    public String getID() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> Customer.this.customerType.name());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name +
                '}';
    }
}
