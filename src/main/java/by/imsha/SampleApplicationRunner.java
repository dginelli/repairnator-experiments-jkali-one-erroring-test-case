package by.imsha;

import by.imsha.domain.Mass;
import by.imsha.domain.dto.MassInfo;
import by.imsha.domain.dto.mapper.MassInfoMapper;
import by.imsha.repository.CityRepository;
import by.imsha.repository.MassRepository;
import by.imsha.repository.factory.QuerableMongoRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.time.LocalTime;

/*
@SpringBootApplication
@ComponentScan(basePackages = {"by.imsha", "com.auth"})

@EnableAutoConfiguration(   )  // Sprint Boot Auto Configuration
@EnableMongoRepositories(
        repositoryFactoryBeanClass = QuerableMongoRepositoryFactoryBean.class
)
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:auth0.properties")
})*/
public class SampleApplicationRunner implements CommandLineRunner {

    @Autowired
    private CityRepository repository;

    @Autowired
    private MassRepository massRepo;

    @Override
    public void run(String... args) throws Exception {

        // save a couple of customers
        Mass mass = new Mass();
        mass.setDays(new int[]{1,3,4});
        mass.setDuration(60 * 60);
        mass.setLangCode("BY");
        mass.setCityId("580bb971f9efb566767eee77");
        mass.setParishId("574360fcccbd5297c86047fd");
        mass.setTime("11:10");
//        mass.setSingleEndTime(LocalDateTime.now());

        this.massRepo.save(mass);

        MassInfo massInfo = MassInfoMapper.MAPPER.toMassInfo(mass);
        System.out.println("!!!!!!!!!!mass = " + mass);
        System.out.println("++++++massInfo = " + massInfo);

  }

   /* public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleApplicationRunner.class, args);
    }*/

}
