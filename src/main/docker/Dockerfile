FROM dockerfile/java:oracle-java8
MAINTAINER João Joaquim <joaoluizjoaquim@gmail.com>
RUN apt-get update && apt-get upgrade -y
COPY /var/lib/jenkins/workspace/open-networking-create-image/target/open-networking-spring-*.jar /opt/
