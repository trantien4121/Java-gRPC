package greeting.client;

import com.proto.greeting.GreetingRequest;
import com.proto.greeting.GreetingResponse;
import com.proto.greeting.GreetingServiceGrpc;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClientTls {

    private static void doGreet(ManagedChannel channel){
        System.out.println("Enter doGreet");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.greet(GreetingRequest.newBuilder().setFirstName("Clement").build());

        System.out.println("Greeting: " + response.getResult());
    }


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Need one argument to work");
            return;
        }

        ChannelCredentials creds = TlsChannelCredentials.newBuilder().trustManager(
                new File("ssl/ca.crt")
        ).build();
        ManagedChannel chanel = Grpc.newChannelBuilderForAddress("localhost", 50051, creds).build();


        switch (args[0]) {
            case "greet": doGreet(chanel); break;
            default:
                System.out.println("Keyword invalid: " + args[0]);
        }

        System.out.println("Shutting down");
        chanel.shutdown();
    }
}
