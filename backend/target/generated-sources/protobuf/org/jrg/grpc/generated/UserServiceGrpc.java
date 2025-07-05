package org.jrg.grpc.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * User service definition
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "org.jrg.grpc.UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetUserRequest,
      org.jrg.grpc.generated.User> getGetUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUser",
      requestType = org.jrg.grpc.generated.GetUserRequest.class,
      responseType = org.jrg.grpc.generated.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetUserRequest,
      org.jrg.grpc.generated.User> getGetUserMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetUserRequest, org.jrg.grpc.generated.User> getGetUserMethod;
    if ((getGetUserMethod = UserServiceGrpc.getGetUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetUserMethod = UserServiceGrpc.getGetUserMethod) == null) {
          UserServiceGrpc.getGetUserMethod = getGetUserMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.GetUserRequest, org.jrg.grpc.generated.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.GetUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.User.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("GetUser"))
              .build();
        }
      }
    }
    return getGetUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetAllUsersRequest,
      org.jrg.grpc.generated.GetAllUsersResponse> getGetAllUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllUsers",
      requestType = org.jrg.grpc.generated.GetAllUsersRequest.class,
      responseType = org.jrg.grpc.generated.GetAllUsersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetAllUsersRequest,
      org.jrg.grpc.generated.GetAllUsersResponse> getGetAllUsersMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.GetAllUsersRequest, org.jrg.grpc.generated.GetAllUsersResponse> getGetAllUsersMethod;
    if ((getGetAllUsersMethod = UserServiceGrpc.getGetAllUsersMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetAllUsersMethod = UserServiceGrpc.getGetAllUsersMethod) == null) {
          UserServiceGrpc.getGetAllUsersMethod = getGetAllUsersMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.GetAllUsersRequest, org.jrg.grpc.generated.GetAllUsersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.GetAllUsersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.GetAllUsersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("GetAllUsers"))
              .build();
        }
      }
    }
    return getGetAllUsersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.CreateUserRequest,
      org.jrg.grpc.generated.User> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUser",
      requestType = org.jrg.grpc.generated.CreateUserRequest.class,
      responseType = org.jrg.grpc.generated.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.CreateUserRequest,
      org.jrg.grpc.generated.User> getCreateUserMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.CreateUserRequest, org.jrg.grpc.generated.User> getCreateUserMethod;
    if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
          UserServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.CreateUserRequest, org.jrg.grpc.generated.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.CreateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.User.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("CreateUser"))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.UpdateUserRequest,
      org.jrg.grpc.generated.User> getUpdateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUser",
      requestType = org.jrg.grpc.generated.UpdateUserRequest.class,
      responseType = org.jrg.grpc.generated.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.UpdateUserRequest,
      org.jrg.grpc.generated.User> getUpdateUserMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.UpdateUserRequest, org.jrg.grpc.generated.User> getUpdateUserMethod;
    if ((getUpdateUserMethod = UserServiceGrpc.getUpdateUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getUpdateUserMethod = UserServiceGrpc.getUpdateUserMethod) == null) {
          UserServiceGrpc.getUpdateUserMethod = getUpdateUserMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.UpdateUserRequest, org.jrg.grpc.generated.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.UpdateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.User.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("UpdateUser"))
              .build();
        }
      }
    }
    return getUpdateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.DeleteUserRequest,
      org.jrg.grpc.generated.DeleteUserResponse> getDeleteUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteUser",
      requestType = org.jrg.grpc.generated.DeleteUserRequest.class,
      responseType = org.jrg.grpc.generated.DeleteUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.DeleteUserRequest,
      org.jrg.grpc.generated.DeleteUserResponse> getDeleteUserMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.DeleteUserRequest, org.jrg.grpc.generated.DeleteUserResponse> getDeleteUserMethod;
    if ((getDeleteUserMethod = UserServiceGrpc.getDeleteUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getDeleteUserMethod = UserServiceGrpc.getDeleteUserMethod) == null) {
          UserServiceGrpc.getDeleteUserMethod = getDeleteUserMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.DeleteUserRequest, org.jrg.grpc.generated.DeleteUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.DeleteUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.DeleteUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("DeleteUser"))
              .build();
        }
      }
    }
    return getDeleteUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.jrg.grpc.generated.StreamUsersRequest,
      org.jrg.grpc.generated.User> getStreamUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamUsers",
      requestType = org.jrg.grpc.generated.StreamUsersRequest.class,
      responseType = org.jrg.grpc.generated.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.jrg.grpc.generated.StreamUsersRequest,
      org.jrg.grpc.generated.User> getStreamUsersMethod() {
    io.grpc.MethodDescriptor<org.jrg.grpc.generated.StreamUsersRequest, org.jrg.grpc.generated.User> getStreamUsersMethod;
    if ((getStreamUsersMethod = UserServiceGrpc.getStreamUsersMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getStreamUsersMethod = UserServiceGrpc.getStreamUsersMethod) == null) {
          UserServiceGrpc.getStreamUsersMethod = getStreamUsersMethod =
              io.grpc.MethodDescriptor.<org.jrg.grpc.generated.StreamUsersRequest, org.jrg.grpc.generated.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.StreamUsersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jrg.grpc.generated.User.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("StreamUsers"))
              .build();
        }
      }
    }
    return getStreamUsersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static UserServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingV2Stub>() {
        @java.lang.Override
        public UserServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return UserServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * User service definition
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Get a user by ID
     * </pre>
     */
    default void getUser(org.jrg.grpc.generated.GetUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserMethod(), responseObserver);
    }

    /**
     * <pre>
     * Get all users
     * </pre>
     */
    default void getAllUsers(org.jrg.grpc.generated.GetAllUsersRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.GetAllUsersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllUsersMethod(), responseObserver);
    }

    /**
     * <pre>
     * Create a new user
     * </pre>
     */
    default void createUser(org.jrg.grpc.generated.CreateUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }

    /**
     * <pre>
     * Update an existing user
     * </pre>
     */
    default void updateUser(org.jrg.grpc.generated.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateUserMethod(), responseObserver);
    }

    /**
     * <pre>
     * Delete a user
     * </pre>
     */
    default void deleteUser(org.jrg.grpc.generated.DeleteUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.DeleteUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteUserMethod(), responseObserver);
    }

    /**
     * <pre>
     * Stream users (server streaming)
     * </pre>
     */
    default void streamUsers(org.jrg.grpc.generated.StreamUsersRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStreamUsersMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserService.
   * <pre>
   * User service definition
   * </pre>
   */
  public static abstract class UserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserService.
   * <pre>
   * User service definition
   * </pre>
   */
  public static final class UserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Get a user by ID
     * </pre>
     */
    public void getUser(org.jrg.grpc.generated.GetUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get all users
     * </pre>
     */
    public void getAllUsers(org.jrg.grpc.generated.GetAllUsersRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.GetAllUsersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllUsersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Create a new user
     * </pre>
     */
    public void createUser(org.jrg.grpc.generated.CreateUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Update an existing user
     * </pre>
     */
    public void updateUser(org.jrg.grpc.generated.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Delete a user
     * </pre>
     */
    public void deleteUser(org.jrg.grpc.generated.DeleteUserRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.DeleteUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Stream users (server streaming)
     * </pre>
     */
    public void streamUsers(org.jrg.grpc.generated.StreamUsersRequest request,
        io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getStreamUsersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserService.
   * <pre>
   * User service definition
   * </pre>
   */
  public static final class UserServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingV2Stub> {
    private UserServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Get a user by ID
     * </pre>
     */
    public org.jrg.grpc.generated.User getUser(org.jrg.grpc.generated.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get all users
     * </pre>
     */
    public org.jrg.grpc.generated.GetAllUsersResponse getAllUsers(org.jrg.grpc.generated.GetAllUsersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllUsersMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Create a new user
     * </pre>
     */
    public org.jrg.grpc.generated.User createUser(org.jrg.grpc.generated.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update an existing user
     * </pre>
     */
    public org.jrg.grpc.generated.User updateUser(org.jrg.grpc.generated.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Delete a user
     * </pre>
     */
    public org.jrg.grpc.generated.DeleteUserResponse deleteUser(org.jrg.grpc.generated.DeleteUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Stream users (server streaming)
     * </pre>
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, org.jrg.grpc.generated.User>
        streamUsers(org.jrg.grpc.generated.StreamUsersRequest request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getStreamUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service UserService.
   * <pre>
   * User service definition
   * </pre>
   */
  public static final class UserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Get a user by ID
     * </pre>
     */
    public org.jrg.grpc.generated.User getUser(org.jrg.grpc.generated.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get all users
     * </pre>
     */
    public org.jrg.grpc.generated.GetAllUsersResponse getAllUsers(org.jrg.grpc.generated.GetAllUsersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllUsersMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Create a new user
     * </pre>
     */
    public org.jrg.grpc.generated.User createUser(org.jrg.grpc.generated.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update an existing user
     * </pre>
     */
    public org.jrg.grpc.generated.User updateUser(org.jrg.grpc.generated.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Delete a user
     * </pre>
     */
    public org.jrg.grpc.generated.DeleteUserResponse deleteUser(org.jrg.grpc.generated.DeleteUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUserMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Stream users (server streaming)
     * </pre>
     */
    public java.util.Iterator<org.jrg.grpc.generated.User> streamUsers(
        org.jrg.grpc.generated.StreamUsersRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getStreamUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserService.
   * <pre>
   * User service definition
   * </pre>
   */
  public static final class UserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Get a user by ID
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jrg.grpc.generated.User> getUser(
        org.jrg.grpc.generated.GetUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Get all users
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jrg.grpc.generated.GetAllUsersResponse> getAllUsers(
        org.jrg.grpc.generated.GetAllUsersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllUsersMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Create a new user
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jrg.grpc.generated.User> createUser(
        org.jrg.grpc.generated.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Update an existing user
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jrg.grpc.generated.User> updateUser(
        org.jrg.grpc.generated.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateUserMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Delete a user
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jrg.grpc.generated.DeleteUserResponse> deleteUser(
        org.jrg.grpc.generated.DeleteUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_USER = 0;
  private static final int METHODID_GET_ALL_USERS = 1;
  private static final int METHODID_CREATE_USER = 2;
  private static final int METHODID_UPDATE_USER = 3;
  private static final int METHODID_DELETE_USER = 4;
  private static final int METHODID_STREAM_USERS = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_USER:
          serviceImpl.getUser((org.jrg.grpc.generated.GetUserRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User>) responseObserver);
          break;
        case METHODID_GET_ALL_USERS:
          serviceImpl.getAllUsers((org.jrg.grpc.generated.GetAllUsersRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.GetAllUsersResponse>) responseObserver);
          break;
        case METHODID_CREATE_USER:
          serviceImpl.createUser((org.jrg.grpc.generated.CreateUserRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User>) responseObserver);
          break;
        case METHODID_UPDATE_USER:
          serviceImpl.updateUser((org.jrg.grpc.generated.UpdateUserRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User>) responseObserver);
          break;
        case METHODID_DELETE_USER:
          serviceImpl.deleteUser((org.jrg.grpc.generated.DeleteUserRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.DeleteUserResponse>) responseObserver);
          break;
        case METHODID_STREAM_USERS:
          serviceImpl.streamUsers((org.jrg.grpc.generated.StreamUsersRequest) request,
              (io.grpc.stub.StreamObserver<org.jrg.grpc.generated.User>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.jrg.grpc.generated.GetUserRequest,
              org.jrg.grpc.generated.User>(
                service, METHODID_GET_USER)))
        .addMethod(
          getGetAllUsersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.jrg.grpc.generated.GetAllUsersRequest,
              org.jrg.grpc.generated.GetAllUsersResponse>(
                service, METHODID_GET_ALL_USERS)))
        .addMethod(
          getCreateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.jrg.grpc.generated.CreateUserRequest,
              org.jrg.grpc.generated.User>(
                service, METHODID_CREATE_USER)))
        .addMethod(
          getUpdateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.jrg.grpc.generated.UpdateUserRequest,
              org.jrg.grpc.generated.User>(
                service, METHODID_UPDATE_USER)))
        .addMethod(
          getDeleteUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.jrg.grpc.generated.DeleteUserRequest,
              org.jrg.grpc.generated.DeleteUserResponse>(
                service, METHODID_DELETE_USER)))
        .addMethod(
          getStreamUsersMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              org.jrg.grpc.generated.StreamUsersRequest,
              org.jrg.grpc.generated.User>(
                service, METHODID_STREAM_USERS)))
        .build();
  }

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.jrg.grpc.generated.UserServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getGetUserMethod())
              .addMethod(getGetAllUsersMethod())
              .addMethod(getCreateUserMethod())
              .addMethod(getUpdateUserMethod())
              .addMethod(getDeleteUserMethod())
              .addMethod(getStreamUsersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
