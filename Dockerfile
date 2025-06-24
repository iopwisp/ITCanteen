FROM openjdk:17
WORKDIR /ITCanteen
COPY target/backend.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM node:18-alpine
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
CMD ["npm", "run", "dev"]
