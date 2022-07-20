package com.example.PetShop.repository;

import com.example.PetShop.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer > {

    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);

    List<Owner> findByDateCreated(String dateCreated);

    @Query(value = "SELECT DISTINCT owners.* FROM owners INNER JOIN ownership ON owners.id = ownership.owner_id INNER JOIN pet ON ownership.pet_id = pet.id WHERE pet.name LIKE CONCAT('%', :name ,'%')", nativeQuery = true)
    List<Owner> findOwnerByName(@Param("name") String name);

    @Query(value = "SELECT DISTINCT owners.* FROM owners INNER JOIN ownership ON owners.id = ownership.owner_id INNER JOIN pet ON ownership.pet_id = pet.id WHERE pet.id = :id",nativeQuery = true)
    Owner findByPetId(@Param("id") int id);
}