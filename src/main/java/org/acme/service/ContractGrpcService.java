package org.acme.service;

import io.quarkus.grpc.GrpcService;

import io.quarkus.logging.Log;
import org.acme.GetContractRequest;
import org.acme.SaveContractRequest;
import org.hibernate.reactive.mutiny.Mutiny;
import io.smallrye.mutiny.Uni;
import org.acme.ContractGrpc;
import org.acme.ContractReply;
import org.acme.model.Contract;
import org.jboss.logging.Logger;

import javax.inject.Inject;

import static io.quarkus.logging.Log.info;
import static io.quarkus.logging.Log.log;

@GrpcService
public class ContractGrpcService implements ContractGrpc {

    @Inject
    Mutiny.SessionFactory sf;

    @Override
    public Uni<ContractReply> create(SaveContractRequest request) {
        Contract contract = new Contract();
        contract.description = request.getDescription();

        return sf.withTransaction((s,t) -> s.persist(contract))
                .onItem().invoke(item -> log(Logger.Level.INFO, "Item Id: " + contract.id))
                .replaceWith(() -> ContractReply.newBuilder().setId(contract.id).setDescription(contract.description).build())
                .onFailure().invoke(throwable -> log(Logger.Level.ERROR, "Failed with " + throwable.getMessage()));

    }

    @Override
    public Uni<ContractReply> getSingle(GetContractRequest request) {
        return sf.withTransaction((session, transaction) -> session.find(Contract.class, request.getId()))
                .onItem().transform(contract -> ContractReply.newBuilder().setId(contract.id).setDescription(contract.description).build());
    }

}
