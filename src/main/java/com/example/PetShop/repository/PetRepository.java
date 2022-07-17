package com.example.PetShop.repository;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByName(String name);

    @Query(value = "SELECT p.name, COUNT(p.name) AS Counter FROM pet p GROUP BY p.name ORDER BY Counter DESC", nativeQuery = true)
    List<Object> countPetByName();
}
