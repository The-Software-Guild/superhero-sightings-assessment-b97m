package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.controller.dto.Superpower;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private static final RowMapper<Super> SUPER_MAPPER = (ResultSet rs, int index) ->{
	Super sup = new Super();
	sup.setId(rs.getInt("superId"));
	sup.setName(rs.getString("superName"));
	sup.setDescription(rs.getString("superDescription"));
	sup.setIsHero(rs.getBoolean("superIsHero"));
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
		+ "FROM supers A INNER JOIN organizationsHaveMembers O ON A.superId = O.superId "
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

    @Override
    public List<Super> getSupers() {
	List<Super> receivedList;
	try {
	    receivedList = jdbc.query(
		"SELECT * FROM supers", 
		SUPER_MAPPER
	    );
	} catch (DataAccessException ex){
	    System.out.println(ex.getMessage());
	    receivedList = new LinkedList<>();
	}

	receivedList.forEach(sup -> setExtraneousFields(sup));
	return receivedList;

    }

    @Override
    public Optional<Super> createSuper(String name, String description, boolean isHero) {
	int rowsUpdated;
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) ->{
		    PreparedStatement statement = conn.prepareStatement(
			"INSERT INTO supers (superName, superDescription, superIsHero) "
			+ "VALUES (?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setString(1, name);
		    statement.setString(2, description);
		    statement.setBoolean(3, isHero);

		    return statement;
		},
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}

	if (rowsUpdated > 0) {
	    Super newSuper = new Super();
	    newSuper.setId(keyHolder.getKey().intValue());
	    newSuper.setName(name);
	    newSuper.setIsHero(isHero);
	    setExtraneousFields(newSuper);
	    return Optional.of(newSuper);
	}
	return Optional.empty();
    }

    @Override
    public boolean updateSuper(int superId, String name, String description, boolean isHero) {
    	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"UPDATE supers "
		+ "SET superName = ?, superDescription = ?, superIsHero = ? "
		+ "WHERE superId = ?",
		name,
		description,
		isHero,
		superId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    @Override
    public boolean deleteSuper(int superId) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE supers WHERE superId = ?",
		superId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    private void setExtraneousFields(Super subject) {
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