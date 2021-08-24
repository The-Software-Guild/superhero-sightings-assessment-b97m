package com.bm.superheroessightings.controller.dto;

import java.util.Objects;

/**
 * Represents a Superpower 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
public class Superpower {
    private int id;
    private String name;

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

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 67 * hash + this.id;
	hash = 67 * hash + Objects.hashCode(this.name);
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
	final Superpower other = (Superpower) obj;
	if (this.id != other.id) {
	    return false;
	}
	return Objects.equals(this.name, other.name);
    }
}