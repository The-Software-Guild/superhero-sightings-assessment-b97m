package com.bm.superheroessightings.controller.dto;

import java.util.Objects;

/**
 * Represents a superhero/villain organization 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public class Organization {
    private int id;
    private String name, description, address, contact;

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

    public String getContact() {
	return contact;
    }

    public void setContact(String contact) {
	this.contact = contact;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 71 * hash + this.id;
	hash = 71 * hash + Objects.hashCode(this.name);
	hash = 71 * hash + Objects.hashCode(this.description);
	hash = 71 * hash + Objects.hashCode(this.address);
	hash = 71 * hash + Objects.hashCode(this.contact);
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
	final Organization other = (Organization) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	if (!Objects.equals(this.description, other.description)) {
	    return false;
	}
	if (!Objects.equals(this.address, other.address)) {
	    return false;
	}
	return Objects.equals(this.contact, other.contact);
    }
}