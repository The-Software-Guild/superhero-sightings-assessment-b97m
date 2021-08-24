package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Super;
import java.time.LocalDate;
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
}