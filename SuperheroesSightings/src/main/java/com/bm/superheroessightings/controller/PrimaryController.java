package com.bm.superheroessightings.controller;

import com.bm.superheroessightings.controller.dto.Super;
import com.bm.superheroessightings.service.Service;
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
	if (name == null) {
	    errors.add("The name of the superhero/villain should not be null");
	} else if (name.length() <= 0) {
	    errors.add("The name of the superhero/villain must not be empty");
	} else if (name.length() > 50) {
	    String err = "For now, our database can only handle 50 characters "
		+ "per superhero name. Please keep the names no more than 50 "
		+ "characters long";
	    errors.add(err); 
	}

	if (description == null) {
	    errors.add("The description of the superhero can be empty, but never null");
	} else if (description.length() > 500) {
	    String err = "For now, our database can only handle 500 characters "
		+ "per superhero description. Please keep the descriptions no more than 500 "
		+ "characters long";
	    errors.add(err); 
	}
	

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
	    if (superName == null) {
		errors.add("The name of the superhero/villain cannot be null");
	    } else if (superName.length() <= 0) {
		errors.add("The name of the superhero/villain must not be empty");
	    } else if (superName.length() > 50) {
		errors.add(
		    "Currently, our database can only handle 50 characters for names. "
		    + "Please keep the names no more than 50 characters long." 
		);
	    }

	    if (superDescription == null) {
		errors.add("The description may be empty, but never null");
	    } else if (superDescription.length() > 500) {
		errors.add(
		    "Currently, our database can only handle 500 characters for "
		    + "descriptions. Please keep descriptions no more than 500 "
		    + "characters long."
		);
	    }

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
    	if (superpowerName == null) {
	    errors.add("Superpower names must not be null");
	} else if (superpowerName.length() <= 0) {
	    errors.add("Superpower names cannot be empty");
	} else if (superpowerName.length() > 50) {
	    errors.add(
		"For now, our database can only handle 50 characters per "
		+ "superpower name. Please keep such names no more than 50 "
		+ "characters long"
	    );
	}

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
	    if (superpowerName == null) {
		errors.add("Superpower names must not be null");
	    } else if (superpowerName.length() <= 0) {
		errors.add("Superpower names cannot be empty");
	    } else if (superpowerName.length() > 50) {
		errors.add(
		    "For now, our database can only handle 50 characters per "
		    + "superpower name. Please keep such names no more than 50 "
		    + "characters long"
		);
	    }
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
}