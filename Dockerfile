FROM amazoncorretto:17-alpine
#if you don't have the jar file, you can use the following command to build it
#mvn clean
#mvn install -DskipTests=true
COPY target/*.jar akloona.jar
#same server port as in the application.properties
EXPOSE 8080
ENTRYPOINT ["java","-jar","akloona.jar"]

#to build the img in the terminal write (docker build -t akloona_img .) don't forget the dot
#to stop the container in the terminal write (docker stop container_id)
# to show the images in the terminal write (docker images)
#to rename the image in the terminal write (docker tag akloona_img akloona_img:latest)
#CONTAINER ID   IMAGE         COMMAND                  CREATED          STATUS          PORTS                    NAMES
 #201fd1f983f2   akloona_img   "java -jar akloona.j…"   4 seconds ago    Up 3 seconds    0.0.0.0:8080->8080/tcp   akloona_com
 #9c6a88736fe0   postgres      "docker-entrypoint.s…"   32 minutes ago   Up 32 minutes   0.0.0.0:5432->5432/tcp   postgres_com

#to get the above information in the terminal write (docker ps)
#to show the springboot logs in the terminal write (docker logs akloona_com)

#if you make changes to the code and you want to rebuild the image you have to stop the container and remove it
#to stop the container in the terminal write (docker stop akloona_com)
#to remove the container in the terminal write (docker rm akloona_com)
#to remove the image in the terminal write (docker rmi akloona_img)
#to build after changes the code in the terminal write (docker build -t akloona_img .)
#to run the img in the terminal write (docker run -d -p 8080:8080 --name=akloona_com akloona_img)
#to show the containers in the terminal write (docker ps)
#to see online