/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Organization;
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

/**
 * ... 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
@Repository
public class OrganizationDaoDB implements OrganizationDao {
    private JdbcTemplate jdbc;

    private static final RowMapper<Organization> ORGANIZATION_MAPPER = (ResultSet rs, int index) -> {
	Organization org = new Organization();
	org.setId(rs.getInt("organizationId"));
	org.setName(rs.getString("organizationName"));
	org.setDescription(rs.getString("organizationDescription"));
	org.setAddress(rs.getString("organizationAddress"));
	org.setContact(rs.getString("organizationContact"));
	return org;
    };
    
    @Autowired
    public OrganizationDaoDB(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }

    @Override
    public List<Organization> getOrganizations() {
	List<Organization> orgs;
	try {
	    orgs = jdbc.query(
		"SELECT * FROM organizations",
		ORGANIZATION_MAPPER
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    orgs = new LinkedList<>();
	}
	return orgs;
    }

    @Override
    public Optional<Organization> getOrganizationById(int organizationId) {
	Optional<Organization> inst;
	try {
	    inst = Optional.of(jdbc.queryForObject(
		"SELECT * FROM organizations WHERE organizationId = ?",
		ORGANIZATION_MAPPER,
		organizationId
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    inst = Optional.empty();
	}
	return inst;
    }

    @Override
    public Optional<Organization> createOrganization(
	String name, 
	String description, 
	String address, 
	String contact) {

	int rowsUpdated;
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) ->{
		    var statement = conn.prepareStatement(
			"INSERT INTO organizations ("
			+ "organizationName, "
			+ "organizationDescription "
			+ "organizationAddress "
			+ "organizationContact"
			+ ") VALUES (?, ?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    statement.setString(1, name);
		    statement.setString(2, description);
		    statement.setString(3, address);
		    statement.setString(4, contact);
		    return statement;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	if (rowsUpdated > 0) {
	    Organization org = new Organization();
	    org.setId(keyHolder.getKey().intValue());
	    org.setName(name);
	    org.setDescription(description);
	    org.setAddress(address);
	    org.setContact(contact);
	    return Optional.of(org);
	}
	return Optional.empty();
    }

    @Override
    public boolean updateOrganization(
	int organizationId, 
	String name, 
	String description, 
	String address, 
	String contact) {

	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"UPDATE organizations SET "
		+ "organizationName = ?, "
		+ "organizationDescription = ?,"
		+ "organizationAddress = ?, "
		+ "organizationContact = ? "
		+ "WHERE organizationId = ?",
		name,
		description,
		address,
		contact,
		organizationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

    @Override
    public boolean deleteOrganization(int organizationId) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE organizations WHERE organizationId = ?",
		organizationId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }

}
