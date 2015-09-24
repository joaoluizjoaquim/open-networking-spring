FROM dockerfile/java:oracle-java8
MAINTAINER João Joaquim <joaoluizjoaquim@gmail.com>
ADD target/open-networking-spring.jar .
RUN java -jar open-networking-spring.jar
