package com.bm.superheroessightings.dao;

import com.bm.superheroessightings.controller.dto.Organization;
import java.util.List;
import java.util.Optional;

/**
 * Handles the storage and retrieval of Organizations
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 24, 2021
 */
public interface OrganizationDao {

    /**
     * Retrieves a list of all Organizations
     * 
     * @return The above list
     */
    public List<Organization> getOrganizations();

    /**
     * Attempts to retrieve the Organization corresponding to this id
     * 
     * If such an organization exists, an instance containing it is returned.
     * Otherwise, an empty instance is returned.
     * 
     * @param organizationId
     * @return The above instance
     */
    public Optional<Organization> getOrganizationById(int organizationId);

    /**
     * Attempts to add an organization.
     * 
     * If the addition succeeds, an instance containing the created Organization
     * is returned. Otherwise, an empty instance is returned.
     * 
     * @param name
     * @param description
     * @param address
     * @param contact
     * @return The above instance
     */
    public Optional<Organization> createOrganization(
	String name,
	String description, 
	String address, 
	String contact
    );

    /**
     * Attempts to update the organization by providing the given information.
     * 
     * The value returned depends on the success of the update operation.
     * 
     * @param organizationId
     * @param name
     * @param description
     * @param address
     * @param contact
     * @return The above value
     */
    public boolean updateOrganization(
	int organizationId,
	String name,
	String description, 
	String address, 
	String contact
    );

    /**
     * Attempts to delete the organization
     * 
     * The value returned depends on the success of the deletion operation.
     * 
     * @param organizationId
     * @return The above value 
     */
    public boolean deleteOrganization(int organizationId);
}