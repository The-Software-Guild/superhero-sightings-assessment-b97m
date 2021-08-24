package com.bm.superheroessightings.service;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Sighting;
import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.dao.SightingDao;
import com.bm.superheroessightings.dao.SuperDao;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The service layer of the whole application
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
@Component
public class Service {
     
    private SuperDao superDao;
    private SightingDao sightingDao;

    @Autowired
    public Service(SuperDao superDao, SightingDao sightingDao) {
	this.superDao = superDao;
	this.sightingDao = sightingDao;
    }

    /**
     * Attempts to parse a date from the indicated string
     * 
     * If this parsing succeeds, an instance with a LocalDate
     * is returned. Otherwise, an empty instance is returned.
     * 
     * @param dateStr
     * @return 
     */
    public Optional<LocalDate> parsedDate(String dateStr) {
	Optional<LocalDate> receivedInstance;
	try {
	    receivedInstance = Optional.of(LocalDate.parse(dateStr));
	} catch (Exception ex) {
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }
    
    /**
     * Attempts to add a sighting for the given superhero/villain, location, 
     * and date. The value depends on the success of this operation
     * 
     * @param superId
     * @param locationId
     * @param date
     * @return The aforementioned value
     */
    public boolean addSighting(int superId, int locationId, LocalDate date) {
	return sightingDao.createSighting(superId, locationId, date);
    }


    /**
     * Retrieves a list of all the superheroes that have been sighted
     * at a given location
     * 
     * @param locationId
     * @return The aforementioned list
     */
    public List<Super> getSupersBySightingLocationId(int locationId) {
    	return superDao.getSupersBySightingLocationId(locationId);
    }

    /**
     * Retrieves a list of all the locations that the superhero/villain
     * has been sighted in
     * 
     * @param superId
     * @return The aforementioned list
     */
    public List<Location> getSightingLocationsForSuper(int superId) {
	Optional<Super> possSuper = superDao.getSuperById(superId);
	if (possSuper.isPresent()) {
	    return new LinkedList<>(possSuper.get().getSightings().values());
	}
	return new LinkedList<>();
    }
    
    /**
     * Retrieves a list of all the sightings on a given date
     * 
     * @param date
     * @return 
     */
    public List<Sighting> getSightingsByDate(LocalDate date) {
	System.out.println(date);
	return sightingDao.getSightingsByDate(date);
    }

    /**
     * Retrieves a list of all the superheroes that are members
     * of a given organization
     * 
     * @param organizationId
     * @return The aforementioned list
     */
    public List<Super> getSupersByOrganizationId(int organizationId) {
	return superDao.getSupersByOrganizationId(organizationId);
    }

    /**
     * Retrieves a list of all the organizations that this superhero/villain
     * is a member of
     * 
     * @param superId
     * @return The aforementioned list
     */
    public List<Organization> getOrganizationsOfSuper(int superId) {
	Optional<Super> possSuper = superDao.getSuperById(superId);
	if (possSuper.isPresent()) {
	    return possSuper.get().getOrganizations();
	}
	return new LinkedList<>();
    }
}