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
package org.apache.plc4x.java.ads.api.serial;

import io.netty.buffer.ByteBuf;
import org.apache.plc4x.java.ads.api.generic.AMSPacket;
import org.apache.plc4x.java.ads.api.serial.types.*;
import org.apache.plc4x.java.ads.api.util.ByteReadable;
import org.apache.plc4x.java.api.exceptions.PlcRuntimeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * An AMS packet can be transferred via RS232 with the help of an AMS serial frame.
 * The actual AMS packet is in the user data field of the frame.
 * The max. length of the AMS packet is limited to 255 bytes.
 * Therefore the max. size of an AMS serial frame is 263 bytes.
 * The fragment number is compared with an internal counter by the receiver.
 * The frame number is simply accepted and not checked when receiving the first AMS frame or in case a timeout is
 * exceeded. The CRC16 algorithm is used for calculating the checksum.
 *
 * @see <a href="https://infosys.beckhoff.com/content/1033/tcadsamsserialspec/html/tcamssericalspec_amsframe.htm?id=8115637053270715044">TwinCAT AMS via RS232 Specification</a>
 */
public class AMSSerialFrame implements ByteReadable {

    public static final int ID = 0xA501;

    /**
     * Id for detecting an AMS serial frame.
     */
    private final MagicCookie magicCookie;

    /**
     * Address of the sending participant. This value can always be set to 0 for an RS232 communication,
     * since it is a 1 to 1 connection and hence the participants are unique.
     */
    private final TransmitterAddress transmitterAddress;

    /**
     * Receiver’s address. This value can always be set to 0 for an RS232 communication, since it is a 1 to 1
     * connection and hence the participants are unique.
     */
    private final ReceiverAddress receiverAddress;

    /**
     * Number of the frame sent. Once the number 255 has been sent, it starts again from 0. The receiver checks this
     * number with an internal counter.
     */
    private final FragmentNumber fragmentNumber;

    /**
     * The max. length of the AMS packet to be sent is 255. If larger AMS packets are to be sent then they have to be
     * fragmented (not published at the moment).
     */
    private final UserDataLength userDataLength;

    /**
     * The AMS packet to be sent.
     */
    private AMSPacket amsPacket;
    private final UserData userData;

    private final CRC crc;

    private AMSSerialFrame(MagicCookie magicCookie, TransmitterAddress transmitterAddress, ReceiverAddress receiverAddress, FragmentNumber fragmentNumber, UserDataLength userDataLength, UserData userData, CRC crc) {
        this.magicCookie = magicCookie;
        this.transmitterAddress = transmitterAddress;
        this.receiverAddress = receiverAddress;
        this.fragmentNumber = fragmentNumber;
        this.userDataLength = userDataLength;
        this.userData = userData;
        this.crc = crc;
    }

    private AMSSerialFrame(FragmentNumber fragmentNumber, AMSPacket amsPacket) {
        this.magicCookie = MagicCookie.of(ID);
        this.transmitterAddress = TransmitterAddress.RS232_COMM_ADDRESS;
        this.receiverAddress = ReceiverAddress.RS232_COMM_ADDRESS;
        this.fragmentNumber = fragmentNumber;
        long calculatedLength = amsPacket.getCalculatedLength();
        if (calculatedLength > 255) {
            throw new IllegalArgumentException("Paket length must not exceed 255");
        }
        this.userDataLength = UserDataLength.of((byte) calculatedLength);
        this.amsPacket = amsPacket;
        byte[] amsPacketBytes = amsPacket.getBytes();
        this.userData = UserData.of(amsPacketBytes);
        // TODO: java has no CRC-16 implementation so we better be of implementing it by ourself.
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("CRC-16");
        } catch (NoSuchAlgorithmException e) {
            throw new PlcRuntimeException(e);
        }
        messageDigest.update(magicCookie.getBytes());
        messageDigest.update(transmitterAddress.getBytes());
        messageDigest.update(receiverAddress.getBytes());
        messageDigest.update(fragmentNumber.getBytes());
        messageDigest.update(userDataLength.getBytes());
        byte[] digest = messageDigest.digest(amsPacketBytes);
        if (digest.length > 2) {
            throw new PlcRuntimeException("Digest length too great " + digest.length);
        }
        this.crc = CRC.of(digest[0], digest[1]);
    }

    public static AMSSerialFrame of(MagicCookie magicCookie, TransmitterAddress transmitterAddress, ReceiverAddress receiverAddress, FragmentNumber fragmentNumber, UserDataLength userDataLength, UserData userData, CRC crc) {
        return new AMSSerialFrame(magicCookie, transmitterAddress, receiverAddress, fragmentNumber, userDataLength, userData, crc);
    }

    public static AMSSerialFrame of(FragmentNumber fragmentNumber, AMSPacket amsPacket) {
        return new AMSSerialFrame(fragmentNumber, amsPacket);
    }

    public AMSPacket getAmsPacket() {
        return amsPacket;
    }

    @Override
    public ByteBuf getByteBuf() {
        return buildByteBuff(magicCookie, transmitterAddress, receiverAddress, fragmentNumber, userDataLength, userData, crc);
    }

}
