package com.example.PetShop.model;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "ownership")
public class Ownership {

    @Id

    private int pet_id;


}
