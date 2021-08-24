package com.bm.superheroessightings.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a superhero/villain 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public class Super {
    private int id;
    private String name;
    private String description;
    private boolean isHero;
    private List<Superpower> superpowers;
    private List<Organization> organizations;
    private Map<LocalDate, Location> sightings;

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

    public boolean isIsHero() {
	return isHero;
    }

    public void setIsHero(boolean isHero) {
	this.isHero = isHero;
    }

    public List<Superpower> getSuperpowers() {
	return superpowers;
    }

    public void setSuperpowers(List<Superpower> superpowers) {
	this.superpowers = superpowers;
    }

    public List<Organization> getOrganizations() {
	return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
	this.organizations = organizations;
    }

    public Map<LocalDate, Location> getSightings() {
	return sightings;
    }

    public void setSightings(Map<LocalDate, Location> sightings) {
	this.sightings = sightings;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 61 * hash + this.id;
	hash = 61 * hash + Objects.hashCode(this.name);
	hash = 61 * hash + Objects.hashCode(this.description);
	hash = 61 * hash + (this.isHero ? 1 : 0);
	hash = 61 * hash + Objects.hashCode(this.superpowers);
	hash = 61 * hash + Objects.hashCode(this.organizations);
	hash = 61 * hash + Objects.hashCode(this.sightings);
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
	final Super other = (Super) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (this.isHero != other.isHero) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	if (!Objects.equals(this.description, other.description)) {
	    return false;
	}
	if (!Objects.equals(this.superpowers, other.superpowers)) {
	    return false;
	}
	if (!Objects.equals(this.organizations, other.organizations)) {
	    return false;
	}
	return Objects.equals(this.sightings, other.sightings);
    }
}