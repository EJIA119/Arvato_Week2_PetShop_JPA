package com.example.PetShop.controller;

import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.service.OwnerService;
import com.example.PetShop.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minidev.json.JSONUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OwnerController.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    OwnerController ownerController;

    @MockBean
    PetService petService;

    @MockBean
    OwnerService ownerService;

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String date = "2022-07-03";

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    private Owner owner1;
    private Owner owner2;

    private Pet pet1 = new Pet(1, "DouDou", "Labrador", date, date,owner1);
    private Pet pet2 = new Pet(2, "DouDou2", "Labrador", date, date, owner1);
    private Pet pet3 = new Pet(3, "DouDou3", "Labrador", date, date, owner2);
    private Pet pet4 = new Pet(4, "DouDou4", "Labrador", date, date, owner2);

    @Before
    public void initialize() throws ValidationException {
        List<Pet> petList1 = new ArrayList<>();
        petList1.add(pet1);
        petList1.add(pet2);

        owner1 = new Owner(1, "Jony", "J", date, date, petList1);

        List<Pet> petList2 = new ArrayList<>();
        petList2.add(pet3);
        petList2.add(pet4);

        owner2 = new Owner(2, "Krystal", "K", date, date, petList2);
    }

    @Test
    public void getAllOwners() throws Exception {

        List<Owner> ownerList = new ArrayList<>(Arrays.asList(owner1, owner2));

        when(ownerService.getAllOwners()).thenReturn(ownerList);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/owner")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", Matchers.is("Jony")))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("Krystal")));
    }

    @Test
    public void retrieveOwnerById() throws Exception {

        Mockito.when(ownerService.findById(1)).thenReturn(owner1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owner/findById/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expectedResult = objectMapper.writeValueAsString(owner1);

        System.out.println(result.getResponse().getContentAsString());

        JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createTestWithPost() throws Exception {
        Owner newOwner = new Owner(3, "Sunny", "Chew", date, date,null);

        Pet pet1 = new Pet(5, "DouDou", "Labrador", date, date, newOwner);
        Pet pet2 = new Pet(6, "DouDou2", "Labrador", date, date, newOwner);
        List<Pet> petList = new ArrayList<>();
        petList.add(pet1);
        petList.add(pet2);

        newOwner.setPetList(petList);

        Mockito.when(ownerController.create(newOwner)).thenReturn(newOwner);

        // Convert the object into json
        String content = objectMapper.writeValueAsString(newOwner);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owner/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Sunny")));
    }

    @Test
    public void findByDateTest() throws Exception {

        List<Owner> ownerList = new ArrayList<>(Arrays.asList(owner1, owner2));
        when(ownerService.findByDateCreated("2022-07-15")).thenReturn(ownerList);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/owner/findByDateCreated?dateCreated=2022-07-15")
                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.hasSize(2)))
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].firstName", Matchers.is("Jony")));
    }

}