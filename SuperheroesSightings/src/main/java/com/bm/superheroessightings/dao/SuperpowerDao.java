package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Superpower;
import java.util.List;
import java.util.Optional;

/**
 * Handles the storage and retrieval of Superpowers
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
public interface SuperpowerDao {
    /**
     * Retrieves a list of all superpowers
     * 
     * @return The above list
     */
    public List<Superpower> getSuperpowers();

    /**
     * Attempts to retrieve the superpower corresponding to the
     * superpowerId
     * 
     * If such a superpower is found, an instance containing 
     * it is returned. Otherwise, an empty instance is returned.
     * 
     * @return The above instance
     */
    public Optional<Superpower> getSuperpowerById(int superpowerId);

    /**
     * Attempts to create a superpower with the given name
     * 
     * If this creation process succeeds, then an instance containing
     * the created superpower is returned. Otherwise, an empty instance
     * is returned.
     * 
     * @param name
     * @return The above instance
     */
    public Optional<Superpower> createSuperpower(String name);

    /**
     * Attempts to update the superpower with the given id by changing
     * its name. The value returned depends on the success of the update
     * operation.
     * 
     * @param superpowerId
     * @param name
     * @return The above value
     */
    public boolean updateSuperpower(int superpowerId, String name);

    /**
     * Attempts to delete the superpower with the given id.
     * 
     * The value returned depends on the success of the deletion operation.
     * 
     * @param superpowerId
     * @return The above value
     */
    public boolean deleteSuperpower(int superpowerId);
}