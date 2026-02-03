# Project README: gRPC Server for Encoded Circle Data

This project involves the implementation of a **gRPC server** in Java designed to handle and serve encoded geometric data (circles) stored in a database.

---

## 🛠 Project Components

The system is built using a client-server architecture facilitated by **Google Remote Procedure Call (gRPC)** and a relational database for persistent storage.

### 1. gRPC Service Definition (`circles.proto`)

* **Service Contract**: Defines the `GetCircles` method within the gRPC service.
* **Communication**: Specifies the request type (`Empty`) and the response type (`CircleResponse`) to standardize data exchange between the server and potential clients.

### 2. Server Implementation (`GrpcServer.java`)

* **Server Lifecycle**: Manages starting the server on a specified port and blocking the main thread to keep the service active until termination.
* **Service Registration**: Utilizes `ServerBuilder` to register the `CircleServiceImpl` which contains the actual business logic for retrieving circle data.
* **Feedback**: Prints terminal messages to confirm successful initialization and the active listening port.

### 3. Database Layer (H2 Database)

* **Data Source**: Uses an **H2 database** to store circle records.
* **Data Loading**: Requires importing pre-existing circle data from a binary file (`circles.bin`) into the database tables.

---

## 🚀 How to Use

### Setup & Build

1. **Project Initialization**: Create a new project with **Gradle** and configure the `build.gradle.kts` file with the required gRPC and Protobuf dependencies.
2. **Protocol Buffers**:
* Create a `proto` directory in `src/main/java/`.
* Place `circles.proto` in this directory and add the `rpc GetCircles` statement to the service section.


3. **Code Generation**: Use the IntelliJ **Gradle tool window** to run the `build` task, which generates the Java classes necessary for the gRPC service.

### Running the Server

1. **Database Preparation**: Ensure the H2 database is active and populated with the data from the binary file.
2. **Execute**: Run the `main` method in `GrpcServer.java`.
3. **Verification**: Confirm the "server started" message appears in the console.

### Testing

* Run the unit tests located in `GrpcServerTest.java` to verify that the server correctly handles requests and returns the expected circle data.
