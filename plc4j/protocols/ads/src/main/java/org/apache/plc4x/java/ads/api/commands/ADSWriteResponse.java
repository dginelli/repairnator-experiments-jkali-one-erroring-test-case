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

import org.apache.plc4x.java.ads.api.commands.types.Result;
import org.apache.plc4x.java.ads.api.generic.ADSData;
import org.apache.plc4x.java.ads.api.generic.AMSHeader;
import org.apache.plc4x.java.ads.api.generic.types.AMSNetId;
import org.apache.plc4x.java.ads.api.generic.types.AMSPort;
import org.apache.plc4x.java.ads.api.generic.types.Command;
import org.apache.plc4x.java.ads.api.generic.types.Invoke;

import static java.util.Objects.requireNonNull;

/**
 * With ADS Write data can be written to an ADS device.
 */
@ADSCommandType(Command.ADS_WRITE)
public class ADSWriteResponse extends ADSAbstractResponse {

    /**
     * 4 bytes	ADS error number
     */
    private final Result result;

    private ADSWriteResponse(AMSHeader amsHeader, Result result) {
        super(amsHeader);
        this.result = requireNonNull(result);
    }

    private ADSWriteResponse(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, Result result) {
        super(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId);
        this.result = requireNonNull(result);
    }

    public static ADSWriteResponse of(AMSHeader amsHeader, Result result) {
        return new ADSWriteResponse(amsHeader, result);
    }

    public static ADSWriteResponse of(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId, Result result) {
        return new ADSWriteResponse(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId, result);
    }

    public Result getResult() {
        return result;
    }

    @Override
    public ADSData getAdsData() {
        return buildADSData(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ADSWriteResponse))
            return false;
        if (!super.equals(o))
            return false;

        ADSWriteResponse that = (ADSWriteResponse) o;

        return result.equals(that.result);
    }

    @Override
    public int hashCode() {
        int result1 = super.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    @Override
    public String toString() {
        return "ADSWriteResponse{" +
            "result=" + result +
            "} " + super.toString();
    }
}
