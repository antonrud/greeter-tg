# greeter-tg

This bot will greet new group chat members

## Setup

1. Create new bot with [BotFather](https://t.me/BotFather) and obtain your bot token.
2. Add your bot to a group chat and promote it to admin.
3. Edit greeting message in `src/main/resources/greeting.txt`

## Launch

```bash
./gradlew run --args="BOT_TOKEN"
```

## Docker

Build the image:

```bash
docker build -t greeter-tg .
```

Run the container:

```bash
docker run -e BOT_TOKEN=your_token greeter-tg
```

To enable debug logging, add `-e DEBUG=debug`:

```bash
docker run -e BOT_TOKEN=your_token -e DEBUG=debug greeter-tg
```
