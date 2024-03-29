package com.hsbc.codechallenge;

import com.hsbc.codechallenge.api.ApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeChallengeApplicationTests {

	@Autowired
	private ApiController apiController;

	@Test
	public void contextLoads() {
		assertThat(apiController).isNotNull();
	}
}
