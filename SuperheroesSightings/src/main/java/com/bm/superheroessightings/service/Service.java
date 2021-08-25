package com.bm.superheroessightings.service;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Sighting;
import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.controller.dto.Superpower;
import com.bm.superheroessightings.dao.OrganizationDao;
import com.bm.superheroessightings.dao.SightingDao;
import com.bm.superheroessightings.dao.SuperDao;
import com.bm.superheroessightings.dao.SuperpowerDao;
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
    private OrganizationDao organizationDao;
    private SuperpowerDao superpowerDao;

    @Autowired
    public Service(
	SuperDao superDao, 
	SightingDao sightingDao, 
	OrganizationDao organizationDao,
	SuperpowerDao superpowerDao) {

	this.superDao = superDao;
	this.sightingDao = sightingDao;
	this.organizationDao = organizationDao;
	this.superpowerDao = superpowerDao;
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
     * Attempts to add a sighting with the indicated superhero/villain,
     * location, and date. If successful, an instance with the created sighting
     * is returned.
     * 
     * @param superId
     * @param locationId
     * @param date
     * @return The above instance
     */
    public Optional<Sighting> addSighting(int superId, int locationId, LocalDate date) {
	return sightingDao.createSighting(superId, locationId, date);
    }

    /**
     * A list of the latest sightings.
     * 
     * @return The above list
     */
    public List<Sighting> getLatestSightings() {
	return sightingDao.getLatestSightings();
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

    /**
     * Retrieves a list of all supers
     * @return The above list
     */
    public List<Super> getSupers() {
	return superDao.getSupers();
    }

    /**
     * Retrieves the super corresponding to this id
     */
    public Optional<Super> getSuperById(int superId) {
	return superDao.getSuperById(superId);
    }

    public Optional<Super> createSuper(String name, String description, boolean isHero) {
    	return superDao.createSuper(name, description, isHero);
    }

    public List<Organization> getOrganizations() {
    	return organizationDao.getOrganizations();
    }

    public boolean addOrganizationForSuper(int superId, int organizationId) {
	return superDao.addOrganizationForSuper(superId, organizationId);
    }

    public List<Superpower> getSuperpowers() {
	return superpowerDao.getSuperpowers();
    }

    public boolean addSuperpowerForSuper(int superId, int superpowerId) {
	return superDao.addSuperpowerForSuper(superId, superpowerId);
    }

    public boolean updateSuper(int superId, String name, String description, boolean isHero) {
	return superDao.updateSuper(superId, name, description, isHero);
    }

    public boolean deleteSuper(int superId) {
	return superDao.deleteSuper(superId);
    }

    public Optional<Superpower> addSuperpower(String name) {
	return superpowerDao.createSuperpower(name);
    }

    public Optional<Superpower> getSuperpowerById(int superpowerId) {
	return superpowerDao.getSuperpowerById(superpowerId);
    }

    public boolean updateSuperpower(int superpowerId, String superpowerName) {
	return superpowerDao.updateSuperpower(superpowerId, superpowerName);
    }

    public boolean deleteSuperpower(int superpowerId) {
	return superpowerDao.deleteSuperpower(superpowerId);
    }
}