package org.corfudb.integration;

import lombok.extern.slf4j.Slf4j;
import org.corfudb.protocols.wireprotocol.orchestrator.AddNodeResponse;
import org.corfudb.protocols.wireprotocol.orchestrator.OrchestratorResponse;
import org.corfudb.protocols.wireprotocol.orchestrator.QueryResponse;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.MultiCheckpointWriter;
import org.corfudb.runtime.clients.ManagementClient;
import org.corfudb.runtime.collections.SMRMap;
import org.corfudb.runtime.exceptions.WrongEpochException;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Maithem on 12/1/17.
 */
@Slf4j
public class AddNodeIT extends AbstractIT {

    final String host = "localhost";

    final int maxTries = 5;

    final int sleepTime = 1000;

    String getConnectionString(int port) {
        return host + ":" + port;
    }

    @Test
    public void AddNodeTest() throws Exception {
        final String host = "localhost";
        final String streamName = "s1";
        final int n1Port = 9000;

        // Start node one and populate it with data
        new CorfuServerRunner()
                .setHost(host)
                .setPort(n1Port)
                .setSingle(true)
                .runServer();

        CorfuRuntime n1Rt = new CorfuRuntime(getConnectionString(n1Port)).connect();

        SMRMap<String, String> map = n1Rt.getObjectsView()
                .build()
                .setType(SMRMap.class)
                .setStreamName(streamName)
                .open();

        final int numEntries = 12_000;
        for (int x = 0; x < numEntries; x++) {
            map.put(String.valueOf(x), String.valueOf(x));
        }

        // Add a second node
        final int n2Port = 9001;
        new CorfuServerRunner()
                .setHost(host)
                .setPort(n2Port)
                .runServer();

        ManagementClient mgmt = n1Rt.getRouter(getConnectionString(n1Port))
                .getClient(ManagementClient.class);

        AddNodeResponse resp = mgmt.addNodeRequest(getConnectionString(n2Port));

        assertThat(resp.getWorkflowId()).isNotNull();

        waitForWorkflow(resp.getWorkflowId(), n1Rt, n1Port);

        n1Rt.invalidateLayout();
        final int clusterSizeN2 = 2;
        assertThat(n1Rt.getLayoutView().getLayout().getAllServers().size()).isEqualTo(clusterSizeN2);

        MultiCheckpointWriter mcw = new MultiCheckpointWriter();
        mcw.addMap(map);

        long prefix = mcw.appendCheckpoints(n1Rt, "Maithem");

        n1Rt.getAddressSpaceView().prefixTrim(prefix - 1);

        n1Rt.getAddressSpaceView().invalidateClientCache();
        n1Rt.getAddressSpaceView().invalidateServerCaches();
        n1Rt.getAddressSpaceView().gc();

        // Add a 3rd node after compaction

        final int n3Port = 9002;
        new CorfuServerRunner()
                .setHost(host)
                .setPort(n3Port)
                .runServer();

        AddNodeResponse resp2 = mgmt.addNodeRequest(getConnectionString(n3Port));
        assertThat(resp2.getWorkflowId()).isNotNull();

        waitForWorkflow(resp2.getWorkflowId(), n1Rt, n1Port);

        // Verify that the third node has been added and data can be read back
        n1Rt.invalidateLayout();
        final int clusterSizeN3 = 3;
        assertThat(n1Rt.getLayoutView().getLayout().getAllServers().size()).isEqualTo(clusterSizeN3);
        for (int x = 0; x < numEntries; x++) {
            String v = map.get(String.valueOf(x));
            assertThat(v).isEqualTo(String.valueOf(x));
        }
    }

    void waitForWorkflow(UUID id, CorfuRuntime rt, int port) throws Exception {
        ManagementClient mgmt = rt.getRouter(getConnectionString(port))
                .getClient(ManagementClient.class);
        for (int x = 0; x < maxTries; x++) {
            try {
                if (mgmt.queryRequest(id).isActive()) {
                    Thread.sleep(sleepTime);
                } else {
                    break;
                }
            } catch (Exception e) {
                rt.invalidateLayout();
                Thread.sleep(sleepTime);
            }
        }
    }
}
