package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetService extends CrudRepository<Pet, Integer> {

    List<Pet> findByName(String name);

    @Query(value = "SELECT p.name, COUNT(p.name) FROM pet p GROUP BY p.name",nativeQuery = true)
    List<Object> countPetByName();


}
