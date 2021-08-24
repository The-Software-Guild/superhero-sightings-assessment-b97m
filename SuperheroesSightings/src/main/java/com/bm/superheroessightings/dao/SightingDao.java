package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Sighting;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles the storage and retrieval of sighting information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public interface SightingDao {
    /**
     * Attempts to add a sighting for the given superhero/villain, location, 
     * and date. The value depends on the success of this operation
     * 
     * @param superId
     * @param locationId
     * @param date
     * @return The aforementioned value
     */
    public boolean addSighting(int superId, int locationId, LocalDate date);

    /**
     * Retrieves a list of all the sightings on a given date
     * 
     * @param date
     * @return The aforementioned list
     */
    List<Sighting> getSightingsByDate(LocalDate date);
}
