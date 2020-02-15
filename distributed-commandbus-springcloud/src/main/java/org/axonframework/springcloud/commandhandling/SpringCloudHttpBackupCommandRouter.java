/*
 * Copyright (c) 2010-2017. Axon Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.springcloud.commandhandling;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.distributed.RoutingStrategy;
import org.axonframework.commandhandling.distributed.SimpleMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of the {@link org.axonframework.springcloud.commandhandling.SpringCloudCommandRouter} which has a
 * backup mechanism to request and provide a member and/or its {@link MessageRoutingInformation}.
 * It uses {@link org.springframework.web.client.RestTemplate} to request {@code MembershipInformation} and services the
 * purposes of a queryable location to retrieve the local {@code MembershipInformation} from by being a
 * {@link org.springframework.web.bind.annotation.RestController}.
 * The default endpoint for this set up is "/message-routing-information".
 * To configure this, the "axon.distributed.spring-cloud.fallback-url" application property needs to be set.
 *
 * @author Steven van Beelen
 */
@RestController
@RequestMapping("${axon.distributed.spring-cloud.fallback-url:/message-routing-information}")
public class SpringCloudHttpBackupCommandRouter extends SpringCloudCommandRouter {

    private static final Predicate<ServiceInstance> ACCEPT_ALL_INSTANCES_FILTER = serviceInstance -> true;

    private final RestTemplate restTemplate;
    private final String messageRoutingInformationEndpoint;

    private volatile MessageRoutingInformation messageRoutingInfo;

    /**
     * Initialize a {@link org.axonframework.commandhandling.distributed.CommandRouter} with the given {@link
     * org.springframework.cloud.client.discovery.DiscoveryClient} to update its own membership as a {@code
     * CommandRouter} and to create its own awareness of available nodes to send commands to in a {@link
     * org.axonframework.commandhandling.distributed.ConsistentHash}.
     * The {@code routingStrategy} is used to define the key based on which Command Messages are routed to their
     * respective handler nodes.
     * The {@link org.springframework.web.client.RestTemplate} is used as a backup mechanism to request another member's
     * {@link org.axonframework.springcloud.commandhandling.MessageRoutingInformation} with.
     * Uses a default {@code Predicate<ServiceInstance>} filter function which allows any
     * {@link org.springframework.cloud.client.ServiceInstance} through the update membership process.
     *
     * @param discoveryClient                   The {@code DiscoveryClient} used to discovery and notify other nodes
     * @param routingStrategy                   The strategy for routing Commands to a Node
     * @param restTemplate                      The {@code RestTemplate} used to request another member's {@link
     *                                          org.axonframework.springcloud.commandhandling.MessageRoutingInformation}
     *                                          with.
     * @param messageRoutingInformationEndpoint The endpoint where to retrieve the another nodes message routing
     *                                          information from
     */
    public SpringCloudHttpBackupCommandRouter(DiscoveryClient discoveryClient,
                                              RoutingStrategy routingStrategy,
                                              RestTemplate restTemplate,
                                              @Value("${axon.distributed.spring-cloud.fallback-url}:/message-routing-information") String messageRoutingInformationEndpoint) {
        this(discoveryClient,
             routingStrategy,
             ACCEPT_ALL_INSTANCES_FILTER,
             restTemplate,
             messageRoutingInformationEndpoint);
    }

    /**
     * Initialize a {@link org.axonframework.commandhandling.distributed.CommandRouter} with the given {@link
     * org.springframework.cloud.client.discovery.DiscoveryClient} to update its own membership as a {@code
     * CommandRouter} and to create its own awareness of available nodes to send commands to in a {@link
     * org.axonframework.commandhandling.distributed.ConsistentHash}.
     * The {@code routingStrategy} is used to define the key based on which Command Messages are routed to their
     * respective handler nodes.
     * A {@code Predicate<ServiceInstance>} to filter a {@link org.springframework.cloud.client.ServiceInstance} from
     * the membership update loop.
     * The {@link org.springframework.web.client.RestTemplate} is used as a backup mechanism to request another member's
     * {@link org.axonframework.springcloud.commandhandling.MessageRoutingInformation} with.
     *
     * @param discoveryClient                   The {@code DiscoveryClient} used to discovery and notify other nodes
     * @param routingStrategy                   The strategy for routing Commands to a Node
     * @param serviceInstanceFilter             The {@code Predicate<ServiceInstance>} used to filter
     * @param restTemplate                      The {@code RestTemplate} used to request another member's {@link
     *                                          org.axonframework.springcloud.commandhandling.MessageRoutingInformation}
     *                                          with.
     * @param messageRoutingInformationEndpoint The endpoint where to retrieve the
     *                                          another nodes message routing
     *                                          information from
     */
    public SpringCloudHttpBackupCommandRouter(DiscoveryClient discoveryClient,
                                              RoutingStrategy routingStrategy,
                                              Predicate<ServiceInstance> serviceInstanceFilter,
                                              RestTemplate restTemplate,
                                              @Value("${axon.distributed.spring-cloud.fallback-url}:/message-routing-information") String messageRoutingInformationEndpoint) {
        super(discoveryClient, routingStrategy, serviceInstanceFilter);
        this.restTemplate = restTemplate;
        this.messageRoutingInformationEndpoint = messageRoutingInformationEndpoint;
        this.messageRoutingInfo = null;
    }

    @Override
    public void updateMembership(int loadFactor, Predicate<? super CommandMessage<?>> commandFilter) {
        messageRoutingInfo = new MessageRoutingInformation(loadFactor, commandFilter, serializer);
        super.updateMembership(loadFactor, commandFilter);
    }

    @GetMapping
    public MessageRoutingInformation getLocalMessageRoutingInformation() {
        return messageRoutingInfo;
    }

    @Override
    protected Optional<MessageRoutingInformation> getMessageRoutingInformation(ServiceInstance serviceInstance) {
        Optional<MessageRoutingInformation> defaultMessageRoutingInfo =
                super.getMessageRoutingInformation(serviceInstance);
        return defaultMessageRoutingInfo.isPresent() ?
                defaultMessageRoutingInfo : requestMessageRoutingInformation(serviceInstance);
    }

    private Optional<MessageRoutingInformation> requestMessageRoutingInformation(ServiceInstance serviceInstance) {
        SimpleMember<URI> simpleMember = buildSimpleMember(serviceInstance);
        if (simpleMember.local()) {
            return Optional.of(getLocalMessageRoutingInformation());
        }

        URI endpoint = simpleMember.getConnectionEndpoint(URI.class)
                                   .orElseThrow(() -> new IllegalArgumentException(String.format(
                                           "No Connection Endpoint found in Member [%s] for protocol [%s] to send a " +
                                                   "%s request to", simpleMember,
                                           URI.class, MessageRoutingInformation.class.getSimpleName()
                                   )));
        URI destinationUri = buildURIForPath(endpoint, messageRoutingInformationEndpoint);

        ResponseEntity<MessageRoutingInformation> responseEntity = restTemplate.exchange(destinationUri,
                                                                                         HttpMethod.GET,
                                                                                         HttpEntity.EMPTY,
                                                                                         MessageRoutingInformation.class);

        return responseEntity.hasBody() ? Optional.of(responseEntity.getBody()) : Optional.empty();
    }

    private static URI buildURIForPath(URI uri, String appendToPath) {
        return UriComponentsBuilder.fromUri(uri)
                                   .path(uri.getPath() + appendToPath)
                                   .build()
                                   .toUri();
    }
}
