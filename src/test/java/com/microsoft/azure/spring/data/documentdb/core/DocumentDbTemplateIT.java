/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.data.documentdb.core;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.spring.data.documentdb.Constants;
import com.microsoft.azure.spring.data.documentdb.core.convert.MappingDocumentDbConverter;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbMappingContext;
import com.microsoft.azure.spring.data.documentdb.domain.Address;
import com.microsoft.azure.spring.data.documentdb.domain.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.Persistent;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource(value = {"classpath:application.properties"})
public class DocumentDbTemplateIT {
    private static final String TEST_ID = "template_it_id";
    private static final String TEST_NOTEXIST_ID = "non_exist_id";

    private static final String TEST_DB_NAME = "template_it_db";
    private static final List<String> HOBBIES = Constants.HOBBIES;
    private static final List<Address> ADDRESSES = Constants.ADDRESSES;
    private static final Person TEST_PERSON = new Person(TEST_ID, "testfirstname", "testlastname", HOBBIES, ADDRESSES);

    @Value("${documentdb.uri}")
    private String documentDbUri;
    @Value("${documentdb.key}")
    private String documentDbKey;

    private DocumentClient documentClient;
    private DocumentDbTemplate dbTemplate;

    private MappingDocumentDbConverter dbConverter;
    private DocumentDbMappingContext mappingContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        mappingContext = new DocumentDbMappingContext();
        try {
            mappingContext.setInitialEntitySet(new EntityScanner(this.applicationContext)
                    .scan(Persistent.class));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());

        }
        dbConverter = new MappingDocumentDbConverter(mappingContext);
        documentClient = new DocumentClient(documentDbUri, documentDbKey,
                ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);

        dbTemplate = new DocumentDbTemplate(documentClient, dbConverter, TEST_DB_NAME);

        dbTemplate.createCollectionIfNotExists(Person.class.getSimpleName(), null, null);
        dbTemplate.insert(Person.class.getSimpleName(), TEST_PERSON, null);
    }

    @After
    public void cleanup() {
        dbTemplate.deleteAll(Person.class.getSimpleName());
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDuplicateId() {
        dbTemplate.insert(Person.class.getSimpleName(), TEST_PERSON, null);
    }

    @Test
    public void testFindAll() {
        final List<Person> result = dbTemplate.findAll(Person.class.getSimpleName(), Person.class);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(TEST_PERSON);
    }

    @Test
    public void testFindById() {
        final Person result = dbTemplate.findById(Person.class.getSimpleName(),
                TEST_PERSON.getId(), Person.class);
        assertTrue(result.equals(TEST_PERSON));

        final Person nullResult = dbTemplate.findById(Person.class.getSimpleName(),
                TEST_NOTEXIST_ID, Person.class);
        assertThat(nullResult).isNull();
    }

    @Test
    public void testUpsertNewDocument() {
        // Delete first as was inserted in setup
        dbTemplate.deleteById(Person.class.getSimpleName(), TEST_PERSON.getId(), Person.class, null);

        final String firstName = "newFirstName_" + UUID.randomUUID().toString();
        final Person newPerson = new Person(null, firstName, "newLastName", null, null);

        dbTemplate.upsert(Person.class.getSimpleName(), newPerson, null, null);

        final List<Person> result = dbTemplate.findAll(Person.class);

        assertThat(result.size()).isEqualTo(1);
        assertTrue(result.get(0).getFirstName().equals(firstName));
    }

    @Test
    public void testUpdate() {
        final Person updated = new Person(TEST_PERSON.getId(), "updatedname",
                TEST_PERSON.getLastName(), TEST_PERSON.getHobbies(), TEST_PERSON.getShippingAddresses());
        dbTemplate.upsert(Person.class.getSimpleName(), updated, updated.getId(), null);

        final Person result = dbTemplate.findById(Person.class.getSimpleName(),
                updated.getId(), Person.class);

        assertTrue(result.equals(updated));
    }

    @Test
    public void testDeleteById() {
        final Person person2 = new Person("newid", "newfn", "newln", HOBBIES, ADDRESSES);
        dbTemplate.insert(person2, null);
        assertThat(dbTemplate.findAll(Person.class).size()).isEqualTo(2);

        dbTemplate.deleteById(Person.class.getSimpleName(), TEST_PERSON.getId(), null, null);

        final List<Person> result = dbTemplate.findAll(Person.class);
        assertThat(result.size()).isEqualTo(1);
        assertTrue(result.get(0).equals(person2));
    }
}
