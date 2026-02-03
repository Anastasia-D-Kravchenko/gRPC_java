package pj;

import grpc.circles.CircleDTO;
import grpc.circles.CircleResponse;
import grpc.circles.CircleServiceGrpc;
import grpc.circles.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrpcServer {

    private static final int PORT = 50051;
    // H2 database URL (adjust path or use mem/file mode depending on your setup from exercise 10-01)
    private static final String DB_URL = "jdbc:h2:~/circlesdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create and configure the gRPC server
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new CircleServiceImpl())
                .build();

        // Inform the user that listening has started
        System.out.println("gRPC server started. Listening on port " + PORT);

        // Start the server
        server.start();

        // Block until shutdown
        server.awaitTermination();
    }

    static class CircleServiceImpl extends CircleServiceGrpc.CircleServiceImplBase {
        @Override
        public void getCircles(Empty request, StreamObserver<CircleResponse> responseObserver) {
            CircleResponse.Builder responseBuilder = CircleResponse.newBuilder();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT x, y, r, g, b FROM circles");
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    CircleDTO circle = CircleDTO.newBuilder()
                            .setX(rs.getInt("x"))
                            .setY(rs.getInt("y"))
                            .setR(rs.getInt("r"))
                            .setG(rs.getInt("g"))
                            .setB(rs.getInt("b"))
                            .build();
                    responseBuilder.addCircles(circle);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Send response back to client
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }
}
