package com.example.PetShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.PetShop.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByName(String name);

    @Query(value = "SELECT p.name, COUNT(p.name) AS Counter FROM pet p GROUP BY p.name ORDER BY Counter DESC", nativeQuery = true)
    List<Object> countPetByName();
}
