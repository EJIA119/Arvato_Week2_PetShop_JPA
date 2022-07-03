package com.example.PetShop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;
    @NonNull
    private String breed;
    @NonNull
    private LocalDate date_created;
    @NonNull
    private LocalDate date_modified;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private Owner owner;

    private int owner_id;

    public Pet() {
    }

    public Pet(int id, String name, String breed, LocalDate date_created, LocalDate date_modified) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }



    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public LocalDate getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(LocalDate date_modified) {
        this.date_modified = date_modified;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", date_created=" + date_created +
                ", date_modified=" + date_modified +
                ", Owner=" + owner +
                '}';
    }
}
