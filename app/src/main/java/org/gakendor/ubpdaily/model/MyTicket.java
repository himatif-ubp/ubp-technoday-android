package org.gakendor.ubpdaily.model;

import com.google.gson.annotations.SerializedName;

import org.gakendor.ubpdaily.clients.api.model.User;

/**
 * Created by yaziedda on 3/29/18.
 */

public class MyTicket {

    @SerializedName("product")
    private Product product;

    @SerializedName("ticket")
    private Tiket tiket;

    @SerializedName("payment")
    private Payment payment;

    @SerializedName("user")
    private User user;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Tiket getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
