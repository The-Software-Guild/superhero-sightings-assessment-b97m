package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Sighting;
import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.controller.dto.Superpower;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
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

    private static final RowMapper<Sighting> SIGHTING_MAPPER = (ResultSet rs, int index) -> {
	Sighting sighting = new Sighting();
	sighting.setId(rs.getInt("sightingId"));
	sighting.setSightingDate(rs.getDate("dateOfSighting").toLocalDate());
	return sighting;
    };
    
    @Autowired
    public SightingDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Sighting> getSightings() {
	List<Sighting> sightings;
	try {
	    sightings = jdbc.query(
		"SELECT * FROM sightings",
		SIGHTING_MAPPER
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    sightings = new LinkedList<>();
	}

	sightings.forEach(sighting -> occupyExtraneousSightingFields(sighting));
	return sightings;
    }

    @Override
    public Optional<Sighting> getSightingById(int sightingId) {
	Optional<Sighting> inst;
	try {
	    inst = Optional.of(jdbc.queryForObject(
		"SELECT * FROM sightings WHERE sightingId = ?",
		SIGHTING_MAPPER,
		sightingId
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    inst = Optional.empty();
	}
	return inst;
    }

    @Override
    public Optional<Sighting> createSighting(int superId, int locationId, LocalDate date) {
	int rowsUpdated;
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) ->{
		    var statement = conn.prepareStatement(
			"INSERT INTO sightings "
			+ "(superId, locationId, dateOfSighting) "
			+ "VALUES (?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setInt(1, superId);
		    statement.setInt(2, locationId);
		    statement.setDate(3, Date.valueOf(date));
		    return statement;
		},
	    	keyHolder 
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	if (rowsUpdated > 0) {
	    Sighting sighting = new Sighting();
	    sighting.setId(keyHolder.getKey().intValue());
	    sighting.setSightingDate(date);
	    occupyExtraneousSightingFields(sighting);
	    return Optional.of(sighting);
	}
	return Optional.empty();
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
	List<Sighting> sightings;
	try {
	    sightings = jdbc.query(
		"SELECT * FROM sightings WHERE dateOfSighting = ?",
		SIGHTING_MAPPER,
		Date.valueOf(date)
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    sightings = new LinkedList<>();
	}

	sightings.forEach(sighting -> occupyExtraneousSightingFields(sighting));
	return sightings;
    }

    @Override
    public boolean updateSighting(int sightingId, int superId, int locationId, LocalDate date) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"UPDATE sightings SET "
		+ "superId = ?, "
		+ "locationId = ?, "
		+ "dateOfSighting = ? "
		+ "WHERE sightingId = ?",
		superId,
		locationId,
		Date.valueOf(date),
		sightingId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    @Override
    public boolean deleteSighting(int sightingId) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE sightings WHERE sightingId = ?", 
		sightingId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    private void occupyExtraneousSightingFields(Sighting subject) {
	occupySightingLocation(subject);
	occupySightedSuper(subject);
    }
    
    private void occupySightingLocation(Sighting subject) {
	try {
	    Location loc = jdbc.queryForObject(
		"SELECT A.* FROM "
		+ "locations A INNER JOIN sightings B ON A.locationId = B.locationId "
		+ "WHERE B.sightingId = ?", 
		(ResultSet rs, int index) -> {
		    Location derivedLoc = new Location();
		    derivedLoc.setId(rs.getInt("locationId"));
		    derivedLoc.setName(rs.getString("locationName"));
		    derivedLoc.setDescription(rs.getString("locationDescription"));
		    derivedLoc.setAddress(rs.getString("locationAddress"));
		    derivedLoc.setLatitude(rs.getDouble("locationLatitude"));
		    derivedLoc.setLongitude(rs.getDouble("locationLongitude"));
		    return derivedLoc;
		},
		subject.getId()
	    );
	    subject.setSightingLocation(loc);
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	}
    }

    private void occupySightedSuper(Sighting subject) {
    	try {
	    Super sightedSuper = jdbc.queryForObject(
		"SELECT A.* FROM "
		+ "supers A INNER JOIN sightings B ON A.superId = B.superId "
		+ "WHERE B.sightingId = ?",
		(ResultSet rs, int index) -> {
		    Super sup = new Super();
		    sup.setId(rs.getInt("superId"));
		    sup.setName(rs.getString("superName"));
		    sup.setDescription(rs.getString("superDescription"));
		    sup.setIsHero(rs.getBoolean("superIsHero"));
		    return sup;
		},
		subject.getId()
	    );
	    setExtraneousSuperFields(sightedSuper);
	    subject.setSightedSuper(sightedSuper);
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	}
    }

    private void setExtraneousSuperFields(Super subject) {
	occupySightingLocationsOfSuper(subject);
	occupyOrganizationsOfSuper(subject);
	occupySuperpowersOfSuper(subject);
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
		    location.setName(rs.getString("locationName"));
		    location.setDescription(rs.getString("locationDescription"));
		    location.setAddress(rs.getString("locationAddress"));
		    location.setLatitude(rs.getDouble("locationLatitude"));
		    location.setLongitude(rs.getDouble("locationLongitude"));

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
		+ "INNER JOIN organizationsHaveMembers OS ON O.organizationId = OS.organizationId "
		+ "WHERE OS.superId = ?",
		(ResultSet rs, int index) -> {
		    Organization org = new Organization();
		    org.setId(rs.getInt("organizationId"));
		    org.setName(rs.getString("organizationName"));
		    org.setDescription(rs.getString("organizationDescription"));
		    org.setAddress(rs.getString("organizationAddress"));
		    org.setContact(rs.getString("organizationContact"));
		    return org;
		},
		subject.getId()
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    subject.setOrganizations(new LinkedList<>());
	}
    }

    private void occupySuperpowersOfSuper(Super subject) {
	List<Superpower> superpowers;
	try {
	    superpowers = jdbc.query(
		"SELECT A.* "
		+ "FROM superpowers A INNER JOIN supersHaveSuperpowers B ON a.superpowerId = B.superpowerId "
		+ "WHERE B.superId = ?",
		(ResultSet rs, int index) -> {
		    Superpower power = new Superpower();
		    power.setId(rs.getInt("superpowerId"));
		    power.setName(rs.getString("superpowerName"));
		    return power;
		},
		subject.getId()
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    superpowers = new LinkedList<>();
	}
	subject.setSuperpowers(superpowers);
    }
}