package org.gakendor.ubpdaily.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yaziedda on 3/29/18.
 */

public class Product implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("sub_judul")
    @Expose
    private String subJudul;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("gambar_besar")
    @Expose
    private String gambarBesar;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("harga")
    @Expose
    private Integer harga;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("stok")
    @Expose
    private Integer stok;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("isteam")
    @Expose
    private Integer isteam;
    @SerializedName("max_team")
    @Expose
    private Integer maxTeam;
    @SerializedName("is_umum")
    @Expose
    private Integer isUmum;
    @SerializedName("is_competition")
    @Expose
    private Integer isCompetition;
    @SerializedName("is_mobile")
    @Expose
    private Integer isMobile;
    @SerializedName("tempat")
    @Expose
    private String tempat;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSubJudul() {
        return subJudul;
    }

    public void setSubJudul(String subJudul) {
        this.subJudul = subJudul;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambarBesar() {
        return gambarBesar;
    }

    public void setGambarBesar(String gambarBesar) {
        this.gambarBesar = gambarBesar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getIsteam() {
        return isteam;
    }

    public void setIsteam(Integer isteam) {
        this.isteam = isteam;
    }

    public Integer getMaxTeam() {
        return maxTeam;
    }

    public void setMaxTeam(Integer maxTeam) {
        this.maxTeam = maxTeam;
    }

    public Integer getIsUmum() {
        return isUmum;
    }

    public void setIsUmum(Integer isUmum) {
        this.isUmum = isUmum;
    }

    public Integer getIsCompetition() {
        return isCompetition;
    }

    public void setIsCompetition(Integer isCompetition) {
        this.isCompetition = isCompetition;
    }

    public Integer getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(Integer isMobile) {
        this.isMobile = isMobile;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
