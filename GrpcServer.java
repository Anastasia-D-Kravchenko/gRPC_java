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
//TODO: specify variable DB_URL based on H2 database URL
    private static final String DB_URL = "jdbc:h2:C:/...";

    public static void main(String[] args) throws IOException, InterruptedException {
//TODO: implement program based on tutorial instructions

    }

    static class CircleServiceImpl extends CircleServiceGrpc.CircleServiceImplBase {
        @Override
        public void getCircles(Empty request, StreamObserver<CircleResponse> responseObserver) {
            System.out.println("Request received. Fetching from DB...");
            
            CircleResponse.Builder responseBuilder = CircleResponse.newBuilder();

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement("SELECT x, y, r, g, b FROM circles");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    responseBuilder.addCircles(CircleDTO.newBuilder()
                        .setX(rs.getInt("x"))
                        .setY(rs.getInt("y"))
                        .setR(rs.getInt("r"))
                        .setG(rs.getInt("g"))
                        .setB(rs.getInt("b"))
                        .build()
                    );
                }
            } catch (SQLException e) {
                System.err.println("DB Error: " + e.getMessage());
                responseObserver.onError(e);
                return;
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            System.out.println("Data sent to client.");
        }
    }
}