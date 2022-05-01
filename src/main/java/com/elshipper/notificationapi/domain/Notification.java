package com.elshipper.notificationapi.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String asset;
    @NotNull
    private String assetType;
    @NotNull
    private String value;
    @NotNull
    private String direction;
    @NotNull
    private String frequency;
    private Boolean triggered;

    public Notification() {
    }

    public Notification(String asset, String assetType, String value, String direction, String frequency, Boolean triggered) {
        this.asset = asset;
        this.assetType = assetType;
        this.value = value;
        this.direction = direction;
        this.frequency = frequency;
        this.triggered = triggered;
    }

    public Notification(Integer id, String asset, String assetType, String value, String direction, String frequency, Boolean triggered) {
        this(asset, assetType, value, direction, frequency, triggered);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(asset, that.asset) &&
                Objects.equals(assetType, that.assetType) && Objects.equals(value, that.value) &&
                Objects.equals(direction, that.direction) && Objects.equals(frequency, that.frequency) &&
                Objects.equals(triggered, that.triggered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, asset, assetType, value, direction, frequency, triggered);
    }
}
