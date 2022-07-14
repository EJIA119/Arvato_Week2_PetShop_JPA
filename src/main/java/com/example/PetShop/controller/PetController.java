package com.example.PetShop.controller;




import com.example.PetShop.model.ErrorMessage;
import com.example.PetShop.model.Pet;
import com.example.PetShop.model.Owner;
import com.example.PetShop.service.OwnerService;
import com.example.PetShop.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    OwnerService ownerService;

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/pet")
    public Pet create(@RequestBody Pet pet) throws ValidationException {

        if (pet.getName() == null || pet.getBreed() == null)
            throw new ValidationException("Pet record cannot be empty.");
        else if(ownerService.findById(pet.getOwner_id()).isPresent()){
            throw new ValidationException("Owner record cannot be found.");
        }

        pet.setDate_created(dtf.format(LocalDate.now()));
        pet.setDate_modified(dtf.format(LocalDate.now()));
        pet.setName(StringUtils.capitalize(pet.getName()));
        pet.setBreed(StringUtils.capitalize(pet.getBreed()));

        petService.save(pet);
        return pet;
    }

    @GetMapping("/pet")
    Iterable<Pet> read() {
        return petService.findAll();
    }

    @PutMapping("/pet")
    public Pet update(@RequestBody Pet pet) throws ValidationException {

        if (pet != null && petService.findById(pet.getId()).isPresent()) {
            Pet updatePet = petService.findById(pet.getId()).get();
            updatePet.setName(StringUtils.capitalize(pet.getName()));
            updatePet.setBreed(StringUtils.capitalize(pet.getBreed()));
            pet.setDate_modified(dtf.format(LocalDate.now()));

            return petService.save(updatePet);
        } else
            throw new ValidationException("Pet record not found with ID(" + pet.getId() + ").");
    }

    @DeleteMapping("/pet/{id}")
    public String deleteById(@PathVariable Integer id) throws ValidationException {
        Pet tempPet;
        if (petService.findById(id).isPresent()) {
            tempPet = petService.findById(id).get();
            petService.deleteById(id);
        } else
            throw new ValidationException("Pet record not found with ID(" + id + ").");

        return "Delete Successfully: " + tempPet;
    }

    @GetMapping("/pet/{id}")
    public Optional<Pet> findById(@PathVariable Integer id) {
        return petService.findById(id);
    }

    @GetMapping("/pet/search")
    public List<Pet> findOwnerByName(@RequestParam("name") String name) throws ValidationException {

        if(petService.findByName(name).size() > 0)
            return petService.findByName(name);
        else
            throw new ValidationException("Pet name does not exist.");
    }

    @GetMapping("/pet/{id}/owner")
    public Owner findOwnerById(@PathVariable Integer id) throws ValidationException {

        if (petService.findById(id).isPresent())
            return petService.findById(id).get().getOwner();
        else
            throw new ValidationException("Pet record not found with ID(" + id + ").");

    }

    @GetMapping("/pet/top")
    public List<Object> findTopName() throws ValidationException {

        return petService.countPetByName();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorMessage exceptionHandler(ValidationException e) {
        return new ErrorMessage("400", e.getMessage());
    }
}
