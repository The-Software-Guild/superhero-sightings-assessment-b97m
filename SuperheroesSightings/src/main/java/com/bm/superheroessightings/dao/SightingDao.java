package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Sighting;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Handles the storage and retrieval of sighting information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public interface SightingDao {
    /**
     * Retrieves a list of all sightings
     * 
     * @return The above list
     */
    public List<Sighting> getSightings();
    
    /**
     * Retrieves a list of the ten most recent sightings 
     * @return The above list
     */
    public List<Sighting> getLatestSightings();

    /**
     * Attempts to find a sighting by its id.
     * 
     * If this succeeds, then an instance containing the sighting
     * is returned. Otherwise, an empty instance is returned.
     * 
     * @param sightingId
     * @return The above instance
     */
    public Optional<Sighting> getSightingById(int sightingId);
    
    /**
     * Attempts to add a sighting for the given superhero/villain, location, 
     * and date. If the addition is successful, an instance containing the
     * created sighting will be returned. Otherwise, an empty instance will
     * be returned.
     * 
     * @param superId
     * @param locationId
     * @param date
     * @return The aforementioned instance
     */
    public Optional<Sighting> createSighting(
	int superId, 
	int locationId, 
	LocalDate date
    );

    /**
     * Retrieves a list of all the sightings on a given date
     * 
     * @param date
     * @return The aforementioned list
     */
    List<Sighting> getSightingsByDate(LocalDate date);

    /**
     * Attempts to update the sighting with the corresponding id 
     * 
     * The value returned depends on the success of the update operation
     * 
     * @param sightingId
     * @param superId
     * @param locationId
     * @param date
     * @return The above value
     */
    public boolean updateSighting(
	int sightingId,
	int superId, 
	int locationId, 
	LocalDate date);

    /**
     * Attempts to delete the sighting
     * 
     * The value returned depends on the success of the deletion process
     * @param sightingId
     * @return The above value
     */
    public boolean deleteSighting(int sightingId);
}