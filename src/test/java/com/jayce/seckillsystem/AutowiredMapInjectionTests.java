package com.jayce.seckillsystem;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = AutowiredMapInjectionTests.SpringConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutowiredMapInjectionTests {

	@Autowired
	@Qualifier("myMapBean")
	private Map<String, Object> myMapBean;

	/**
	 * This test passes correctly.
	 */
	@Test
	@Order(1)
	public void injectMapViaAutowiredAnnotation1() {
		System.out.println("Order1");
		System.out.println(this.myMapBean.getClass());
		this.myMapBean.forEach((k, v) -> System.out.println("k = " + k + ", v = " + v));
		assertThat(this.myMapBean).isNotNull();
		assertThat(this.myMapBean).isInstanceOf(Map.class);
		// Should return only 1 bean "myMapBean", not the underlying map.
		assertThat(this.myMapBean).hasSize(1);
	}

	/**
	 * This test should fail but does not.
	 */
	@Test
	@Order(2)
	public void injectMapViaAutowiredAnnotation2() {
		System.out.println("Order2");
		System.out.println(this.myMapBean.getClass());
		this.myMapBean.forEach((k, v) -> System.out.println("k = " + k + ", v = " + v));
		assertThat(this.myMapBean).isNotNull();
		assertThat(this.myMapBean).isInstanceOf(Map.class);
		// Should fail!! and return only 1 bean "myMapBean", not the underlying map.
		assertThat(this.myMapBean).hasSize(2);
	}

	@Configuration
	static class SpringConfig {
		@Bean("myMapBean")
		public Map<String, Object> myMapBean() {
			final Map<String, Object> myMapBean = new HashMap<>();
			myMapBean.put("hello", "world");
			myMapBean.put("foo", "bar");
			System.out.println("SpringConfig: \n" + myMapBean.getClass());
			return myMapBean;
		}
	}
}
