# Use a lightweight JDK base image
FROM openjdk:17-slim

# Create app directory
WORKDIR /app

# Copy the built jar file into the image
COPY build/libs/*.jar app.jar

# Set environment variable for the token (will be injected by Render)
ENV DISCORD_BOT_TOKEN=""

# Run the bot
ENTRYPOINT ["java", "-jar", "app.jar"]
