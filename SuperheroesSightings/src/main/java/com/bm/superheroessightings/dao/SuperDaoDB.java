package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Super;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * An implementation of the SuperDao interface
 * that interacts with a MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
@Repository
public class SuperDaoDB implements SuperDao {
    private JdbcTemplate jdbc;

    private RowMapper<Super> SUPER_MAPPER = (ResultSet rs, int index) ->{
	Super sup = new Super();
	sup.setId(rs.getInt("superId"));
	sup.setName(rs.getString("name"));
	sup.setDescription(rs.getString("description"));
	sup.setIsHero(rs.getBoolean("isHero"));
	sup.setSuperpower(rs.getString("superpower"));
	return sup;
    };

    @Autowired
    public SuperDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Super> getSupersBySightingLocationId(int locationId) {
	List<Super> receivedList;
	try {
	    receivedList = jdbc.query(
		"SELECT A.* "
		+ "FROM supers A INNER JOIN sightings B ON A.superId = B.superId "
		+ "WHERE B.locationId = ?",
		SUPER_MAPPER,
		locationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedList = new LinkedList<>();
	}

	receivedList.forEach(sup -> setExtraneousFields(sup));
	return receivedList;
    }

    @Override
    public List<Super> getSupersByOrganizationId(int organizationId) {
	List<Super> receivedList;
	try {
	    receivedList = jdbc.query(
		"SELECT A.* "
		+ "FROM supers A INNER JOIN organizationHasMembers O ON A.superId = O.superId "
		+ "WHERE O.organizationId = ?",
		SUPER_MAPPER,
		organizationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedList = new LinkedList<>();
	}

	receivedList.forEach(sup -> setExtraneousFields(sup));
	return receivedList;
    }

    @Override
    public Optional<Super> getSuperById(int superId) {
	Optional<Super> receivedInstance;
	try {
	    Super sup = jdbc.queryForObject(
		"SELECT * FROM supers WHERE superId = ?", 
		SUPER_MAPPER, 
		superId
	    );
	    setExtraneousFields(sup);
	    receivedInstance = Optional.of(sup);
	} catch (DataAccessException ex){
	    System.out.println(ex.getMessage());
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
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
	    subject.setOrganizations(new LinkedList<>());
	}
    }
}