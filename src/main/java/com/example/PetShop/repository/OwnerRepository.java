package com.example.PetShop.repository;

import com.example.PetShop.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);

    List<Owner> findByDateCreated(String dateCreated);

    @Query(value = "SELECT DISTINCT owners.* FROM owners INNER JOIN pet ON owners.id = pet.owner_id WHERE name LIKE CONCAT('%',:name ,'%')", nativeQuery = true)
    List<Owner> findOwnerByName(@Param("name") String name);

    @Query(value = "SELECT DISTINCT owners.* FROM owners INNER JOIN pet ON owners.id = pet.owner_id WHERE pet.id = :id",nativeQuery = true)
    Owner findByPetId(@Param("id") int id);
}