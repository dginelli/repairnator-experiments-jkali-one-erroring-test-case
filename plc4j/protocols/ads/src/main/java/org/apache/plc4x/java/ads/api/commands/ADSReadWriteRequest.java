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

import org.apache.plc4x.java.ads.api.commands.types.*;
import org.apache.plc4x.java.ads.api.generic.ADSData;
import org.apache.plc4x.java.ads.api.generic.AMSHeader;
import org.apache.plc4x.java.ads.api.generic.types.AMSNetId;
import org.apache.plc4x.java.ads.api.generic.types.AMSPort;
import org.apache.plc4x.java.ads.api.generic.types.Command;
import org.apache.plc4x.java.ads.api.generic.types.Invoke;
import org.apache.plc4x.java.ads.api.util.LengthSupplier;

import static java.util.Objects.requireNonNull;

/**
 * With ADS Read Write data will be written to an ADS device. Additionally, data can be read from the ADS device.
 * <p>
 * The data which can be read are addressed by the Index Group and the Index Offset
 */
@ADSCommandType(Command.ADS_READ_WRITE)
public class ADSReadWriteRequest extends ADSAbstractRequest {

    /**
     * 4 bytes	Index Group, in which the data should be written.
     */
    private final IndexGroup indexGroup;
    /**
     * 4 bytes	Index Offset, in which the data should be written
     */
    private final IndexOffset indexOffset;
    /**
     * 4 bytes	Length of data in bytes, which should be read.
     */
    private final ReadLength readLength;
    /**
     * 4 bytes	Length of data in bytes, which should be written
     */
    private final WriteLength writeLength;
    /**
     * n bytes	Data which are written in the ADS device.
     */
    private final Data data;

    private final LengthSupplier writeLengthSupplier;

    private ADSReadWriteRequest(AMSHeader amsHeader, IndexGroup indexGroup, IndexOffset indexOffset, ReadLength readLength, WriteLength writeLength, Data data) {
        super(amsHeader);
        this.indexGroup = requireNonNull(indexGroup);
        this.indexOffset = requireNonNull(indexOffset);
        this.readLength = requireNonNull(readLength);
        this.writeLength = requireNonNull(writeLength);
        this.data = requireNonNull(data);
        this.writeLengthSupplier = null;
    }

    private ADSReadWriteRequest(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, IndexGroup indexGroup, IndexOffset indexOffset, ReadLength readLength, Data data) {
        super(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId);
        this.indexGroup = requireNonNull(indexGroup);
        this.indexOffset = requireNonNull(indexOffset);
        this.readLength = requireNonNull(readLength);
        this.writeLength = null;
        this.data = requireNonNull(data);
        this.writeLengthSupplier = data;
    }

    public static ADSReadWriteRequest of(AMSHeader amsHeader, IndexGroup indexGroup, IndexOffset indexOffset, ReadLength readLength, WriteLength writeLength, Data data) {
        return new ADSReadWriteRequest(amsHeader, indexGroup, indexOffset, readLength, writeLength, data);
    }

    public static ADSReadWriteRequest of(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, IndexGroup indexGroup, IndexOffset indexOffset, ReadLength readLength, Data data) {
        return new ADSReadWriteRequest(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId, indexGroup, indexOffset, readLength, data);
    }

    public IndexGroup getIndexGroup() {
        return indexGroup;
    }

    public IndexOffset getIndexOffset() {
        return indexOffset;
    }

    public ReadLength getReadLength() {
        return readLength;
    }

    public WriteLength getWriteLength() {
        return writeLengthSupplier == null ? writeLength : WriteLength.of(writeLengthSupplier.getCalculatedLength());
    }

    public Data getData() {
        return data;
    }

    @Override
    public ADSData getAdsData() {
        return buildADSData(indexGroup, indexOffset, readLength, getWriteLength(), data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ADSReadWriteRequest))
            return false;
        if (!super.equals(o))
            return false;

        ADSReadWriteRequest that = (ADSReadWriteRequest) o;

        if (!indexGroup.equals(that.indexGroup))
            return false;
        if (!indexOffset.equals(that.indexOffset))
            return false;
        if (!readLength.equals(that.readLength))
            return false;
        if (!getWriteLength().equals(that.getWriteLength()))
            return false;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + indexGroup.hashCode();
        result = 31 * result + indexOffset.hashCode();
        result = 31 * result + readLength.hashCode();
        result = 31 * result + getWriteLength().hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ADSReadWriteRequest{" +
            "indexGroup=" + indexGroup +
            ", indexOffset=" + indexOffset +
            ", readLength=" + readLength +
            ", writeLength=" + getWriteLength() +
            ", data=" + data +
            "} " + super.toString();
    }
}
