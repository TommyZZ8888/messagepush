FROM openjdk:8-jre
ENV PARAMS=""
WORKDIR /build
ADD ./autin.web-0.0.1-SNAPSHOT.jar ./austin.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS austin.jar $PARAMS"]

#docker run -e PARAMS="--austin-database-ip= --austin-database-port=3306 --austin-redis-ip= --austin-mq-pipeline=eventbus  " -p 8080:8080 --name austin:1.0