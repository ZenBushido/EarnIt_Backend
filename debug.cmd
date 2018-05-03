
call mvn clean package -DskipTests

call java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar target/earnit-api.war
