package com.example.PetShop.controller;

import com.example.PetShop.model.ErrorMessage;
import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.service.OwnerService;
import com.example.PetShop.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @Autowired
    PetService petService;

    @GetMapping("/owner/findAll")
    public Iterable<Owner> findAll(){
        return ownerService.getAllOwners();
    }

    @PostMapping("/owner/add")
    public Owner create(@RequestBody(required=false) Owner owner) throws ValidationException {
   
    	ownerService.create(owner);
        return owner;
    	
    }

    @GetMapping("/owner/findById/{id}")
    public Owner findById(@PathVariable("id") Integer id) throws ValidationException {
        return ownerService.findById(id);
    }

    @GetMapping("/owner/findPetByOwnerId/{id}")
    public List<Pet> getPetByOwnerId(@PathVariable("id") Integer id) throws ValidationException {
        return ownerService.findPetByOwnerId(id);
    }

    @PutMapping("/owner/update")
    public Owner update(@RequestBody(required=false) Owner owner) throws ValidationException {
        return ownerService.update(owner);
    }

    @GetMapping("/owner/search")
    public List<Owner> findByQuery(@RequestParam("first") String firstName,
                               @RequestParam("last") String lastName){
        return ownerService.findByFirstNameAndLastName(firstName,lastName);
    }


    @GetMapping("/owner/findByDateCreated")
    public List<Owner> findByDateCreated(
            @RequestParam("dateCreated")
            @DateTimeFormat(pattern = "yyyy-MM-dd") String dateCreated) throws ValidationException {

        return ownerService.findByDateCreated(dateCreated);
    }

    @GetMapping("/owner/findByPetName")
    public List<Owner> findOwnerByPetName(@RequestParam("name") String name) throws ValidationException {

        return ownerService.findOwnerByPetName(name);
    }

    @GetMapping("/owner/findByPetId/{id}")
    public Owner findByPetId(@PathVariable int id) throws ValidationException {

        return ownerService.findByPetId(id);
    }

    @PutMapping("/owner/updateRelation/{id}")
    public void updateRelation(@PathVariable int id, @RequestBody(required=false) Pet pet) throws Exception {
    	
    	if(pet == null)
    		throw new ValidationException("Pet is null");
    	
        ownerService.updateRelation(pet.getId(), id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorMessage exceptionHandler(ValidationException e){
        return new ErrorMessage("400",e.getMessage());
    }


}
