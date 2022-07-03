package com.example.PetShop;

import com.example.PetShop.controller.PetController;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class PetShopApplicationTests {

	@Autowired
	PetController petController;

	@Test
	void contextLoads() {
		assertNotNull(petController);
	}

}
