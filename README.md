# Overview

This workshop is a word cloud application. The application uses Kafka to receive text from external sources.
The word cloud application shows the data in two forms - real-time and historical.
The real-time dashboard shows top words by that are currently active.
The historical dashboard shows top words in last 30 min, 60 min, and so on.
The dashboard needs great user experience where speed of data access and data aggregations becomes important.
For this purpose we will use Gemfire/PCC distributed in-memory data grid.
The streaming applications consuming from Kafka topic use Kafka's native streaming API built on top of
Spring Cloud Stream binders.

Here is the topology of the application
![Words Cloud application](architecture.png)

## Key components

### Kafka topics
1. Lines - unprocessed text
1. Counts - aggregate word count
1. Windowed counts - word count based on a tumbling window

### Apps
1. [Cache Server](./cache-server) - creates a cache server instance to load/store word count and windowed word count stream data. 
1. [Lines Generator](./lines-generator) - sends random text lines to “lines” topic, reads data from “counts” topic, reads data from “windowed counts” topic
1. [Word Count Stream](./wordcount-stream) - Uses Kstream to read from “lines” topic, performs stateful transformations i.e. count words, send output to “counts” topic on Kafka and stores the output in a PCC region “Words_Count”
1. [Windowed Word Count Stream](./windowed-wordcount-stream) - Uses Kstream to read from “lines” topic, performs stateful transformations using tumbling window i.e. windowed count words, send output to “windowed counts” topic on Kafka and stores the output in a PCC region “Words_Count_Windowed”
1. [Web UI](./web-ui) - UI dashboard that uses Word cloud API

### Gemfire/PCC regions
1. Words_Count - partitioned cache designed to hold real-time aggregate word count, evicts word entries if no write/update in 60 sec
1. Words_Count_Windowed - partitioned cache with LRU based eviction policy designed to hold windowed word count, eviction triggered at 70% heap usage

## Steps to start the apps
For Kafka we will use docker-compose to start a dockerized Kafka on your laptop.
We have included the the required files in [kafka-local](./kafka-local) folder in this repo.
Of course, you will need to have Docker already installed on your laptop.
We also have included instructions to setup Kafka on a Kubernetes cluster using Helm charts.

The rest of the demo components are spring boot apps that can be run locally.

#### Git clone this repo
```
cd pivotal-confluent-demo
```

#### Setup local kafka
```
cd kafka-local
./stop-docker.sh
docker-compose up -d
```
#### Start gemfire cache server instance [cache-server](cache-server)
```
cd cache-server
./mvnw clean package
java -jar target/cache-server-1.0.jar
```
#### Start [lines-generator](lines-generator) app, either from your favorite IDE or
```
cd lines-generator
./mvnw clean package
java -jar target/lines-generator-1.0.jar
```

#### Start [words count stream](wordcount-stream) app, either from your favorite IDE or
```
cd wordcount-stream
./mvnw clean package
java -jar target/wordcount-stream-1.0.jar
```

#### Start [windowed words count stream](windowed-wordcount-stream) app, either from your favorite IDE or
```
cd windowed-wordcount-stream
./mvnw clean package
java -jar target/windowed-wordcount-stream-1.0.jar
```

#### Start the [web UI](web-ui) app, either from your favorite IDE or
```
cd web-ui
./mvnw clean package
java -jar target/web-ui-1.0.jar
```

#### API Endpoints
1. [List all Words / Counts](http://localhost:8084)
1. [Windowed Word / Count] - TODO
1. Get Wordcount for Start/End Time - TODO

### Deploying to Kubernetes
#### Build the apps
The `docker.image.prefix` property should be set to be set to your username on Dockerhub.  For example, if you called the command below with `-Ddocker.image.prefix=foobar`, then the resulting image would be tagged as `foobar/${project.artifactId}`.
```
pivotal-confluent-demo $ ./mvnw package docker:build -Ddocker.image.prefix=<your-dockerhub-username>
```

#### Push to Dockerhub
```
pivotal-confluent-demo $ ./mvnw docker:push -Ddocker.image.prefix=<your-dockerhub-username>
```

#### Deploy Kafka
You will need a storage class for Kafka to create persistent volumes.  Here's an template for doing this on vSphere:
```
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
annotations:
    storageclass.kubernetes.io/is-default-class: "true"
name: default
parameters:
datastore: <your-vsphere-datastore-here>
provisioner: kubernetes.io/vsphere-volume 
```

Set the namespace that you want to work in
```
kubectl config set-context --current --namespace=wordcount
```

Then you can proceed to deploy Kafka via Helm
```
helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator
helm install --set log.retention.hours=4,persistence.size=10Gi,replicas=6 --name my-kafka incubator/kafka 
```

#### Create and Run Cache Pod
Run the following command, and break out after you see the ready.
```
kubectl create -f k8s/cache; kubectl get pod cache-server -w
```

#### Create and Run Application Pods
Run the following command, and break out after you see the web-ui ready.
```
kubectl create -f k8s/apps; kubectl get pod web-ui -w
```

#### Expose the Web UI
##### NodePort
```
kubectl expose pod web-ui --target-port=8084 --name=web-ui-service --type=NodePort
````

##### LoadBalancer
```
kubectl expose pod web-ui --target-port=8084 --name=web-ui-service --type=LoadBalancer
```

#### Delete Apps
```
kubectl delete -f k8s/apps
```

#### Delete Cache
```
kubectl delete -f k8s/cache
```

#### Delete web-ui-service Service
```
kubectl delete service web-ui-service
```

#### Delete Kafka
```
helm delete my-kafka --purge
kubectl delete pvc $(kubectl get pvc -o=custom-columns=Name:.metadata.name | grep datadir-my-kafka)
```