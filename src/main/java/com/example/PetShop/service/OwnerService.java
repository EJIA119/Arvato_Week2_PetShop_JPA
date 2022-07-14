package com.example.PetShop.service;

import com.example.PetShop.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface OwnerService extends CrudRepository<Owner, Integer> {

    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);

    List<Owner> findByDateCreated(String dateCreated);

}
