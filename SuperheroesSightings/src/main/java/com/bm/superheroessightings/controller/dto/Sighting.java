package com.bm.superheroessightings.controller.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a sighting 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public class Sighting {
    private Super sightedSuper;
    private Location sightingLocation;
    private LocalDate sightingDate;

    public Super getSightedSuper() {
	return sightedSuper;
    }

    public void setSightedSuper(Super sightedSuper) {
	this.sightedSuper = sightedSuper;
    }

    public Location getSightingLocation() {
	return sightingLocation;
    }

    public void setSightingLocation(Location sightingLocation) {
	this.sightingLocation = sightingLocation;
    }

    public LocalDate getSightingDate() {
	return sightingDate;
    }

    public void setSightingDate(LocalDate sightingDate) {
	this.sightingDate = sightingDate;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 41 * hash + Objects.hashCode(this.sightedSuper);
	hash = 41 * hash + Objects.hashCode(this.sightingLocation);
	hash = 41 * hash + Objects.hashCode(this.sightingDate);
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
	final Sighting other = (Sighting) obj;
	if (!Objects.equals(this.sightedSuper, other.sightedSuper)) {
	    return false;
	}
	if (!Objects.equals(this.sightingLocation, other.sightingLocation)) {
	    return false;
	}
	return Objects.equals(this.sightingDate, other.sightingDate);
    }
}
