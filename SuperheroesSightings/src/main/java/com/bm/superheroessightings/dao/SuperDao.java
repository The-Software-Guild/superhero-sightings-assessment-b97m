package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Super;
import java.util.List;
import java.util.Optional;

/**
 * Handles storage and access of superhero/villain information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
public interface SuperDao {
    /**
     * Retrieves a list of all the superheroes that have been sighted
     * at a given location
     * 
     * @param locationId
     * @return The aforementioned list
     */
    public List<Super> getSupersBySightingLocationId(int locationId);

    /**
     * Retrieves a list of all the superheroes that are members
     * of a given organization
     * 
     * @param organizationId
     * @return The aforementioned list
     */
    public List<Super> getSupersByOrganizationId(int organizationId);

    /**
     * Retrieves the superhero/villain corresponding to this id
     * If no such superhero/villain exists, an empty instance will
     * be returned. Otherwise, an instance containing superhero/villain
     * will be returned 
     * 
     * @param superId
     * @return The aforementioned instance
     */
    public Optional<Super> getSuperById(int superId);
    
    /**
     * Retrieves a list of all the superheroes/villains
     * 
     * @return The aforementioned list
     */
    public List<Super> getSupers();

    /**
     * Attempts to create a new superhero with the following properties
     * 
     * If this creation process succeeds, an instance with the created
     * Super is returned. Otherwise, an empty instance is returned.
     * 
     * @param name
     * @param description
     * @param isHero 
     * @return the aforementioned instance
     */
    public Optional<Super> createSuper(String name, String description, boolean isHero);

    /**
     * Attempts to update the super with the corresponding id 
     * with updated name, description, and isHero status
     * 
     * The value returned depends on the success of the update
     * 
     * @param superId
     * @param name
     * @param description
     * @param isHero
     * @return The aforementioned value
     */
    public boolean updateSuper(int superId, String name, String description, boolean isHero);

    /**
     * Attempts to delete the super associated with the id.
     * 
     * The value returned depends on the success of the deletion
     * 
     * @param superId
     * @return The aforementioned instance
     */
    public boolean deleteSuper(int superId);
}