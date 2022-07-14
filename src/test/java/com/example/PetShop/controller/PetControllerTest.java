package com.example.PetShop.controller;

import com.example.PetShop.controller.OwnerController;
import com.example.PetShop.controller.PetController;
import com.example.PetShop.model.Owner;
import com.example.PetShop.model.Pet;
import com.example.PetShop.service.OwnerService;
import com.example.PetShop.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PetController petController;

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

    private Pet pet1 = new Pet(1, "DouDou", "Labrador", date, date, 1);
    private Pet pet2 = new Pet(2, "DouDou2", "Labrador", date, date, 1);
    private Pet pet3 = new Pet(3, "DouDou3", "Labrador", date, date, 2);
    private Pet pet4 = new Pet(4, "DouDou4", "Labrador", date, date, 2);

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
    public void getAllPets() throws Exception {

        List<Pet> petList = new ArrayList<>(Arrays.asList(pet1, pet2, pet3, pet4));

        when(petService.findAll()).thenReturn(petList);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/pet")
                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", Matchers.is("DouDou")))
                .andExpect(jsonPath("$[1].name", Matchers.is("DouDou2")));
    }

    @Test
    public void retrievePetById() throws Exception {
        Mockito.when(petService.findById(1)).thenReturn(Optional.of(pet1));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expectedResult = objectMapper.writeValueAsString(pet1);

        System.out.println(result.getResponse().getContentAsString());

        JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createPetTest() throws Exception {

        Pet newPet = new Pet(5, "Cola", "Pomeranian", date, date, 2);

        List<Owner> ownerList = (List<Owner>) ownerService.findAll();
        ownerList.stream().forEach(System.out::println);


        Mockito.when(petService.save(newPet)).thenReturn(newPet);

        // Convert the object into json
        String content = objectMapper.writeValueAsString(newPet);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Cola")));
    }

    @Test
    public void findByPetIdTest() throws Exception {

        Pet newPet = new Pet(6, "Snowy", "Pomeranian", date, date, 2);

        Mockito.when(petService.findById(newPet.getId())).thenReturn(Optional.of(newPet));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pet/6")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Snowy")));
    }

    @Test
    public void updatePetTest() throws Exception {
        Pet updatePet = new Pet(7, "Loki", "Pomeranian", date, date, 2);

        when(petService.findById(updatePet.getId())).thenReturn(Optional.of(updatePet));

        Mockito.when(petService.save(updatePet)).thenReturn(updatePet);

        String updateContent = objectMapper.writeValueAsString(updatePet);

        MockHttpServletRequestBuilder requestBuilders = MockMvcRequestBuilders.put("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updateContent);

        mockMvc.perform(requestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Loki")));
    }

    @Test
    public void deleteByIdTest() throws Exception {

        Pet deletePet = new Pet(8, "Loki", "Pomeranian", date, date, 2);

        when(petService.findById(deletePet.getId())).thenReturn(Optional.of(deletePet));

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/pet/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findTopNameTest() throws Exception {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj1 = new JSONObject();
        JSONObject jsonObj2 = new JSONObject();

        jsonObj1.put("name", "DouDou");
        jsonObj1.put("counter", 2);

        jsonObj2.put("name", "Dolla");
        jsonObj2.put("counter", 1);

        jsonArray.add(jsonObj1);
        jsonArray.add(jsonObj2);

        Mockito.when(petController.findTopName()).thenReturn(jsonArray);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/pet/top")
                        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].name",is("DouDou")))
                .andExpect(jsonPath("$[0].counter",is(2)));

    }
}