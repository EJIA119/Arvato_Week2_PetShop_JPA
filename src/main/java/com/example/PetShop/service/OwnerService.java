package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.repository.OwnerRepository;
import com.example.PetShop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository;
    
    @Lazy
    @Autowired
    PetService petService;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Owner create(Owner newOwner) throws ValidationException {
        // Check if the owner is filled up with necessary information
    	if(newOwner == null)
    		throw new ValidationException("Owner information is null.");
    	
        if (newOwner.getFirstName() == null || newOwner.getLastName() == null)
            throw new ValidationException("First name and last name cannot be null");

        // Check whether the owner is existing by finding the first name & last name
        // if existing, then throw exception
        List<Owner> owners = ownerRepository.findByFirstNameAndLastName(newOwner.getFirstName(), newOwner.getLastName());
        if (owners.size() > 0)
            throw new ValidationException("Owner record is existing in database. Owner cannot be created.");

        // Continue to add
        newOwner.setDateCreated(dtf.format(LocalDate.now()));
        newOwner.setDate_modified(dtf.format(LocalDate.now()));

        // Set the pet list attribute value
        if (newOwner.getPetList() != null) {
            for (Pet p : newOwner.getPetList()) {
            	
            	if(p.getName() == null || p.getBreed() == null)
            		throw new ValidationException("Pet information cannot be null.");
            	
                p.setDate_created(dtf.format(LocalDate.now()));
                p.setDate_modified(dtf.format(LocalDate.now()));
            }
        }

        ownerRepository.save(newOwner);
        return newOwner;
    }

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    public Owner findById(int id) throws ValidationException {
        return ownerRepository.findById(id).orElseThrow(() -> new ValidationException("Owner with ID (" + id + ") not found."));
    }

    public List<Pet> findPetByOwnerId(int id) throws ValidationException {
        Optional<Owner> owner = ownerRepository.findById(id);
        return owner.map(Owner::getPetList).orElseThrow(() -> new ValidationException("Pet with Owner ID (" + id + ") not found."));
    }


    // Extra function as the requirements didn't mention for this
    public Owner update(Owner owner) throws ValidationException {

    	if(owner == null)
    		throw new ValidationException("Owner information is null.");
    	
        Owner updateOwner = ownerRepository.findById(owner.getId()).orElseThrow(() -> new ValidationException("Owner with ID (" + owner.getId() + ") not found."));

        updateOwner.setFirstName(owner.getFirstName());
        updateOwner.setLastName(owner.getLastName());
        updateOwner.setDate_modified(dtf.format(LocalDate.now()));

        updateOwner.setPetList(owner.getPetList());

        ownerRepository.save(updateOwner);

        return updateOwner;
    }

    public void updateRelation(int pet_id, int owner_id) throws Exception{
    	
    	try {
    		removeRelation(pet_id);
    		
    		Pet pet = petService.findById(pet_id);
    		
    		Owner owner = ownerRepository.findById(owner_id).orElseThrow(() -> new ValidationException("Owner ID not found."));
    		
    		List<Pet> petList = owner.getPetList();
    		
    		if(petList == null)
    			petList = new ArrayList<>();
    		
    		if(!petList.contains(pet))
    			petList.add(pet);
    		
    		owner.setPetList(petList);
    		
    		ownerRepository.save(owner);
    		
    	}catch(Exception e) {
    		throw new ValidationException("Update Relation: " + e.getMessage());
    	}
    	
        
    }
    
    public void removeRelation(int pet_id) throws Exception {
    	
    	try {
			Pet pet = petService.findById(pet_id);
    				
    		List<Owner> ownerList = ownerRepository.findAll();
    		
    		if(ownerList != null) {
    			ownerList.stream().forEach(o -> o.getPetList().removeIf(p -> p.getId() == pet.getId()));
    		
    			ownerRepository.saveAll(ownerList);
    		}

    	}catch(Exception e) {
    		throw new ValidationException("Remove Relation: " + e.getMessage());
    	}
    }

    public List<Owner> findByFirstNameAndLastName(String firstName, String lastName) {
        return ownerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Owner> findByDateCreated(String dateCreated) throws ValidationException {

        List<Owner> ownerList = ownerRepository.findByDateCreated(dateCreated);

        return ownerList;
    }

    public List<Owner> findOwnerByPetName(String name) throws ValidationException {

        List<Owner> ownerList = ownerRepository.findOwnerByName(name);

        return ownerList;
    }

    public Owner findByPetId(int id) throws ValidationException {

        Owner owner = ownerRepository.findByPetId(id);

        if (owner == null)
            throw new ValidationException("Owner with Pet ID (" + id + ") not found. ");

        return owner;
    }
}
