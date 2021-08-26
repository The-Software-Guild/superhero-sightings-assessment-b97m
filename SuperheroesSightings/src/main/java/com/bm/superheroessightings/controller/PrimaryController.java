package com.bm.superheroessightings.controller;

import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.service.Service;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * The primary controller 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 23, 2021
 */
@Controller
@CrossOrigin()
@RequestMapping("superhero-sightings")
public class PrimaryController {

    private Service service;
    
    @Autowired
    public PrimaryController(Service service) {
	this.service = service;
    }

    /*
    -- HOME PAGE --
    */
    @GetMapping("home")
    public String homePage(Model mod) {
	mod.addAttribute("latestSightings", service.getLatestSightings());
	return "home.html";
    }

    /*
    -- SUPER PAGE
    */
    @GetMapping("super")
    public String superPage(Model mod) {
    	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("errors", new HashSet<>());
	return "super.html";
    }

    @PostMapping("super")
    public String superCreate(Model mod, String name, String description, boolean isHero) {
    	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("orgs", service.getOrganizations());

	Set<String> errors = new HashSet<>();
	service.addErrors(name, errors, "Name", 50);
	service.addErrorsAllowEmpty(description, errors, "Description", 500);

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    return "super"; 
	} else {
	    Optional<Super> possSuper = service.createSuper(name, description, isHero);
	    if (possSuper.isPresent()) {
		mod.addAttribute("errors", errors);
		return "redirect:/superhero-sightings/super";
	    } else {
		errors.add("Sorry, we were unable to create the new hero");
		mod.addAttribute("errors", errors);
		return "super";	
	    }
	}
    }

    /*
    -- SUPER DETAILS
    */
    @GetMapping("super/{superId}")
    public String superDetailPage(Model mod, @PathVariable int superId) {
    	String retrPage;
	var possSuper = service.getSuperById(superId);
	if (possSuper.isPresent()) {
	    mod.addAttribute("super", possSuper.get());
	    mod.addAttribute("orgs", service.getOrganizations());
	    mod.addAttribute("powers", service.getSuperpowers());
	    retrPage = "super-detail";
	} else {
	    retrPage = "redirect:/superhero-sightings/super";
	}

	return retrPage;
    }

    @PostMapping("super/{superId}/addOrg")
    public String addOrgForSuper(@PathVariable int superId, String organizationIdStr) {
	try {
	    int organizationId = Integer.parseInt(organizationIdStr);
	    service.addOrganizationForSuper(superId, organizationId); 
	} catch (NumberFormatException ex) {
	    System.out.println(ex.getMessage());
	}
    	return "redirect:/superhero-sightings/super/" + superId;	
    }

    @PostMapping("super/{superId}/addPower")
    public String addPowerForSuper(@PathVariable int superId, String powerIdStr) {
	try {
	    int superpowerId = Integer.parseInt(powerIdStr);
	    service.addSuperpowerForSuper(superId, superpowerId);
	} catch (NumberFormatException ex) {
	    System.out.println(ex.getMessage());
	}
    	return "redirect:/superhero-sightings/super/" + superId;	
    }

    @PostMapping("super/{superId}/removePower/{powerId}")
    public String removePowerForSuper(@PathVariable int superId, @PathVariable int powerId) {
	service.removeSuperpowerForSuper(superId, powerId);
	return "redirect:/superhero-sightings/super/" + superId;
    }

    @PostMapping("super/{superId}/removeOrganization/{organizationId}")
    public String removeOrganizationForSuper(@PathVariable int superId, @PathVariable int organizationId) {
	service.removeOrganizationForSuper(superId, organizationId);
	return "redirect:/superhero-sightings/super/" + superId;
    }
    /*
    -- SUPER EDITING
    */
    @GetMapping("super/{superId}/edit")
    public String getEditPage(Model mod, @PathVariable int superId) {
    	String retrPage;
	var possSuper = service.getSuperById(superId);
	if (possSuper.isPresent()) {
	    mod.addAttribute("super", possSuper.get());
	    mod.addAttribute("errors", new HashSet<>());
	    retrPage = "super-edit";
	} else {
	    retrPage = "redirect:/superhero-sightings/super";
	}
	return retrPage;
    }

    @PostMapping("super/{superId}/save")
    public String saveEdits(
	Model mod,
	@PathVariable int superId, 
	String superName, 
	String superDescription, 
	boolean superIsHero) {

    	String retrPage;
	var possSuper = service.getSuperById(superId);
	if (possSuper.isPresent()) {
	    mod.addAttribute("super", possSuper.get());

	    Set<String> errors = new HashSet<>();
	    service.addErrors(superName, errors, "Name", 50);
	    service.addErrorsAllowEmpty(superDescription, errors, "Description", 500);

	    if (!errors.isEmpty()) {
	    	mod.addAttribute("errors", errors);
		retrPage = "super-edit";
	    } else if (service.updateSuper(superId, superName, superDescription, superIsHero)) {
		retrPage = "redirect:/superhero-sightings/super/" + superId;
	    } else {
		errors.add("Sorry, we were unable to save the changes");
		mod.addAttribute("errors", errors);
		retrPage = "super-edit";
	    }
	} else {
	    retrPage = "redirect:/superhero-sightings/super";
	}
	return retrPage;
    }

    /*
    -- SUPER DELETION CONFIRMATION
    */
    @GetMapping("super/{superId}/delete-confirm")
    public String confirmDeletions(Model mod, @PathVariable int superId) {
	var possSuper = service.getSuperById(superId);
	if (possSuper.isEmpty()) {
	    return "redirect:/superhero-sightings/super";
	}

	mod.addAttribute("super", possSuper.get());
	return "super-delete";
    }

    @PostMapping("super/{superId}/delete")
    public String attemptDeletion(@PathVariable int superId) {
	service.deleteSuper(superId);
	return "redirect:/superhero-sightings/super";
    }


    /*
    -- SUPERPOWER PAGE
    */
    @GetMapping("superpower")
    public String getSuperpowers(Model mod) {
    	mod.addAttribute("powers", service.getSuperpowers());
	mod.addAttribute("errors", new HashSet<>());
	return "superpower";	
    }

    @PostMapping("superpower")
    public String addSuperpower(Model mod, String superpowerName) {
	mod.addAttribute("powers", service.getSuperpowers());

	Set<String> errors = new HashSet<>();
	service.addErrors(superpowerName, errors, "Name", 50);

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    return "superpower";
	} else if (service.addSuperpower(superpowerName).isEmpty()) {
	    errors.add("Sorry, we were unable to create the superpower");
	    mod.addAttribute("errors", errors);
	    return "superpower";
	} else {
	    return "redirect:/superhero-sightings/superpower";
	}
    }

    /*
    -- SUPERPOWER DETAILS
    */
    @GetMapping("superpower/{superpowerId}")
    public String getSuperpower(Model mod, @PathVariable int superpowerId) {
	var possPower = service.getSuperpowerById(superpowerId);
	if (possPower.isPresent()) {
	    mod.addAttribute("power", possPower.get());
	    return "superpower-detail";
	}
    	return "redirect:/superhero-sightings/superpower";
    }

    /*
    -- SUPERPOWER EDITING
    */
    @GetMapping("superpower/{superpowerId}/edit")
    public String superpowerEditPage(Model mod, @PathVariable int superpowerId) {
	var possPower = service.getSuperpowerById(superpowerId);
	Set<String> errors = new HashSet<>();
	if (possPower.isPresent()) {
	    mod.addAttribute("power", possPower.get());
	    mod.addAttribute("errors", errors);
	    return "superpower-edit";
	}
    	return "redirect:/superhero-sightings/superpower";
    }

    @PostMapping("superpower/{superpowerId}/save")
    public String saveSuperpower(Model mod, @PathVariable int superpowerId, String superpowerName) {
	String retrPage;
	var possPower = service.getSuperpowerById(superpowerId);
	if (possPower.isPresent()) {
	    mod.addAttribute("power", possPower.get());
	    Set<String> errors = new HashSet<>();
	    service.addErrors(superpowerName, errors, "Name", 50);

	    if (!errors.isEmpty()) {
		mod.addAttribute("errors", errors);
		retrPage = "superpower-edit";
	    } else if (service.updateSuperpower(superpowerId, superpowerName)) {
		retrPage = "redirect:/superhero-sightings/superpower/" + superpowerId;
	    } else {
		errors.add("Sorry, we could not save the changes to this superpower");
		mod.addAttribute("errors", errors);
		retrPage = "superpower-edit";
	    }
	} else {
	    retrPage = "redirect:/superhero-sightings/superpower";
	}

	return retrPage;
    }

    /*
    -- SUPERPOWER DELETION
    */
    @GetMapping("superpower/{superpowerId}/delete-confirm")
    public String confirmPowerDeletion(Model mod, @PathVariable int superpowerId) {
	var possPower = service.getSuperpowerById(superpowerId);
	if (possPower.isEmpty()) {
	    return "redirect:/superhero-sightings/superpower";
	}
	mod.addAttribute("power", possPower.get());
	return "superpower-delete";
    }

    @PostMapping("superpower/{superpowerId}/delete")
    public String performPowerDeletion(@PathVariable int superpowerId) {
	service.deleteSuperpower(superpowerId);
	return "redirect:/superhero-sightings/superpower";
    }

    /*
    -- ORGANIZATION PAGE
    */
    @GetMapping("organization")
    public String organizationInfo(Model mod) {
	mod.addAttribute("orgs", service.getOrganizations());
	mod.addAttribute("errors", new HashSet<String>());
    	return "organization";	
    }

    @PostMapping("organization")
    public String addOrg(
	Model mod, 
	String name, 
	String description, 
	String address, 
	String contact) {
    
	mod.addAttribute("orgs", service.getOrganizations());
	String retrPage;

	Set<String> errors = new HashSet<>();
	service.addErrors(name, errors, "Name", 50);
	service.addErrorsAllowEmpty(description, errors, "Description", 500);	
	service.addErrorsAllowEmpty(address, errors, "Address", 100);	
	service.addErrorsAllowEmpty(contact, errors, "Contact", 100);	

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    retrPage = "organization";
	} else {
	    var possOrg = service.addOrganization(
		name, 
		description, 
		address, 
		contact
	    );
	    if (possOrg.isPresent()) {
		retrPage = "redirect:/superhero-sightings/organization";
	    } else {
		errors.add("Sorry, we were unable to create the organization");
		mod.addAttribute("errors", errors);
		retrPage = "organization";
	    } 
	}
	return retrPage;
    }

    /*
    -- ORGANIZATION DETAIL
    */
    @GetMapping("organization/{organizationId}")
    public String orgDetail(Model mod, @PathVariable int organizationId) {
    	var possOrg = service.getOrganizationById(organizationId);	
	if (possOrg.isEmpty()) {
	    return "redirect:/superhero-sightings/organization";
	}
	mod.addAttribute("org", possOrg.get());
	return "organization-detail";
    }

    /*
    -- ORGANIZATION EDITING
    */
    @GetMapping("organization/{organizationId}/edit")
    public String orgEditing(Model mod, @PathVariable int organizationId) {
    	var possOrg = service.getOrganizationById(organizationId);	
	if (possOrg.isEmpty()) {
	    return "redirect:/superhero-sightings/organization";
	}
	mod.addAttribute("org", possOrg.get());
	mod.addAttribute("errors", new HashSet<>());
	return "organization-edit";
    }


    @PostMapping("organization/{organizationId}/save")
    public String orgSaving(
	Model mod,
	@PathVariable int organizationId,
	String name, 
	String description, 
	String address, 
	String contact) {

	var possOrg = service.getOrganizationById(organizationId);
	if (possOrg.isEmpty()) {
	    return "redirect:/superhero-sightings/organization";
	}
	mod.addAttribute("org", possOrg.get());

	String retrPage;
	Set<String> errors = new HashSet<>();
	service.addErrors(name, errors, "Name", 50);
	service.addErrorsAllowEmpty(description, errors, "Description", 500);
	service.addErrorsAllowEmpty(address, errors, "Address", 100);
	service.addErrorsAllowEmpty(contact, errors, "Contact", 100);

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    retrPage = "organization-edit";
	} else if (service.updateOrganization(organizationId, name, description, address, contact)) {
	    retrPage = "redirect:/superhero-sightings/organization/" + organizationId;
	} else {
	    errors.add("Sorry, we were unable to save the organization");
	    mod.addAttribute("errors", errors);
	    retrPage = "organization-edit";
	}
	return retrPage;
    }

    /*
    -- ORGANIZATION DELETION
    */
    @GetMapping("organization/{organizationId}/delete-confirm")
    public String orgDelConfirm(Model mod, @PathVariable int organizationId) {
	var possOrg = service.getOrganizationById(organizationId);
	if (possOrg.isEmpty()) {
	    return "redirect:/superhero-sightings/organization";
	}
	mod.addAttribute("org", possOrg.get());
	return "organization-delete";
    }

    @PostMapping("organization/{organizationId}/delete")
    public String orgDel(@PathVariable int organizationId) {
	service.deleteOrganization(organizationId);
	return "redirect:/superhero-sightings/organization";
    }

    /*
    -- LOCATION PAGE
    */
    @GetMapping("location")
    public String viewLocs(Model mod) {
    	mod.addAttribute("locations", service.getLocations());
	return "location";
    }

    @PostMapping("location")
    public String addLoc(
	Model mod, 
	String name, 
	String description, 
	String address, 
	String latitudeStr, 
	String longitudeStr) {

	mod.addAttribute("locations", service.getLocations());
	Set<String> errors = new HashSet<>();

	service.addErrors(name, errors, "Name", 50);
	service.addErrorsAllowEmpty(description, errors, "Description", 500);
	service.addErrorsAllowEmpty(address, errors, "Address", 100);
	service.addGeocordErrors(latitudeStr, errors, "Latitude");
	service.addGeocordErrors(longitudeStr, errors, "Longitude");

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    return "location";
	} else {
	    double latitude = Double.parseDouble(latitudeStr);
	    double longitude = Double.parseDouble(longitudeStr);
	    if (service.addLocation(name, description, address, latitude, longitude).isPresent()) {
	    	return "redirect:/superhero-sightings/location";
	    } else {
		errors.add("Sorry, we were unable to create the location");
		mod.addAttribute("errors", errors);
		return "location";
	    }
	}
    }

    /*
    -- LOCATION DETAIL
    */
    @GetMapping("location/{locationId}")
    public String showLoc(Model mod, @PathVariable int locationId) {
	var possLoc = service.getLocationById(locationId);
	if (possLoc.isEmpty()) {
	    return "redirect:/superhero-sightings/location";
	}
	mod.addAttribute("location", possLoc.get());
	return "location-detail";
    }

    /*
    -- LOCATION EDITING
    */
    @GetMapping("location/{locationId}/edit")
    public String editLoc(Model mod, @PathVariable int locationId) {
	var possLoc = service.getLocationById(locationId);
	if (possLoc.isEmpty()) {
	    return "redirect:/superhero-sightings/location";
	}
	mod.addAttribute("location", possLoc.get());
	return "location-edit";
    }
    
    @PostMapping("location/{locationId}/save")
    public String saveLoc(
	Model mod, 
	@PathVariable int locationId, 
	String name, 
	String description, 
	String address, 
	String latitudeStr, 
	String longitudeStr) {

	var possLoc = service.getLocationById(locationId);
	if (possLoc.isEmpty()) {
	    return "redirect:/superhero-sightings/location";
	}
	mod.addAttribute("location", possLoc.get());

	String retrPage;

	Set<String> errors = new HashSet<>();
	service.addErrors(name, errors, "Name", 50);
	service.addErrorsAllowEmpty(description, errors, "Description", 500);
	service.addErrorsAllowEmpty(address, errors, "Address", 100);
	service.addGeocordErrors(latitudeStr, errors, "Latitude");
	service.addGeocordErrors(longitudeStr, errors, "Longitude");

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    retrPage = "location-edit";
	} else {
	    double latitude = Double.parseDouble(latitudeStr); 
	    double longitude = Double.parseDouble(longitudeStr);
	    if (service.updateLocation(locationId, name, description, address, latitude, longitude)) {
		retrPage = "redirect:/superhero-sightings/location/" + locationId;
	    } else {
		errors.add("Sorry, we were unable to save the location");
		mod.addAttribute("errors", errors);
		retrPage = "location-edit";
	    }
	}
	return retrPage;
    }

    /*
    -- LOCATION DELETION
    */
    @GetMapping("location/{locationId}/delete-confirm")
    public String delConfLoc(Model mod, @PathVariable int locationId) {
	var possLoc = service.getLocationById(locationId);
	if (possLoc.isEmpty()) {
	    return "redirect:/superhero-sightings/location";
	}
	mod.addAttribute("location", possLoc.get());
    	return "location-delete";	
    }

    @PostMapping("location/{locationId}/delete")
    public String delLoc(@PathVariable int locationId) {
    	service.deleteLocation(locationId);
	return "redirect:/superhero-sightings/location";
    }

    /*
    -- SIGHTING PAGE
    */
    @GetMapping("sighting")
    public String sights(Model mod) {
	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("locations", service.getLocations());
	mod.addAttribute("sightings", service.getSightings());

	return "sighting";
    } 

    @PostMapping("sighting")
    public String makeSighting(Model mod, String superIdStr, String locationIdStr, String sightingDateStr) {
	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("locations", service.getLocations());
	mod.addAttribute("sightings", service.getSightings());
    
	String retrPage;

	Set<String> errors = new HashSet<>();
    	service.addIntegralErrors(superIdStr, errors, "Super");
	service.addIntegralErrors(locationIdStr, errors, "Location");
	var possDate = service.parsedDate(sightingDateStr);
	if (possDate.isEmpty()) {
	    errors.add("The date of sighting must be an actual date");
	}

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    retrPage = "sighting";
	} else {
	    int superId = Integer.parseInt(superIdStr);
	    int locationId = Integer.parseInt(locationIdStr);
	    LocalDate sightingDate = LocalDate.parse(sightingDateStr);
	    if (service.addSighting(superId, locationId, sightingDate).isPresent()) {
		retrPage = "redirect:/superhero-sightings/sighting"; 
	    } else {
		errors.add("Sorry, we were not able to save the sighting");
		mod.addAttribute("errors", errors);
		retrPage = "sighting";
	    }
	}
	return retrPage;
    }

    /*
    -- SIGHTING DETAIL
    */
    @GetMapping("sighting/{sightingId}")
    public String viewSighting(Model mod, @PathVariable int sightingId) {
    	var possSighting = service.getSightingById(sightingId);
	if (possSighting.isEmpty()) {
	    return "redirect:/superhero-sighting/sighting";
	}
	mod.addAttribute("sighting", possSighting.get());
	return "sighting-detail";
    }

    /*
    -- SIGHTING EDIT
    */
    @GetMapping("sighting/{sightingId}/edit")
    public String editSighting(Model mod, @PathVariable int sightingId) {
    	var possSighting = service.getSightingById(sightingId);
	if (possSighting.isEmpty()) {
	    return "redirect:/superhero-sighting/sighting";
	}
	mod.addAttribute("sighting", possSighting.get());
	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("locations", service.getLocations());

	return "sighting-edit";
    }

    @PostMapping("sighting/{sightingId}/save")
    public String saveSighting(
	Model mod, 
	@PathVariable int sightingId, 
	String superIdStr, 
	String locationIdStr, 
	String sightingDateStr) {

    	var possSighting = service.getSightingById(sightingId);
	if (possSighting.isEmpty()) {
	    return "redirect:/superhero-sighting/sighting";
	}
	mod.addAttribute("sighting", possSighting.get());
	mod.addAttribute("supers", service.getSupers());
	mod.addAttribute("locations", service.getLocations());

	String retrPage;
	Set<String> errors = new HashSet<>();
    	service.addIntegralErrors(superIdStr, errors, "Super");
	service.addIntegralErrors(locationIdStr, errors, "Location");
	var possDate = service.parsedDate(sightingDateStr);
	if (possDate.isEmpty()) {
	    errors.add("The date of sighting must be an actual date");
	}

	if (!errors.isEmpty()) {
	    mod.addAttribute("errors", errors);
	    retrPage = "sighting-edit";
	} else {
	    int superId = Integer.parseInt(superIdStr);
	    int locationId = Integer.parseInt(locationIdStr);
	    LocalDate sightingDate = LocalDate.parse(sightingDateStr);
	    if (service.updateSighting(sightingId, superId, locationId, sightingDate)) {
		retrPage = "redirect:/superhero-sightings/sighting/" + sightingId; 
	    } else {
		errors.add("Sorry, we were not able to save the sighting");
		mod.addAttribute("errors", errors);
		retrPage = "sighting-edit";
	    }
	}
	return retrPage;
    }

    /*
    -- SIGHTING DELETE
    */
    @GetMapping("sighting/{sightingId}/delete-confirm")
    public String deleteConfSighting(Model mod, @PathVariable int sightingId) {
    	var possSighting = service.getSightingById(sightingId);
	if (possSighting.isEmpty()) {
	    return "redirect:/superhero-sighting/sighting";
	}
	mod.addAttribute("sighting", possSighting.get());
	return "sighting-delete";
    }

    @PostMapping("sighting/{sightingId}/delete")
    public String deleteSighting(@PathVariable int sightingId) {
	service.deleteSighting(sightingId);
	return "redirect:/superhero-sightings/sighting";
    }
}