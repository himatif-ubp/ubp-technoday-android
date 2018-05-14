package org.gakendor.ubpdaily.clients.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yaziedda on 3/28/18.
 */

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("tanggal_lahir")
    @Expose
    private Object tanggalLahir;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("instansi")
    @Expose
    private String instansi;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("isverified")
    @Expose
    private Integer isverified;
    @SerializedName("link")
    @Expose
    private Object link;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("complete")
    @Expose
    private Integer complete;
    @SerializedName("lolos")
    @Expose
    private Integer lolos;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Object getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Object tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getIsverified() {
        return isverified;
    }

    public void setIsverified(Integer isverified) {
        this.isverified = isverified;
    }

    public Object getLink() {
        return link;
    }

    public void setLink(Object link) {
        this.link = link;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getLolos() {
        return lolos;
    }

    public void setLolos(Integer lolos) {
        this.lolos = lolos;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
