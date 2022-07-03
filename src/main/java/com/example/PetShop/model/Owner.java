package com.example.PetShop.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name="first_name")
    private String firstName;
    @NonNull
    @Column(name="last_name")
    private String lastName;
    @NonNull
    @Column(name="date_created")
    private LocalDate dateCreated;
    @NonNull
    private LocalDate date_modified;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> petList;

    public Owner() {
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public Owner(int id, String firstName, String lastName, LocalDate dateCreated, LocalDate date_modified, List<Pet> petList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateCreated = dateCreated;
        this.date_modified = date_modified;
        this.petList = petList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(LocalDate date_modified) {
        this.date_modified = date_modified;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
