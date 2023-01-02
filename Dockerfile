# Build layer

FROM maven:3.6.3-adoptopenjdk-11 as build
LABEL maintainer="twbarkhuizen@gmail.com"

ARG GIT_COMMIT
ARG ENVIRONMENT

LABEL service=service \
      stage=build \
      tag=${GIT_COMMIT}\
      env=${ENVIRONMENT}

RUN mkdir /ms
WORKDIR /ms
# Copy the source code to build it
COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN  mvn clean package -DskipTests=true \
  && mv ./target/application.jar ./ms.jar

# Deployment layer

FROM adoptopenjdk:11-jre-hotspot
LABEL maintainer="twarkhuizen@gmail.com"

ARG GIT_COMMIT
ARG ENVIRONMENT

LABEL service=service \
      stage=deploy \
      tag=${GIT_COMMIT}\
      env=${ENVIRONMENT}


RUN addgroup -gid 1009  udemo \
 && adduser -uid 1009 -gid 1009 --gecos "udemo udemo,udemo,udemo,udemo" --disabled-password udemo \
 &&  mkdir -p /ms && chown -R udemo:udemo /ms

WORKDIR /ms
COPY --chown=udemo:udemo --from=build /ms/ms.jar ./ms.jar

USER udemo
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./ms.jar"]
