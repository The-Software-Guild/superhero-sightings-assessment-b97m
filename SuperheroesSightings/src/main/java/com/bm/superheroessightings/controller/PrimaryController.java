package com.bm.superheroessightings.controller;

import com.bm.superheroessightings.controller.dto.Location;
import com.bm.superheroessightings.controller.dto.Organization;
import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.controller.dto.SightingEntry;
import com.bm.superheroessightings.service.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


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
	return "super.html";
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
	    retrPage = "super-detail";
	} else {
	    retrPage = "super";
	}
	return retrPage;
    }
    /*
    @PostMapping("sighting")
    public ResponseEntity addSighting(@RequestBody SightingEntry entry) {
	Optional<LocalDate> possDate = service.parsedDate(entry.getDateStr());
	if (possDate.isEmpty()) {
	    return new ResponseEntity(
		"Could not parse entered date", 
		HttpStatus.BAD_REQUEST
	    );
	}

	var possSighting = service.addSighting(
	    entry.getSuperId(), 
	    entry.getLocationId(), 
	    possDate.get()
	);

	if (possSighting.isPresent()) {
	    return new ResponseEntity(possSighting.get(), HttpStatus.OK);
	}

	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("super/sighting/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Super> heroesBySightingLocation(@PathVariable int locationId) {
	return service.getSupersBySightingLocationId(locationId);
    }

    @GetMapping("location/super/{superId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Location> locationsSuperWasSeen(@PathVariable int superId) {
	return service.getSightingLocationsForSuper(superId);
    }

    @GetMapping("sighting/date/{sightingDate}")
    public ResponseEntity sightingsByDate(@PathVariable String sightingDate) {
	Optional<LocalDate> possDate = service.parsedDate(sightingDate);
	if (possDate.isEmpty()) {
	    return new ResponseEntity("Date could not be parsed", HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity(
	    service.getSightingsByDate(possDate.get()),
	    HttpStatus.OK
	);
    }

    @GetMapping("super/organization/{organizationId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Super> heroesByOrganization(@PathVariable int organizationId) {
	return service.getSupersByOrganizationId(organizationId);
    }

    @GetMapping("organization/super/{superId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Organization> organizationsOfHero(@PathVariable int superId) {
	return service.getOrganizationsOfSuper(superId);
    }
    */
}