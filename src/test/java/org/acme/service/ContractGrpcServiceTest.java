package org.acme.service;

import java.time.Duration;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import org.acme.ContractGrpc;
import org.acme.ContractReply;
import org.acme.SaveContractRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ContractGrpcServiceTest {

    @GrpcClient
    ContractGrpc contractGrpc;

    @Test
    public void testSaveContract() {
        ContractReply reply = contractGrpc
                .create(SaveContractRequest.newBuilder().setDescription("Neo").build()).await().atMost(Duration.ofSeconds(5));
        assertNotEquals(0, reply.getId());
        assertEquals("Neo", reply.getDescription());
    }

}
