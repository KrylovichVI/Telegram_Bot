#!/usr/bin/env bash

mvn clean package

echo 'Copy file...'

scp -i ~/.ssh/id_rsa \
    target/Telegram_Bot-1.0-jar-with-dependencies.jar \
    root@192.168.210.86:/home/root/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@192.168.210.86 << EOF
    pgrep java | xargs kill -9
    nohup java -jar /home/root/Telegram_Bot-1.0-jar-with-dependencies.jar > TelegramBotLog.log &
EOF

echo 'Bye...'