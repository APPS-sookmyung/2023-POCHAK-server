#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/pochakapp"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_PATH 확인" >> $DEPLOY_LOG
JAR_NAME=$(ls $PROJECT_ROOT/pochak/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$PROJECT_ROOT/pochak/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

# jar 파일 실행
echo "$TIME_NOW > $JAR_NAME 파일 실행" >> $DEPLOY_LOG
nohup java -jar -Duser.timezone=Asia/Seoul $JAR_PATH --logging.level.org.hibernate.SQL=DEBUG > $APP_LOG 2> $ERROR_LOG &

echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
