FROM adoptopenjdk/maven-openjdk11 as compile

RUN mkdir /myapp
COPY . /myapp

WORKDIR /myapp
RUN mvn package
RUN mv ./target/Loghmeh.war ./target/ROOT.war

FROM tomcat:9.0-alpine

RUN rm -r /usr/local/tomcat/webapps/ROOT
COPY --from=compile /myapp/target/ROOT.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
