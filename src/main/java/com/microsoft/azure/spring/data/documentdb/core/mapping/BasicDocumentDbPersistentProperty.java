/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.data.documentdb.core.mapping;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;


public class BasicDocumentDbPersistentProperty extends AnnotationBasedPersistentProperty<DocumentDbPersistentProperty>
        implements DocumentDbPersistentProperty {

    private static final String ID_PROPERTY_NAME = "id";

    public BasicDocumentDbPersistentProperty(Property property, DocumentDbPersistentEntity<?> owner,
                                             SimpleTypeHolder simpleTypeHolder) {
        super(property, owner, simpleTypeHolder);
    }

    @Override
    protected Association<DocumentDbPersistentProperty> createAssociation() {
        return new Association<>(this, null);
    }

    @Override
    public boolean isIdProperty() {

        if (super.isIdProperty()) {
            return true;
        }

        return getName().equals(ID_PROPERTY_NAME);
    }

}
