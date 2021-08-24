package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Superpower;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * An implementation of the SuperpowerDao interface that interacts with a 
 * MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
@Repository
public class SuperpowerDaoDB implements SuperpowerDao {
    private JdbcTemplate jdbc;

    private static final RowMapper<Superpower> SUPERPOWER_MAPPER = (ResultSet rs, int index) -> {
    	Superpower power = new Superpower();
	power.setId(rs.getInt("superpowerId"));
	power.setName(rs.getString("superpowerName"));
	return power;
    };

    @Autowired
    public SuperpowerDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Superpower> getSuperpowers() {
    	List<Superpower> powers;
	try {
	    powers = jdbc.query(
		"SELECT * FROM superpowers",
		SUPERPOWER_MAPPER
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    powers = new LinkedList<>();
	}
	return powers;
    }

    @Override
    public Optional<Superpower> getSuperpowerById(int superpowerId) {
    	Optional<Superpower> instance;
	try {
	    instance = Optional.of(
		jdbc.queryForObject(
		    "SELECT * FROM superpowers WHERE superpowerId = ?",
		    SUPERPOWER_MAPPER,
		    superpowerId
		)
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    instance = Optional.empty();
	}
	return instance;
    }

    @Override
    public Optional<Superpower> createSuperpower(String name) {
	if (name == null) {
	    return Optional.empty();
	}

    	Optional<Superpower> instance;
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) -> {
		    PreparedStatement statement = conn.prepareStatement(
			"INSERT INTO superpowers (superpowerName) VALUES (?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setString(1, name);
		    return statement;
		}, keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}

	if (rowsUpdated > 0) {
	    Superpower power = new Superpower();
	    power.setId(keyHolder.getKey().intValue());
	    power.setName(name);
	    return Optional.of(power);
	}
	return Optional.empty();
    }

    @Override
    public boolean updateSuperpower(int superpowerId, String name) {
	if (name == null) {
	    return false;
	}	    

	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"UPDATE superpowers SET superpowerName = ? WHERE superpowerId = ?",
		name,
		superpowerId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    @Override
    @Transactional
    public boolean deleteSuperpower(int superpowerId) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE supersHaveSuperpowers WHERE superpowerId = ?",
		superpowerId
	    );
	    rowsUpdated += jdbc.update(
		"DELETE superpowers WHERE superpowerId = ?",
		superpowerId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }
}
