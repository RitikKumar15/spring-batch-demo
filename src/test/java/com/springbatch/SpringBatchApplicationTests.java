package com.springbatch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class SpringBatchApplicationTests {

	@Test
	void contextLoads() {
        Assert.isTrue(true, "Context loads successfully");
	}

}
