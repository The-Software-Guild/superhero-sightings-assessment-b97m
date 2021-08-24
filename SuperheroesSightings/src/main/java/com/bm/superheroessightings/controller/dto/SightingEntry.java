package com.bm.superheroessightings.controller.dto;

import java.time.LocalDate;

/**
 * An entry for reporting a sighting 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public class SightingEntry {
    private int superId;
    private int locationId;
    private String dateStr;

    public int getSuperId() {
	return superId;
    }

    public void setSuperId(int superId) {
	this.superId = superId;
    }

    public int getLocationId() {
	return locationId;
    }

    public void setLocationId(int locationId) {
	this.locationId = locationId;
    }

    public String getDateStr() {
	return dateStr;
    }

    public void setDateStr(String dateStr) {
	this.dateStr = dateStr;
    }
}
