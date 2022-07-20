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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    OwnerService ownerService;

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/pet/add")
    public Pet create(@RequestBody(required=false) Pet pet) throws ValidationException {

        return petService.create(pet);
    }

    @GetMapping("/pet/findAll")
    Iterable<Pet> read() {
        return petService.getAllPets();
    }

    @PutMapping("/pet/update")
    public Pet update(@RequestBody(required=false) Pet pet) throws ValidationException {
        return petService.update(pet);
    }

    @DeleteMapping("/pet/delete/{id}")
    public void deleteById(@PathVariable int id) throws Exception {
        petService.deleteById(id);
    }

    @GetMapping("/pet/findById/{id}")
    public Pet findById(@PathVariable int id) throws ValidationException {
        return petService.findById(id);
    }

    @GetMapping("/pet/topName")
    public List<Object> findTopName() throws ValidationException {

        return petService.countPetByName();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorMessage exceptionHandler(ValidationException e) {
        return new ErrorMessage("400", e.getMessage());
    }
}
