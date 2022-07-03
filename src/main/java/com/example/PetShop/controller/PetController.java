package com.example.PetShop.controller;

import com.example.PetShop.errorHandler.ErrorMessage;
import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.service.OwnerService;
import com.example.PetShop.service.PetService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.naming.InterruptedNamingException;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.*;

import org.springframework.http.MediaType;

@RestController
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    OwnerService ownerService;

    @PostMapping("/pet")
    Pet create(@RequestBody Pet pet) throws ValidationException {

        if (pet.getName() == null || pet.getBreed() == null)
            throw new ValidationException("Pet record cannot be empty.");
        else if (ownerService.findById(pet.getOwner_id()).isEmpty())
            throw new ValidationException("Owner record cannot be found.");

        pet.setDate_created(LocalDate.now());
        pet.setDate_modified(LocalDate.now());
        pet.setName(StringUtils.capitalize(pet.getName()));
        pet.setBreed(StringUtils.capitalize(pet.getBreed()));

        return petService.save(pet);
    }

    @GetMapping("/pet")
    Iterable<Pet> read() {
        return petService.findAll();
    }

    @PutMapping("/pet")
    Pet update(@RequestBody Pet pet) throws ValidationException {

        if (pet != null && petService.findById(pet.getId()).isPresent()) {
            Pet updatePet = petService.findById(pet.getId()).get();
            updatePet.setName(StringUtils.capitalize(pet.getName()));
            updatePet.setBreed(StringUtils.capitalize(pet.getBreed()));
            pet.setDate_modified(LocalDate.now());

            return petService.save(updatePet);
        } else
            throw new ValidationException("Pet record not found with ID(" + pet.getId() + ").");
    }

    @DeleteMapping("/pet/{id}")
    String delete(@PathVariable Integer id) throws ValidationException {
        Pet tempPet;
        if (petService.findById(id).isPresent()) {
            tempPet = petService.findById(id).get();
            petService.deleteById(id);
        } else
            throw new ValidationException("Pet record not found with ID(" + id + ").");

        return "Delete Successfully: " + tempPet;
    }

    @GetMapping("/pet/{id}")
    Optional<Pet> findById(@PathVariable Integer id) {
        return petService.findById(id);
    }

    @GetMapping("/pet/search")
    List<Pet> findOwnerByName(@RequestParam("name") String name) throws ValidationException {

        if(petService.findByName(name).size() > 0)
            return petService.findByName(name);
        else
            throw new ValidationException("Pet name does not exist.");
    }

    @GetMapping("/pet/{id}/owner")
    Owner findOwnerById(@PathVariable Integer id) throws ValidationException {

        if (petService.findById(id).isPresent())
            return petService.findById(id).get().getOwner();
        else
            throw new ValidationException("Pet record not found with ID(" + id + ").");

    }

//    @GetMapping("/pet/{name}/getOwners")
//    List<Owner> findByName(@PathVariable String name) throws ValidationException {
//
//        return petService.findByName(name);
//    }

    @GetMapping("/pet/top")
    List<Object> findTopName() throws ValidationException {

        return petService.countPetByName();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    ErrorMessage exceptionHandler(ValidationException e) {
        return new ErrorMessage("400", e.getMessage());
    }
}
