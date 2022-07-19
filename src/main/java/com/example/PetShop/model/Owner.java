package com.example.PetShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.jfr.DataAmount;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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
    @Column(name = "date_created")
    private String dateCreated;
    @NonNull
    private String date_modified;


    @JoinTable(
            name="ownership",
            joinColumns = { @JoinColumn(name="owner_id", referencedColumnName = "id")},
            inverseJoinColumns = { @JoinColumn(name="pet_id",referencedColumnName = "id")}
    )
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Pet> petList;

    public Owner() {
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public Owner(int id, @NonNull String firstName, @NonNull String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Owner(int id, String firstName, String lastName, String dateCreated, String date_modified, List<Pet> petList) {
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
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
