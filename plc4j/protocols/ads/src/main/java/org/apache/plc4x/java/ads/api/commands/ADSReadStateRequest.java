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

import org.apache.plc4x.java.ads.api.generic.ADSData;
import org.apache.plc4x.java.ads.api.generic.AMSHeader;
import org.apache.plc4x.java.ads.api.generic.types.AMSNetId;
import org.apache.plc4x.java.ads.api.generic.types.AMSPort;
import org.apache.plc4x.java.ads.api.generic.types.Command;
import org.apache.plc4x.java.ads.api.generic.types.Invoke;

/**
 * Reads the ADS status and the device status of an ADS device.
 * <p>
 * No additional data required
 */
@ADSCommandType(Command.ADS_READ_STATE)
public class ADSReadStateRequest extends ADSAbstractRequest {

    private ADSReadStateRequest(AMSHeader amsHeader) {
        super(amsHeader);
    }

    private ADSReadStateRequest(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId) {
        super(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId);
    }

    public static ADSReadStateRequest of(AMSHeader amsHeader) {
        return new ADSReadStateRequest(amsHeader);
    }

    public static ADSReadStateRequest of(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Invoke invokeId) {
        return new ADSReadStateRequest(targetAmsNetId, targetAmsPort, sourceAmsNetId, sourceAmsPort, invokeId);
    }

    @Override
    public ADSData getAdsData() {
        return ADSData.EMPTY;
    }

}
