package guru.bonacci.spectre.money.services;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import guru.bonacci.spectre.money.MoneyTestApp;
import guru.bonacci.spectre.spectreshared.persistence.Spec;
import guru.bonacci.spectre.spectreshared.persistence.SpecRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MoneyTestApp.class, webEnvironment = NONE)
public class MoneyServiceTests {

	@Autowired
	MoneyService service;
	
	@MockBean
	SpecRepository repo;

	@MockBean
	MoneyCache cache;

	@Test
	public void shouldAddData() throws Exception {
		Spec spec = new Spec();
		spec.id = "ID";
		spec.geoip.country_code2 = "NZ";
		when(repo.findOne(spec.id)).thenReturn(spec);

		String enrichmentData = "very little";
		doReturn(enrichmentData).when(cache).get("NZ");

		service.enrich(spec.id);

		ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Object> arg2 = ArgumentCaptor.forClass(Object.class);
		ArgumentCaptor<Spec> arg3 = ArgumentCaptor.forClass(Spec.class);
		verify(repo).addData(arg1.capture(), arg2.capture(), arg3.capture());

		assertThat(arg1.getValue(), is(equalTo("income")));
		assertThat(arg2.getValue(), is(equalTo(enrichmentData)));
		assertThat(arg3.getValue(), is(equalTo(spec)));
	}
}