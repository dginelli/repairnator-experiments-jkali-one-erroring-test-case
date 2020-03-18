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
package org.apache.plc4x.java.ads.model;

import org.apache.plc4x.java.ads.api.util.ByteValue;
import org.apache.plc4x.java.api.model.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ADSAddress implements Address {
    private static final Pattern RESSOURCE_ADDRESS_PATTERN = Pattern.compile("^(?<indexGroup>\\d+)/(?<indexOffset>\\d+)");

    private final long indexGroup;

    private final long indexOffset;

    private ADSAddress(long indexGroup, long indexOffset) {
        ByteValue.checkUnsignedBounds(indexGroup, 4);
        this.indexGroup = indexGroup;
        ByteValue.checkUnsignedBounds(indexOffset, 4);
        this.indexOffset = indexOffset;
    }

    public static ADSAddress of(long indexGroup, long indexOffset) {
        return new ADSAddress(indexGroup, indexOffset);
    }

    public static ADSAddress of(String address) {
        Matcher matcher = RESSOURCE_ADDRESS_PATTERN.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "address " + address + " doesn't match '{indexGroup}/{indexOffset}' RAW:" + RESSOURCE_ADDRESS_PATTERN);
        }
        String indexGroup = matcher.group("indexGroup");
        String indexOffset = matcher.group("indexOffset");

        return new ADSAddress(Long.parseLong(indexGroup), Long.parseLong(indexOffset));
    }

    public long getIndexGroup() {
        return indexGroup;
    }

    public long getIndexOffset() {
        return indexOffset;
    }
}
