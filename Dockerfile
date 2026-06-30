FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle installDist --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/install/greeter-tg .
# Required: BOT_TOKEN - Telegram bot token
# Optional: DEBUG - set to "debug" to enable verbose logging
CMD ["sh", "-c", "bin/greeter-tg $BOT_TOKEN ${DEBUG:-}"]
