package com.bm.superheroessightings.controller.dto;

import java.util.Objects;

/**
 * Represents a location 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public class Location {
    private int id;
    private String name, description, address;
    private double latitude, longitude;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public double getLatitude() {
	return latitude;
    }

    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    public double getLongitude() {
	return longitude;
    }

    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 47 * hash + this.id;
	hash = 47 * hash + Objects.hashCode(this.name);
	hash = 47 * hash + Objects.hashCode(this.description);
	hash = 47 * hash + Objects.hashCode(this.address);
	hash = 47 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
	hash = 47 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Location other = (Location) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
	    return false;
	}
	if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	if (!Objects.equals(this.description, other.description)) {
	    return false;
	}
	return Objects.equals(this.address, other.address);
    }
}