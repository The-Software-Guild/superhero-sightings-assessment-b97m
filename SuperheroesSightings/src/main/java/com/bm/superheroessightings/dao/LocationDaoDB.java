package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * ... 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
@Repository
public class LocationDaoDB implements LocationDao {
    private JdbcTemplate jdbc;

    private static final RowMapper<Location> LOCATION_MAPPER = (ResultSet rs, int index) -> {
	Location loc = new Location();
	loc.setId(rs.getInt(            "locationId"));
	loc.setName(rs.getString(       "locationName"));
	loc.setDescription(rs.getString("locationDescription"));
	loc.setAddress(rs.getString(    "locationAddress"));
	loc.setLatitude(rs.getDouble(   "locationLatitude"));
	loc.setLongitude(rs.getDouble(  "locationLongitude"));
	return loc;
    };

    @Autowired
    public LocationDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Location> getLocations() {
    	List<Location> locs;
	try {
	    locs = jdbc.query(
		"SELECT * FROM locations",
		LOCATION_MAPPER
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    locs = new LinkedList<>();
	}
	return locs;
    }

    @Override
    public Optional<Location> getLocationById(int locationId) {
	Optional<Location> instance;
	try {
	    instance = Optional.of(jdbc.queryForObject(
		"SELECT * FROM locations WHERE locationId = ?",
		LOCATION_MAPPER,
		locationId
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    instance = Optional.empty();
	} 
	return instance;
    }

    @Override
    public Optional<Location> createLocation(
	String name, 
	String description,
	String address, 
	double latitude, 
	double longitude) {

	if (name == null || description == null || address == null) {
	    return Optional.empty();
	}

	int rowsUpdated;
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) -> {
		    var statement = conn.prepareStatement(
			"INSERT INTO locations ("
			+ "locationName, "
			+ "locationDescription, "
			+ "locationAddress, "
			+ "locationLatitude, "
			+ "locationLongitude"
			+ ") VALUES (?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setString(1, name);
		    statement.setString(2, description);
		    statement.setString(3, address);
		    statement.setDouble(4, latitude);
		    statement.setDouble(5, longitude);
		    return statement;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}

	if (rowsUpdated > 0) {
	    Location loc = new Location();
	    loc.setId(keyHolder.getKey().intValue());
	    loc.setName(name);
	    loc.setDescription(description);
	    loc.setAddress(address);
	    loc.setLatitude(latitude);
	    loc.setLongitude(longitude);
	    return Optional.of(loc);
	} 
	return Optional.empty();
    }

    @Override
    public boolean updateLocation(
	int locationId, 
	String name, 
	String description, 
	String address, 
	double latitude, 
	double longitude) {

	if (name == null || description == null || address == null) {
	    return false;
	}

	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"UPDATE locations SET "
		+ "locationName = ?, "
		+ "locationDescription = ?, "
		+ "locationAddress = ?, "
		+ "locationLatitude = ?, "
		+ "locationLongitude = ? "
		+ "WHERE locationId = ?",
		name,
		description,
		address,
		latitude,
		longitude,
		locationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    @Override
    @Transactional
    public boolean deleteLocation(int locationId) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE FROM sightings WHERE locationId = ?",
		locationId
	    );
	    rowsUpdated += jdbc.update(
		"DELETE FROM locations WHERE locationId = ?",
		locationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }
}
