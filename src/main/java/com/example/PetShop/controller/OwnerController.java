package com.example.PetShop.controller;

import com.example.PetShop.errorHandler.ErrorMessage;
import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.util.StringUtils;

@RestController
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @PostMapping("/owner")
    Owner owner(@RequestBody Owner owner) throws ValidationException {

        // Check if the owner is filled up with necessary information
        if(owner.getFirstName() == null || owner.getLastName() == null)
            throw new ValidationException("First name and last name cannot be null");

        // Check whether the owner is existing by finding the first name & last name
        // if existing, then throw exception
        List<Owner> owners = ownerService.findByFirstNameAndLastName(owner.getFirstName(),owner.getLastName());
        if(owners.size() > 0)
            throw new ValidationException("Owner record is existing in database. Owner cannot be created.");

        // Continue to add
        owner.setDateCreated(LocalDate.now());
        owner.setDate_modified(LocalDate.now());
        owner.setFirstName(StringUtils.capitalize(owner.getFirstName()));
        owner.setLastName(StringUtils.capitalize(owner.getLastName()));

        // Set the pet list attribute value
        if(owner.getPetList() != null){
            // Get the owner ID
            int owner_id = 1;
            List<Owner> ownerList = (List<Owner>) ownerService.findAll();
            if(!ownerList.isEmpty())
                owner_id = ownerList.get(ownerList.size() - 1).getId() + 1;

            // Set the value
            int finalOwner_id = owner_id;
            for(Pet p : owner.getPetList()){
                p.setOwner_id(finalOwner_id);
                p.setDate_created(LocalDate.now());
                p.setDate_modified(LocalDate.now());
                p.setName(StringUtils.capitalize(p.getName()));
                p.setBreed(StringUtils.capitalize(p.getBreed()));
            }
        }

        return ownerService.save(owner);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    ErrorMessage exceptionHandler(ValidationException e){
        return new ErrorMessage("400",e.getMessage());
    }

    @GetMapping("/owner")
    Iterable<Owner> findAll(){
        return ownerService.findAll();
    }

    @GetMapping("/owner/{id}")
    Optional<Owner> findById(@PathVariable("id") Integer id){
        return ownerService.findById(id);
    }

    @GetMapping("/owner/{id}/pets")
    List<Pet> getPetByOwnerId(@PathVariable("id") Integer id){
        Optional<Owner> owner = ownerService.findById(id);
        if(owner.isPresent())
            return owner.get().getPetList();
        else
            return null;
    }

    @PutMapping("/owner")
    Owner update(@RequestBody Owner owner){
        return ownerService.save(owner);
    }

    @GetMapping("/owner/search")
    List<Owner> findByQuery(@RequestParam("first") String firstName,
                               @RequestParam("last") String lastName){
        return ownerService.findByFirstNameAndLastName(firstName,lastName);
    }

    @GetMapping("/owner/{id}/getPets")
    List<Pet> findPetsById(@PathVariable Integer id) throws ValidationException {

        if(ownerService.findById(id).isPresent())
            return ownerService.findById(id).get().getPetList();
        else
            throw new ValidationException("Owner record not found with ID(" + id + ").");

    }

    @GetMapping("/owner/searchByDate")
    List<Owner> findByDateCreated(
            @RequestParam("dateCreated")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateCreated) throws ValidationException {

        List<Owner> ownerList = ownerService.findByDateCreated(dateCreated);

        if(ownerList.size() > 0)
            return ownerList;
        else
            throw new ValidationException("Owner with date created(" + dateCreated + ") not found.");

    }

}
