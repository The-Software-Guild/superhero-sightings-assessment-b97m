package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Sighting;
import com.bm.superheroessightings.controller.dto.Super;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * An implementation of the SightingDao interface that
 * interacts with a MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
@Repository
public class SightingDaoDB implements SightingDao {
    private JdbcTemplate jdbc;

    @Autowired
    public SightingDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public boolean addSighting(int superId, int locationId, LocalDate date) {
	if (date == null) {
	    return false;
	}

	try {
	    int rowsUpdated = jdbc.update(
		"INSERT INTO sightings VALUES (?, ?, ?)", 
		superId, 
		locationId, 
		Date.valueOf(date)
	    );
	    return rowsUpdated > 0;
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
	if (date == null) {
	    return new LinkedList<>();
	}
    	List<Sighting> receivedList;
	try {
	    receivedList = jdbc.query(
		"SELECT SI.dateOfSighting, "
		+ "L.locationId, "
		+ "L.name AS location_name, "
		+ "L.description AS location_description, "
		+ "L.address, "
		+ "L.latitude, "
		+ "L.longitude, "
		+ "SU.superId, "
		+ "SU.name AS super_name, "
		+ "SU.description AS super_description, "
		+ "SU.isHero, "
		+ "SU.superpower "
		+ "FROM sightings SI "
		+ "INNER JOIN locations L ON SI.locationId = L.locationId "
		+ "INNER JOIN supers SU ON SI.superId = SU.superId " 
		+ "WHERE dateOfSighting = ?", 
		(ResultSet rs, int index) -> {
		    Location location = new Location();
		    location.setId(rs.getInt("locationId"));
		    location.setName(rs.getString("location_name"));
		    location.setDescription(rs.getString("location_description"));
		    location.setAddress(rs.getString("address"));
		    location.setLatitude(rs.getDouble("latitude"));
		    location.setLongitude(rs.getDouble("longitude"));

		    Super sup = new Super();
		    sup.setId(rs.getInt("superId"));
		    sup.setName(rs.getString("super_name"));
		    sup.setDescription(rs.getString("super_description"));
		    sup.setIsHero(rs.getBoolean("isHero"));
		    sup.setSuperpower(rs.getString("superpower"));
		    setExtraneousFields(sup);

		    Sighting sighting = new Sighting();
		    sighting.setSightedSuper(sup);
		    sighting.setSightingLocation(location);
		    sighting.setSightingDate(date);
		    return sighting;
		},
		Date.valueOf(date)
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedList = new LinkedList<>();
	}
	return receivedList;
    }

    private void setExtraneousFields(Super subject) {
	occupySightingLocationsOfSuper(subject);
	occupyOrganizationsOfSuper(subject);
    }

    private void occupySightingLocationsOfSuper(Super subject) {
	Map<LocalDate, Location> sightings = new HashMap<>();
	try {
	    jdbc.query(
		"SELECT L.*, SI.dateOfSighting "
		+ "FROM locations L "
		+ "INNER JOIN sightings SI on L.locationId = SI.locationId "
		+ "INNER JOIN supers SU ON SI.superId = SU.superId "
		+ "WHERE SU.superId = ?",
		(ResultSet rs, int index) -> {
		    LocalDate date = rs.getDate("dateOfSighting").toLocalDate();
		    Location location = new Location();
		    location.setId(rs.getInt("locationId"));
		    location.setName(rs.getString("name"));
		    location.setDescription(rs.getString("description"));
		    location.setAddress(rs.getString("address"));
		    location.setLatitude(rs.getDouble("latitude"));
		    location.setLongitude(rs.getDouble("longitude"));

		    return sightings.put(date, location);
		},
		subject.getId()
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	}
	subject.setSightings(sightings);
    }

    private void occupyOrganizationsOfSuper(Super subject) {
    	try {
	    subject.setOrganizations(jdbc.query(
		"SELECT O.*"
		+ "FROM organizations O "
		+ "INNER JOIN organizationHasMembers OS ON O.organizationId = OS.organizationId "
		+ "INNER JOIN supers S ON OS.superId = S.superId "
		+ "WHERE S.superId = ?",
		(ResultSet rs, int index) -> {
		    Organization org = new Organization();
		    org.setId(rs.getInt("organizationId"));
		    org.setName(rs.getString("name"));
		    org.setDescription(rs.getString("description"));
		    org.setAddress(rs.getString("address"));
		    org.setContact(rs.getString("contact"));
		    return org;
		},
		subject.getId()
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    subject.setOrganizations(new LinkedList<>());
	}
    }
}
