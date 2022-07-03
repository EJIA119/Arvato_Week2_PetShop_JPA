package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OwnerService extends CrudRepository<Owner, Integer> {

//    @Query
//    Iterable<Pet> getPetByOwnerId(Integer id);

    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);

    List<Owner> findByDateCreated(LocalDate dateCreated);

}
