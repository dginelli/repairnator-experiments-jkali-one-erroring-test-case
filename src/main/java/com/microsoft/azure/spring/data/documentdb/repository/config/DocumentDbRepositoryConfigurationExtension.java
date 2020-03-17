/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.microsoft.azure.spring.data.documentdb.repository.config;

import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbMappingContext;
import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.microsoft.azure.spring.data.documentdb.repository.support.DocumentDbRepositoryFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;


public class DocumentDbRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    @Override
    public String getModuleName() {
        return "documentdb";
    }

    @Override
    public String getModulePrefix() {
        return "documentdb";
    }

    public String getRepositoryFactoryBeanClassName() {
        return DocumentDbRepositoryFactoryBean.class.getName();
    }

    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.<Class<?>>singleton(DocumentDbRepository.class);
    }

    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.emptyList();
    }


    @Override
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {
        super.registerBeansForRoot(registry, config);

        if (!registry.containsBeanDefinition("documentDbMappingContext")) {
            final RootBeanDefinition definition = new RootBeanDefinition(DocumentDbMappingContext.class);
            definition.setRole(AbstractBeanDefinition.ROLE_INFRASTRUCTURE);
            definition.setSource(config.getSource());

            registry.registerBeanDefinition("documentDbMappingContext", definition);
        }
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
        super.postProcess(builder, source);
    }
}
