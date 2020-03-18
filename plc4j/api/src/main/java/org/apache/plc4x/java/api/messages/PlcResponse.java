/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.java.api.messages;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.plc4x.java.api.messages.items.RequestItem;
import org.apache.plc4x.java.api.messages.items.ResponseItem;

/**
 * Base type for all response messages sent as response for a prior request
 * from a plc to the plc4x system.
 * @param <REQUEST> 
 * @param <RESPONSE_ITEM> 
 * @param <REQUEST_ITEM> 
 */
public abstract class PlcResponse<REQUEST extends PlcRequest, RESPONSE_ITEM extends ResponseItem, REQUEST_ITEM extends RequestItem> implements PlcMessage {

    private final REQUEST request;

    private final List<? extends RESPONSE_ITEM> responseItems;

    public PlcResponse(REQUEST request, List<? extends RESPONSE_ITEM> responseItems) {
        Objects.requireNonNull(request, "Request must not be null");
        Objects.requireNonNull(responseItems, "Response items must not be null");
        this.request = request;
        this.responseItems = responseItems;
    }

    public REQUEST getRequest() {
        return request;
    }

    public List<? extends RESPONSE_ITEM> getResponseItems() {
        return responseItems;
    }

    public Optional<? extends RESPONSE_ITEM> getResponseItem() {
        if (isMultiValue()) {
            throw new IllegalStateException("Too many items " + getNumberOfItems());
        }
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getResponseItems().get(0));
    }

    public int getNumberOfItems() {
        return getResponseItems().size();
    }

    public boolean isMultiValue() {
        return getNumberOfItems() > 1;
    }

    public boolean isEmpty() {
        return getNumberOfItems() < 1;
    }

    public Optional<RESPONSE_ITEM> getValue(REQUEST_ITEM item) {
        return getResponseItems().stream()
            .filter(x -> x.getRequestItem().equals(item))
            .map(e -> (RESPONSE_ITEM) e)
            .findAny();
    }

}
