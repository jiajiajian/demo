#!/bin/bash

SERVICENAME=$2
ACTIVE=$3
PORT=$4
XARGS=""
ROOT="/usr/feisi"
PID="$ROOT/pids/$SERVICENAME.pid"
CONF="$ROOT/conf"
LOG="$ROOT/logs"
PACKAGET=`ls ${ROOT}/packages/${SERVICENAME}*`
JAVA_OPTS="$XARGS -Xverify:none -Dlog.base.dir=$ROOT -Dloader.path=$ROOT/packages/$SERVICENAME -Djava.awt.headless=true -Djava.io.tmpdir=${ROOT}/tmp -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$LOG/${SERVICENAME}.gc -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=1 -XX:GCLogFileSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG}"
STARTUP_PARAMS="--spring.profiles.active=${ACTIVE} --server.port=${PORT}"
export REGISTRY_MASTER_HOST=10.3.12.51
export REGISTRY_BACKUP_HOST=10.3.12.52
export REGISTRY_PORT=8761

cmd=$1

shift
case ${cmd} in
    ("-start")
    if [[ -f ${PID} ]];then
        pidInFile=`cat ${PID}`
        pidReal=`jps|grep -i "\<$pidInFile\>"`
        if [[ ! -z "$pidInFile" ]]&&[[ ! -z "$pidReal" ]];then
            echo "$SERVICENAME is started"
            exit 0
        fi
        fi
        echo "JAVA_OPS ${JAVA_OPTS} STARTUP_PARAMS: ${STARTUP_PARAMS}" 
        nohup java ${JAVA_OPTS} -jar ${PACKAGET} ${STARTUP_PARAMS} >> /dev/null &
        echo $! > ${PID}
    ;;
    ("-stop")
        if [[ -f ${PID} ]];then
            cat ${PID}|xargs kill -SIGTERM
        else
            jps -l| grep "$SERVICENAME*" | awk -F ' ' '{print $1}'|xargs kill -9
        fi
        rm -f ${PID}
    ;;

    (*)
        exit 1
    ;;
esac
