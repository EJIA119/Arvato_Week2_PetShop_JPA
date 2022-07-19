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

        Owner newOwner = new Owner(1,"Jon","J");

        ownerService.create(newOwner);

        assertTrue(true);
    }


    @Test
    public void testCreateTest() throws ValidationException {
        Pet newPet = new Pet(1,"Kucly","Dog");

        petService.create(newPet);

        assertTrue(true);
    }

    @Test
    public void testAddOwnership() throws ValidationException {


        Pet pet = petService.findById(1);
        Owner owner = ownerService.findById(1);
        List<Pet> petList = owner.getPetList();
        if(petList == null){
            petList = new ArrayList<>();
        }

        petList.add(pet);
        ownerService.update(owner);


        assertTrue(true);
    }
}