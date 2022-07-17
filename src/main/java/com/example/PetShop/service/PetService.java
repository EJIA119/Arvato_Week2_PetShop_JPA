package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerService ownerService;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Pet create(Pet pet) throws ValidationException {

        Owner owner = ownerService.findById(pet.getOwner().getId());

        if (owner == null)
            throw new ValidationException("Owner record cannot be found.");
        else if (pet.getName() == null || pet.getBreed() == null)
            throw new ValidationException("Pet record cannot be empty.");

        pet.setOwner(owner);
        pet.setDate_created(dtf.format(LocalDate.now()));
        pet.setDate_modified(dtf.format(LocalDate.now()));

        petRepository.save(pet);
        return pet;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet update(Pet pet) throws ValidationException {

        Pet updatePet = petRepository.findById(pet.getId()).orElseThrow(() -> new ValidationException("Pet record not found with ID(" + pet.getId() + ")."));

        updatePet.setName(pet.getName());
        updatePet.setBreed(pet.getBreed());
        updatePet.setDate_modified(dtf.format(LocalDate.now()));

        Owner owner = ownerService.findById(pet.getOwner().getId());

        updatePet.setOwner(owner);

        return petRepository.save(updatePet);
    }

    public Pet deleteById(int id) throws ValidationException {
        Pet deletePet = petRepository.findById(id).orElseThrow(() -> new ValidationException("Pet record not found with ID(" + id + ")."));

        petRepository.deleteById(id);

        return deletePet;
    }

    public Pet findById(int id) throws ValidationException{
        return petRepository.findById(id).orElseThrow(() -> new ValidationException("Pet record not found with ID(" + id + ")."));
    }

    public List<Object> countPetByName() {
        return petRepository.countPetByName();
    }
}