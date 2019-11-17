FROM openjdk:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac -cp ./res/\*  schema.java
CMD ["java", "schema"]
