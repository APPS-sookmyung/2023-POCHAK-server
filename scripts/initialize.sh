#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/pochakapp"
JAR_FILE="$PROJECT_ROOT/pochak/build/libs/pochak-0.0.1-SNAPSHOT.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)


# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f $JAR_FILE)
