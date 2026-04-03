# internship_vk

Java gRPC key-value service built as a test assignment for the VK Internship.

The service provides basic operations such as put, get, delete, range and count. Data is stored in Tarantool and the system is designed to handle up to 5,000,000 records while also supporting null values in the value field.

## Stack

Java (Spring Boot), gRPC, Tarantool, Docker, Docker Compose, Gradle.

## Build and Run

Build the project:
```bash
./gradlew build
```
Run the application with Docker:

```bash
docker-compose up --build
```

After startup, the gRPC server is available at localhost:9090

## Example Requests

You can test the service using grpcurl or any gRPC client.

Put:
```bash
grpcurl -plaintext -d '{ "key": "test", "value": "hello" }' localhost:9090 KvService/Put
```
Get:
```bash
grpcurl -plaintext -d '{ "key": "test" }' localhost:9090 KvService/Get
```

Delete:
```bash
grpcurl -plaintext -d '{ "key": "test" }' localhost:9090 KvService/Delete
```
Count:
```bash
grpcurl -plaintext localhost:9090 KvService/Count
```
Range:
```bash
grpcurl -plaintext -d '{ "from": "a", "to": "z" }' localhost:9090 KvService/Range
```
