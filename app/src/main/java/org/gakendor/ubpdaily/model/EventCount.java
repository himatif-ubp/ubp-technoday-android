package org.gakendor.ubpdaily.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yaziedda on 5/6/18.
 */

public class EventCount {

    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("absen")
    @Expose
    private Integer absen;

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAbsen() {
        return absen;
    }

    public void setAbsen(Integer absen) {
        this.absen = absen;
    }
}
