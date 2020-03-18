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
package org.apache.plc4x.java.s7.connection;

import io.netty.channel.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.plc4x.java.api.connection.PlcReader;
import org.apache.plc4x.java.api.connection.PlcWriter;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.exceptions.PlcException;
import org.apache.plc4x.java.api.messages.*;
import org.apache.plc4x.java.api.model.Address;
import org.apache.plc4x.java.base.connection.AbstractPlcConnection;
import org.apache.plc4x.java.base.connection.ChannelFactory;
import org.apache.plc4x.java.base.connection.TcpSocketChannelFactory;
import org.apache.plc4x.java.isoontcp.netty.IsoOnTcpProtocol;
import org.apache.plc4x.java.isotp.netty.IsoTPProtocol;
import org.apache.plc4x.java.isotp.netty.model.tpdus.DisconnectRequestTpdu;
import org.apache.plc4x.java.isotp.netty.model.types.DisconnectReason;
import org.apache.plc4x.java.isotp.netty.model.types.TpduSize;
import org.apache.plc4x.java.netty.events.S7ConnectionEvent;
import org.apache.plc4x.java.netty.events.S7ConnectionState;
import org.apache.plc4x.java.s7.model.S7Address;
import org.apache.plc4x.java.s7.model.S7BitAddress;
import org.apache.plc4x.java.s7.model.S7DataBlockAddress;
import org.apache.plc4x.java.s7.netty.Plc4XS7Protocol;
import org.apache.plc4x.java.s7.netty.S7Protocol;
import org.apache.plc4x.java.s7.netty.model.types.MemoryArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S7PlcConnection extends AbstractPlcConnection implements PlcReader, PlcWriter {

    private static final int ISO_ON_TCP_PORT = 102;

    private static final Pattern S7_DATABLOCK_ADDRESS_PATTERN =
        Pattern.compile("^DATA_BLOCKS/(?<blockNumber>\\d{1,4})/(?<byteOffset>\\d{1,4})");
    private static final Pattern S7_ADDRESS_PATTERN =
        Pattern.compile("^(?<memoryArea>.*?)/(?<byteOffset>\\d{1,4})(?:/(?<bitOffset>\\d))?");

    private static final Logger logger = LoggerFactory.getLogger(S7PlcConnection.class);

    private final int rack;
    private final int slot;

    private final TpduSize paramPduSize;
    private final short paramMaxAmqCaller;
    private final short paramMaxAmqCallee;

    public S7PlcConnection(InetAddress address, int rack, int slot, String params) {
        this(new TcpSocketChannelFactory(address, ISO_ON_TCP_PORT), rack, slot, params);

        logger.info("Configured S7cConnection with: host-name {}, rack {}, slot {}, pdu-size {}, max-amq-caller {}, " +
                "max-amq-callee {}", address.getHostAddress(), rack, slot,
            paramPduSize, paramMaxAmqCaller, paramMaxAmqCallee);
    }

    public S7PlcConnection(ChannelFactory channelFactory, int rack, int slot, String params) {
        super(channelFactory, true);

        this.rack = rack;
        this.slot = slot;

        int paramPduSize = 1024;
        short paramMaxAmqCaller = 8;
        short paramMaxAmqCallee = 8;

        if (!StringUtils.isEmpty(params)) {
            for (String param : params.split("&")) {
                String[] paramElements = param.split("=");
                String paramName = paramElements[0];
                if (paramElements.length == 2) {
                    String paramValue = paramElements[1];
                    switch (paramName) {
                        case "pdu-size":
                            paramPduSize = Integer.parseInt(paramValue);
                            break;
                        case "max-amq-caller":
                            paramMaxAmqCaller = Short.parseShort(paramValue);
                            break;
                        case "max-amq-callee":
                            paramMaxAmqCallee = Short.parseShort(paramValue);
                            break;
                        default:
                            logger.debug("Unknown parameter {} with value {}", paramName, paramValue);
                    }
                } else {
                    logger.debug("Unknown no-value parameter {}", paramName);
                }
            }
        }

        // IsoTP uses pre defined sizes. Find the smallest box,
        // that would be able to contain the requested pdu size.
        this.paramPduSize = TpduSize.valueForGivenSize(paramPduSize);
        this.paramMaxAmqCaller = paramMaxAmqCaller;
        this.paramMaxAmqCallee = paramMaxAmqCallee;
    }

    @Override
    protected ChannelHandler getChannelHandler(CompletableFuture<Void> sessionSetupCompleteFuture) {
        return new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) {
                // Build the protocol stack for communicating with the s7 protocol.
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                        if (evt instanceof S7ConnectionEvent &&
                            ((S7ConnectionEvent) evt).getState() == S7ConnectionState.SETUP_COMPLETE) {
                            sessionSetupCompleteFuture.complete(null);
                        } else {
                            super.userEventTriggered(ctx, evt);
                        }
                    }
                });
                pipeline.addLast(new IsoOnTcpProtocol());
                pipeline.addLast(new IsoTPProtocol((byte) rack, (byte) slot, paramPduSize));
                pipeline.addLast(new S7Protocol(paramMaxAmqCaller, paramMaxAmqCallee, (short) paramPduSize.getValue()));
                pipeline.addLast(new Plc4XS7Protocol());
            }
        };
    }

    @Override
    protected void sendChannelCreatedEvent() {
        // Send an event to the pipeline telling the Protocol filters what's going on.
        channel.pipeline().fireUserEventTriggered(new S7ConnectionEvent());
    }

    public int getRack() {
        return rack;
    }

    public int getSlot() {
        return slot;
    }

    public TpduSize getParamPduSize() {
        return paramPduSize;
    }

    public int getParamMaxAmqCaller() {
        return paramMaxAmqCaller;
    }

    public int getParamMaxAmqCallee() {
        return paramMaxAmqCallee;
    }

    @Override
    public void close() {
        if ((channel != null) && channel.isOpen()) {
            // Send the PLC a message that the connection is being closed.
            DisconnectRequestTpdu disconnectRequest = new DisconnectRequestTpdu(
                (short) 0x0000, (short) 0x000F, DisconnectReason.NORMAL, Collections.emptyList(),
                null);
            ChannelFuture sendDisconnectRequestFuture = channel.writeAndFlush(disconnectRequest);
            sendDisconnectRequestFuture.addListener((ChannelFutureListener) future -> {
                // Close the session itself.
                channel.closeFuture().await();
                channel.eventLoop().parent().shutdownGracefully();
            });
            sendDisconnectRequestFuture.awaitUninterruptibly();
        }
        super.close();
    }


    @Override
    public Address parseAddress(String addressString) throws PlcException {
        Matcher datablockAddressMatcher = S7_DATABLOCK_ADDRESS_PATTERN.matcher(addressString);
        if (datablockAddressMatcher.matches()) {
            int datablockNumber = Integer.parseInt(datablockAddressMatcher.group("blockNumber"));
            int datablockByteOffset = Integer.parseInt(datablockAddressMatcher.group("byteOffset"));
            return new S7DataBlockAddress((short) datablockNumber, (short) datablockByteOffset);
        }
        Matcher addressMatcher = S7_ADDRESS_PATTERN.matcher(addressString);
        if (!addressMatcher.matches()) {
            throw new PlcConnectionException(
                "Address string doesn't match the format '{area}/{offset}[/{bit-offset}]'");
        }
        MemoryArea memoryArea = MemoryArea.valueOf(addressMatcher.group("memoryArea"));
        int byteOffset = Integer.parseInt(addressMatcher.group("byteOffset"));
        String bitOffset = addressMatcher.group("bitOffset");
        if (bitOffset != null) {
            return new S7BitAddress(memoryArea, (short) byteOffset, Byte.valueOf(bitOffset));
        }
        return new S7Address(memoryArea, (short) byteOffset);
    }

    @Override
    public CompletableFuture<PlcReadResponse> read(PlcReadRequest readRequest) {
        CompletableFuture<PlcReadResponse> readFuture = new CompletableFuture<>();
        PlcRequestContainer<PlcReadRequest, PlcReadResponse> container =
            new PlcRequestContainer<>(readRequest, readFuture);
        channel.writeAndFlush(container);
        return readFuture;
    }

    @Override
    public CompletableFuture<PlcWriteResponse> write(PlcWriteRequest writeRequest) {
        CompletableFuture<PlcWriteResponse> writeFuture = new CompletableFuture<>();
        PlcRequestContainer<PlcWriteRequest, PlcWriteResponse> container =
            new PlcRequestContainer<>(writeRequest, writeFuture);
        channel.writeAndFlush(container);
        return writeFuture;
    }

}
