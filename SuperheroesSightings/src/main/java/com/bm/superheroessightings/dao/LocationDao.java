package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import java.util.List;
import java.util.Optional;

/**
 * Handles the storage and retrieval of Lccations
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
public interface LocationDao {
    /**
     * Retrieves a list of all locations
     * @return The above list
     */
    public List<Location> getLocations(); 

    /**
     * Attempts to retrieve a location with the given id
     * 
     * If such a location exists, an instance containing
     * it is returned. Otherwise, an empty instance is returned.
     * 
     * @param locationId
     * @return The above instance
     */
    public Optional<Location> getLocationById(int locationId);

    /**
     * Attempts to create a location with the indicated
     * information. If the creation process succeeds, an instance
     * containing the created location is returned.
     * 
     * Otherwise, an empty instance is returned.
     * 
     * @param name
     * @param description
     * @param address
     * @param latitude
     * @param longitude
     * @return 
     */
    public Optional<Location> createLocation(
	String name,
	String description,
	String address,
	double latitude,
	double longitude
    );

    /**
     * Attempts to update the location with the id
     * by changing the corresponding information.
     * 
     * The value returned depends on the success of the update operation
     * 
     * @param locationId
     * @param name
     * @param description
     * @param address
     * @param latitude
     * @param longitude
     * @return The above value
     */
    public boolean updateLocation(
	int locationId, 
	String name,
	String description,
	String address,
	double latitude,
	double longitude
    );

    /**
     * Attempts to delete the location with the corresponding id
     * 
     * The value returned depends on the success of the deletion
     * operation.
     * 
     * @param locationId
     * @return The above value
     */
    public boolean deleteLocation(int locationId);
}