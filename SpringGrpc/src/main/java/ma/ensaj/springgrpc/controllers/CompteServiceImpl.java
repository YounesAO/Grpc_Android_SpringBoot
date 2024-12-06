package ma.ensaj.springgrpc.controllers;

import io.grpc.stub.StreamObserver;
import ma.ensaj.springgrpc.services.CompteService;
import ma.ensaj.springgrpc.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.stream.Collectors;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {
    private final CompteService compteService;

    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }

    @Override
    public void allComptes(GetAllComptesRequest request,
                           StreamObserver<GetAllComptesResponse> responseObserver) {
        var comptes = compteService.findAllComptes().stream()
                .map(compte -> Compte.newBuilder()
                        .setId(compte.getId())
                        .setSolde(compte.getSolde())
                        .setDateCreation(compte.getDateCreation())
                        .setType(TypeCompte.valueOf(compte.getType()))
                        .build())
                .collect(Collectors.toList());

        GetAllComptesResponse response = GetAllComptesResponse.newBuilder()
                .addAllComptes(comptes)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request,
                           StreamObserver<GetCompteByIdResponse> responseObserver) {
        var compte = compteService.findCompteById(request.getId());

        if (compte != null) {
            var grpcCompte = Compte.newBuilder()
                    .setId(compte.getId())
                    .setSolde(compte.getSolde())
                    .setDateCreation(compte.getDateCreation())
                    .setType(TypeCompte.valueOf(compte.getType()))
                    .build();

            GetCompteByIdResponse response = GetCompteByIdResponse.newBuilder()
                    .setCompte(grpcCompte)
                    .build();

            responseObserver.onNext(response);
        } else {
            // Handle not found case
            responseObserver.onNext(GetCompteByIdResponse.getDefaultInstance());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request,
                           StreamObserver<GetTotalSoldeResponse> responseObserver) {
        var comptes = compteService.findAllComptes();

        int count = comptes.size();
        float sum = (float) comptes.stream()
                .mapToDouble(compte -> compte.getSolde())
                .sum();
        float average = count > 0 ? sum / count : 0;

        var stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        GetTotalSoldeResponse response = GetTotalSoldeResponse.newBuilder()
                .setStats(stats)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request,
                           StreamObserver<SaveCompteResponse> responseObserver) {
        var compteReq = request.getCompte();
        var compte = new ma.ensaj.springgrpc.entities.Compte();
        compte.setSolde(compteReq.getSolde());
        compte.setDateCreation(compteReq.getDateCreation());
        compte.setType(compteReq.getType().name());

        var savedCompte = compteService.saveCompte(compte);
        var grpcCompte = Compte.newBuilder()
                .setId(savedCompte.getId())
                .setSolde(savedCompte.getSolde())
                .setDateCreation(savedCompte.getDateCreation())
                .setType(TypeCompte.valueOf(savedCompte.getType()))
                .build();

        SaveCompteResponse response = SaveCompteResponse.newBuilder()
                .setCompte(grpcCompte)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}