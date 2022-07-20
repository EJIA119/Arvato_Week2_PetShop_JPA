package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OwnerServiceTest {

    @Autowired
    OwnerService ownerService;

    @Autowired
    PetService petService;

    @Test
    public void testCreateOwner() throws ValidationException {

        Owner newOwner = new Owner("Jackson","Wang");

        ownerService.create(newOwner);

        assertTrue(true);
    }


    @Test
    public void testAddOwnership() throws Exception {

        Pet pet = petService.findById(2);

        ownerService.updateRelation(pet.getId(), 1);

        assertTrue(true);
    }
    
    @Test
    public void testRemoveOwnership() throws Exception {
    	Pet pet = petService.findById(2);

        ownerService.removeRelation(pet.getId());

        assertTrue(true);
    }

    @Test
    public void testUpdateOwner() throws ValidationException {

        Owner updateOwner = ownerService.findById(1);

        updateOwner.setFirstName("Kimberly");
        updateOwner.setLastName("Kim");
        ownerService.update(updateOwner);

        assertTrue(true);
    }

}