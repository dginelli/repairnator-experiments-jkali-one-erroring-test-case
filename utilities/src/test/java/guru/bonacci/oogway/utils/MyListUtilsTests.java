package guru.bonacci.oogway.utils;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MyListUtilsTests {

	@Test
	public void shouldReturnDifferentElements() {
		String s1 = "this is one string";
		String s2 = "this is another string";
		String s3 = "and this string is not even needed for this test";

		Set<String> results = new HashSet<>();
		for (int i=0; i<10; i++) 
			results.add(MyListUtils.random(asList(s1, s2, s3)).get());

		assertThat(results.size(), greaterThan(1));
	}
	
	@Test
	public void shouldReturnNoElementForEmptyCollection() {
		List<String> input = emptyList();
		Optional<String> result = MyListUtils.random(input);
		assertThat(true, is(not(result.isPresent())));
	}

	@Test
	public void shouldReturnNoElementForNull() {
		Optional<String> result = MyListUtils.random(null);
		assertThat(true, is(not(result.isPresent())));
	}
}

