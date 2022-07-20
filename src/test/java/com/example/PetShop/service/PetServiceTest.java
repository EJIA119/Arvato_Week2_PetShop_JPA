package com.example.PetShop.service;


import com.example.PetShop.model.Pet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetServiceTest {

    @Autowired
    PetService petService;

    @Test
    public void testCreatePet() throws ValidationException {
        Pet newPet = new Pet("Maggi","Dog");

        petService.create(newPet);

        assertTrue(true);
    }

    @Test
    public void testUpdate() throws ValidationException {
        Pet pet = petService.findById(3);

        pet.setName("Vicky");
        pet.setBreed("Dog");

        petService.update(pet);

        assertTrue(true);
    }
    
    @Test
    public void testDelete() throws Exception {
    	Pet pet = petService.findById(3);
    	
    	petService.deleteById(pet.getId());
    	
    	assertTrue(true);
    }

}
