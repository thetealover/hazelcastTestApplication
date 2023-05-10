In the scope of the testing the idea is to create multiple docker containers of the application, place a load balancer
in front of those, and pass requests for response observation. The chosen load balancer is the HAProxy.

### Attention

You'll need Docker installed

### Instructions for running

Clean, code format and build the project JAR

```
./gradlew clean spotlessApply build 
```

Build the project Docker image

```
docker build --tag {applicationImageTagName} .
```

Build the HAProxy Docker image

```
docker build --tag {haproxyImageTagName} -f haproxy.Dockerfile .
```

For testing with instances belonging to the same network, let's create the network with the following:

```
docker network create -d bridge {networkName}
```

Running application instances

```
docker run -d --name hazelcast-t-1 --network {networkName} {applicationImageTagName}
docker run -d --name hazelcast-t-2 --network {networkName} {applicationImageTagName}
docker run -d --name hazelcast-t-3 --network {networkName} {applicationImageTagName}
```

* Make sure to name the containers of the application with the names specified in the HAProxy configuration file.

Running the HAProxy load balancer

```
docker run -d -p 80:80 --network {networkName} --name haproxy {haproxyImageTagName}
```

### Testing

GET http://localhost/cached , http://localhost/cached?msg={optionalMessage} (ICache implementation. The default cache
ttl is 10s)

GET http://localhost/shared , http://localhost/shared?msg={optionalMessage} (shared IMap implementation)

The expected response body is the following:

```
{
  "timestamp": "2023-05-10T12:12:04.7649304Z",
  "endpointCallCount": 1,
  "message": null
}
```

In the result of the cache and shared collection usage, the ``endpointCallCount`` value is shared between the instances
of the test application