package by.danefka.internship_vk.gRPC;


import by.danefka.internship_vk.model.Kv;
import by.danefka.internship_vk.service.KvService;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kv.KvOuterClass;
import kv.KvServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class KvGrpcService extends KvServiceGrpc.KvServiceImplBase {

    private final KvService kvService;

    @Override
    public void get(KvOuterClass.GetRequest request,
                    StreamObserver<KvOuterClass.GetResponse> responseObserver) {
        try {
            byte[] result = kvService.get(request.getKey());

            KvOuterClass.GetResponse.Builder builder = KvOuterClass.GetResponse.newBuilder();

            if (result != null) {
                builder.setValue(new String(result, StandardCharsets.UTF_8));
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Key not found: " + request.getKey())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void put(KvOuterClass.PutRequest request, StreamObserver<Empty> responseObserver) {
        String value = request.getKv().hasValue() ? request.getKv().getValue() : null;

        kvService.put(request.getKv().getKey(), value);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(KvOuterClass.DeleteRequest request, StreamObserver<Empty> responseObserver) {
        kvService.delete(request.getKey());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void range(KvOuterClass.RangeRequest request, StreamObserver<KvOuterClass.Kv> responseObserver) {
        List<Kv> kvList = kvService.range(request.getKeyFrom(), request.getKeyTo());

        for (Kv kv : kvList) {
            KvOuterClass.Kv.Builder builder = KvOuterClass.Kv.newBuilder();
            builder.setKey(kv.getKey());

            if (kv.getValue() != null) {
                builder.setValue(new String(kv.getValue()));
            }


            responseObserver.onNext(builder.build());
        }

        responseObserver.onCompleted();
    }

    @Override
    public void count(Empty request, StreamObserver<KvOuterClass.CountResponse> responseObserver) {
        Integer count = kvService.count();

        KvOuterClass.CountResponse response = KvOuterClass.CountResponse.newBuilder()
                .setCount(count)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
