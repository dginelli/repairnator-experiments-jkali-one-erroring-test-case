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
package org.apache.plc4x.java.ads.api.commands;

import org.apache.plc4x.java.ads.api.commands.types.IndexGroup;
import org.apache.plc4x.java.ads.api.commands.types.IndexOffset;
import org.apache.plc4x.java.ads.api.commands.types.Length;
import org.apache.plc4x.java.ads.api.generic.ADSData;
import org.apache.plc4x.java.ads.api.generic.AMSHeader;
import org.apache.plc4x.java.ads.api.generic.types.AMSNetId;
import org.apache.plc4x.java.ads.api.generic.types.AMSPort;
import org.apache.plc4x.java.ads.api.generic.types.Command;
import org.apache.plc4x.java.ads.api.generic.types.Invoke;

import static java.util.Objects.requireNonNull;

/**
 * With ADS Read data can be read from an ADS device.  The data are addressed by the Index Group and the Index Offset
 */
@ADSCommandType(Command.ADS_READ)
public class ADSReadRequest extends ADSAbstractRequest {

    /**
     * 4 bytes	Index Group of the data which should be read.
     */
    private final IndexGroup indexGroup;

    /**
     * 4 bytes	Index Offset of the data which should be read.
     */
    private final IndexOffset indexOffset;

    /**
     * 4 bytes	Length of the data (in bytes) which should be read.
     */
    private final Length length;

    private ADSReadRequest(AMSHeader amsHeader, IndexGroup indexGroup, IndexOffset indexOffset, Length length) {
        super(amsHeader);
        this.indexGroup = requireNonNull(indexGroup);
        this.indexOffset = requireNonNull(indexOffset);
        this.length = requireNonNull(length);
    }

    private ADSReadRequest(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, IndexGroup indexGroup, IndexOffset indexOffset, Length length) {
        super(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId);
        this.indexGroup = requireNonNull(indexGroup);
        this.indexOffset = requireNonNull(indexOffset);
        this.length = requireNonNull(length);
    }

    public static ADSReadRequest of(AMSHeader amsHeader, IndexGroup indexGroup, IndexOffset indexOffset, Length length) {
        return new ADSReadRequest(amsHeader, indexGroup, indexOffset, length);
    }

    public static ADSReadRequest of(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, IndexGroup indexGroup, IndexOffset indexOffset, Length length) {
        return new ADSReadRequest(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId, indexGroup, indexOffset, length);
    }

    public IndexGroup getIndexGroup() {
        return indexGroup;
    }

    public IndexOffset getIndexOffset() {
        return indexOffset;
    }

    public Length getLength() {
        return length;
    }

    @Override
    public ADSData getAdsData() {
        return buildADSData(indexGroup, indexOffset, length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ADSReadRequest))
            return false;
        if (!super.equals(o))
            return false;

        ADSReadRequest that = (ADSReadRequest) o;

        if (!indexGroup.equals(that.indexGroup))
            return false;
        if (!indexOffset.equals(that.indexOffset))
            return false;

        return length.equals(that.length);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + indexGroup.hashCode();
        result = 31 * result + indexOffset.hashCode();
        result = 31 * result + length.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ADSReadRequest{" +
            "indexGroup=" + indexGroup +
            ", indexOffset=" + indexOffset +
            ", length=" + length +
            "} " + super.toString();
    }
}
