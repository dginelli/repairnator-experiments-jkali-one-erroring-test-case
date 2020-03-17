package org.corfudb.protocols.wireprotocol.orchestrator;

import lombok.Getter;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

import static org.corfudb.protocols.wireprotocol.orchestrator.OrchestratorRequestType.REMOVE_NODE;

/**
 * Request to remove a node from the cluster.
 * @author Maithem
 */
public class RemoveNodeRequest implements CreateRequest {

    /**
     * The endpoint to be removed
     */
    @Getter
    public String endpoint;

    public RemoveNodeRequest(@Nonnull String endpoint) {
        this.endpoint = endpoint;
    }

    public RemoveNodeRequest(byte[] buf) {
        endpoint = new String(buf, StandardCharsets.UTF_8);
    }

    @Override
    public OrchestratorRequestType getType() {
        return REMOVE_NODE;
    }

    @Override
    public byte[] getSerialized() {
        return endpoint.getBytes(StandardCharsets.UTF_8);
    }
}
