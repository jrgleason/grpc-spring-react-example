package org.jrg.grpc.service;

import java.util.List;
import java.util.Optional;

import org.jrg.grpc.generated.CreateUserRequest;
import org.jrg.grpc.generated.DeleteUserRequest;
import org.jrg.grpc.generated.DeleteUserResponse;
import org.jrg.grpc.generated.GetAllUsersRequest;
import org.jrg.grpc.generated.GetAllUsersResponse;
import org.jrg.grpc.generated.GetUserRequest;
import org.jrg.grpc.generated.StreamUsersRequest;
import org.jrg.grpc.generated.UpdateUserRequest;
import org.jrg.grpc.generated.User;
import org.jrg.grpc.generated.UserServiceGrpc;
import org.jrg.grpc.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;

import io.grpc.stub.StreamObserver;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserDataService userDataService;

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        Optional<UserEntity> userEntity = userDataService.getUserById(request.getId());
        
        if (userEntity.isPresent()) {
            User user = convertToProtoUser(userEntity.get());
            responseObserver.onNext(user);
        } else {
            responseObserver.onError(new RuntimeException("User not found with id: " + request.getId()));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(GetAllUsersRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        List<UserEntity> userEntities = userDataService.getAllUsers();
        
        GetAllUsersResponse.Builder responseBuilder = GetAllUsersResponse.newBuilder();
        responseBuilder.setTotalCount(userEntities.size());
        
        for (UserEntity userEntity : userEntities) {
            responseBuilder.addUsers(convertToProtoUser(userEntity));
        }
        
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userDataService.createUser(
            request.getName(),
            request.getEmail(),
            request.getRole()
        );
        
        User user = convertToProtoUser(userEntity);
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userDataService.updateUser(
            request.getId(),
            request.getName(),
            request.getEmail(),
            request.getRole()
        );
        
        if (userEntity != null) {
            User user = convertToProtoUser(userEntity);
            responseObserver.onNext(user);
        } else {
            responseObserver.onError(new RuntimeException("User not found with id: " + request.getId()));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<DeleteUserResponse> responseObserver) {
        boolean deleted = userDataService.deleteUser(request.getId());
        
        DeleteUserResponse response = DeleteUserResponse.newBuilder()
            .setSuccess(deleted)
            .setMessage(deleted ? "User deleted successfully" : "User not found")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void streamUsers(StreamUsersRequest request, StreamObserver<User> responseObserver) {
        List<UserEntity> userEntities = userDataService.getAllUsers();
        
        int batchSize = request.getBatchSize() > 0 ? request.getBatchSize() : 1;
        
        try {
            for (int i = 0; i < userEntities.size(); i += batchSize) {
                int end = Math.min(i + batchSize, userEntities.size());
                for (int j = i; j < end; j++) {
                    User user = convertToProtoUser(userEntities.get(j));
                    responseObserver.onNext(user);
                }
                // Simulate streaming delay
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(e);
            return;
        }
        
        responseObserver.onCompleted();
    }

    private User convertToProtoUser(UserEntity userEntity) {
        return User.newBuilder()
            .setId(userEntity.getId())
            .setName(userEntity.getName())
            .setEmail(userEntity.getEmail())
            .setRole(userEntity.getRole())
            .setCreatedAt(userEntity.getCreatedAt().getEpochSecond())
            .build();
    }
}
