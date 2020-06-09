#!/bin/bash
print_usage(){
  echo "Usage：TStar micro service operation and maintenance script introduction"
  echo -e "  -deploy \t A key deployment TStar micro service, the roles of micro service reference the file role"
  echo -e "  -upgrade [registry | config | system | vehicle | terminal | protocol | monitor]"
  echo -e "  -start [all | registry | config | system | vehicle | terminal | protocol | monitor]"
  echo -e "  -stop  [all | registry | config | system | vehicle | terminal | protocol | monitor]"
  echo -e "  -status \t\t\t show status of all TStar MicroService "
  echo -e "  -clean \t\t\t clean the TStar MicroService"
}

ME=`which $0`
SBIN=`dirname ${ME}`
CURR=`cd ${SBIN};cd ..;pwd`

CURR_CONF="$CURR/conf"
CURR_PACKAGET="$CURR/packages"
CURR_BIN="$CURR/bin"

ROOT_OPERATION="/usr"
ROOT="$ROOT_OPERATION/feisi"
ROOT_BIN="$ROOT/bin"
ROOT_LOG="$ROOT/logs"
ROOT_PACKAGET="$ROOT/packages"
ROOT_PIDS="$ROOT/pids"
ROOT_TEMP="$ROOT/tmp"

ROOT_HTML="$ROOT_OPERATION/feisi"

# PACKAGET_REG=`cd ${CURR_PACKAGET}; ls avatar-registry*`
# PACKAGET_CONF=`cd ${CURR_PACKAGET}; ls avatar-config*`
PACKAGET_SYSTEM="feisi-system"
PACKAGET_VEHICLE="feisi-vehicle"
PACKAGET_TERMINAL="feisi-terminal"
PACKAGET_GRAMPUS="feisi-grampus"
PACKAGET_GATEWAY="feisi-gateway"
PACKAGET_FILES="feisi-files"
PACKAGET_MESSAGE="feisi-message"
PACKAGET_MONITOR="feisi-monitor"
PACKAGET_PROCESS="feisi-activiti"
PACKAGET_APPSERVER="feisi-appserver"

RUN_BIN="microservice.sh"


if [[ ! -f ${CURR_CONF}/role ]];then
    echo "there is no file role on dir ${CURR_CONF} "
    exit 1
else 
    . ${CURR_CONF}/role
fi

# if [ -z "$REGISTRY" ];then
#     echo "this is no registry IP addr on the file role."
#     exit 1
# fi
# if [ -z "$CONFIG" ];then
#     echo "this is no config IP addr on the file role."
#     exit 1
# fi
if [ -z "$ALL" ];then
    echo "this is no All IP addr on the file role."
    exit 1
fi

if [ $# = 0 ];then
    print_usage
    exit 1
fi

createdir(){
    echo "create local dir $ROOT "
    mkdir -p ${ROOT}
    echo "create local dir $ROOT_BIN "
    cp -r ${CURR_BIN} ${ROOT}
    echo "create local dir $ROOT_LOG "
    mkdir -p ${ROOT_LOG}
    echo "create local dir ${ROOT_PACKAGET} "
    cp -r ${CURR_PACKAGET} ${ROOT}
    echo "create local dir $ROOT_PIDS "
    mkdir -p ${ROOT_PIDS}
    echo "create local dir $ROOT_TEMP "
    mkdir -p ${ROOT_TEMP}
}

deploy(){
    createdir
    # 设置环境变量
    array=(${REGISTRY//,/ })
    OLD_IFS=$IFS
    IFS=','    
    for ip in ${ALL[@]};do
        echo "copy dir $ROOT to ${ip} $ROOT_OPERATION"
        echo "-------------"
        scp -q -r ${ROOT} ${ip}:${ROOT_OPERATION}
    done
    IFS=${OLD_IFS}
#    cp -R $CURR/configcenter $ROOT_OPERATION
#    echo "copy $CURR/configcenter to $ROOT_OPERATION"
}


start(){
    OLD_IFS=$IFS
    IFS=','
    case $1 in
        "system")
            for ip in ${SYSTEM[@]};do
                echo "start Avatar System Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-system test 8104 -Xms600m -Xmx600m"
            done
            ;;
        "vehicle")
            for ip in ${VEHICLE[@]};do
                echo "start Avatar Vehicle Service ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-vehicle test 8100 -Xms1000m -Xmx2000m"
            done
            ;;
        "terminal")
            for ip in ${TERMINAL[@]};do
                echo "start Avatar Terminal Service ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-terminal test 8106 -Xms500m -Xmx1000m"
            done
            ;;
        "grampus")
            for ip in ${GRAMPUS[@]};do
                echo "start Avatar Grampus Client ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-grampus test 8108 -Xms500m -Xmx1000m"
            done
            ;;
        "gateway")
            for ip in ${GATEWAY[@]};do
                echo "start Avatar Gateway Service ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-gateway test 8081 -Xms500m -Xmx500m"
            done
            ;;
        "appserver")
            for ip in ${APPSERVER[@]};do
                echo "start Avatar Appserver Service ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-appserver test 8082 -Xms500m -Xmx500m"
            done
            ;;
        "files")
            for ip in ${FILES[@]};do
                echo "start Avatar Files Service ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-files test 8110 -Xms500m -Xmx500m"
            done
            ;;
         "message")
             for ip in ${MESSAGE[@]};do
                 echo "start Avatar Message Service ${ip} ..."
                 ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-message test 8111 -Xms500m -Xmx500m"
             done
             ;;
         "monitor")
             for ip in ${MONITOR[@]};do
                 echo "start Avatar Monitor Service ${ip} ..."
                 ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-monitor test 8101 -Xms500m -Xmx500m"
             done
             ;;
         "process")
             for ip in ${PROCESS[@]};do
                 echo "start Avatar process Service ${ip} ..."
                 ssh ${ip} "$ROOT_BIN/$RUN_BIN -start feisi-activiti test 8101 -Xms500m -Xmx500m"
             done
             ;;
			
        "all")     
            # start registry
            # sleep 4
            # start config
            # sleep 10
            start system
            start vehicle
            start terminal       
            start grampus         
            start gateway
            start appserver
			start files   			
			start message
			start monitor
            ;;
    esac
    IFS=${OLD_IFS}
}

status(){
    OLD_IFS=$IFS
    IFS=','
    echo -e "############################################################"
    echo -e "                       manager Tstar microservice           "
    echo -e "Feisi System:"
        for ip in ${SYSTEM[@]};do
            image_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-system\>'|awk '{print \$1}'")
            if [[ ! -z "$image_pid" ]];then
           echo -e "${ip}(PID:$image_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Vehicle Service:"
        for ip in ${GB6SVC[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-service\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Terminal Service:"
        for ip in ${TERMINAL[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-terminal\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Gateway Service:"
        for ip in ${GATEWAY[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-gateway\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Appserver Service:"
        for ip in ${APPSERVER[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-appserver\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Files Service:"
        for ip in ${FILES[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-files\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Message Service:"
        for ip in ${MESSAGE[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-message\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "Feisi Monitor Service:"
        for ip in ${MONITOR[@]};do
            tag_pid=$(ssh ${ip} "jps -l | grep -i '\<feisi-monitor\>'|awk '{print \$1}'")
            if [[ ! -z "$tag_pid" ]];then
           echo -e "${ip}(PID:$tag_pid) \t\t\033[32;5mOK\033[0m"
            else
               echo -e "${ip} \t\t\t\033[31;5mFAIL\033[0m"
            fi
        done
    echo -e "############################################################"
    IFS=${OLD_IFS}
}

stop(){
    OLD_IFS=$IFS
    IFS=','
    case $1 in
        "system")
            for ip in ${SYSTEM[@]};do
                echo "stop feisi System Service on  ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-system"
            done
            ;;
        "vehicle")
            for ip in ${VEHICLE[@]};do
                echo "stop feisi Iov Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-vehicle"
            done
            ;;
        "terminal")
            for ip in ${TERMINAL[@]};do
                echo "stop feisi Terminal Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-terminal"
            done
            ;;
        "grampus")
            for ip in ${GRAMPUS[@]};do
                echo "stop feisi Grampus Client ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-grampus"
            done
            ;;
        "gateway")
            for ip in ${GATEWAY[@]};do
                echo "stop feisi Gateway Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-gateway"
            done
            ;;
         "appserver")
            for ip in ${APPSERVER[@]};do
                echo "stop feisi Appserver Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-appserver"
            done
            ;;
        "files")
            for ip in ${FILES[@]};do
                echo "stop feisi Files Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-files"
            done
            ;;
        "message")
            for ip in ${MESSAGE[@]};do
                echo "stop feisi Message Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-message"
            done
            ;;
        "monitor")
            for ip in ${MONITOR[@]};do
                echo "stop feisi Monitor Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-monitor"
            done
            ;;
        "process")
            for ip in ${PROCESS[@]};do
                echo "stop feisi process Service on ${ip} ..."
                ssh ${ip} "$ROOT_BIN/$RUN_BIN -stop feisi-activiti"
            done
            ;;

        "all")
            stop system
            stop vehicle
            stop terminal          
            stop grampus       
            stop gateway
            stop appserver
			stop files
			stop message
			stop monitor
            # stop config
            # stop registry
            ;;
    esac
    IFS=${OLD_IFS}
}

clean(){
    OLD_IFS=$IFS
    IFS=','    
    for ip in ${ALL[@]};do
        echo "delete $ROOT on ${ip}  "
        ssh ${ip} "rm -rf $ROOT"
    done
    # rm -rf $ROOT_OPERATION/configcenter
    # echo "delete $ROOT_OPERATION/configcenter"  
    IFS=${OLD_IFS}
}

upgrade(){
    OLD_IFS=$IFS
    IFS=','    
    case $1 in
        "system")
            stop $1
            for ip in ${SYSTEM[@]};do
                echo "update system service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_SYSTEM}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_SYSTEM} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_SYSTEM}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "vehicle")
            stop $1
            for ip in ${VEHICLE[@]};do
                echo "update feisi vehicle Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_VEHICLE}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_VEHICLE} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_VEHICLE}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "terminal")
            stop $1
            for ip in ${TERMINAL[@]};do
                echo "update feisi Terminal Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_TERMINAL}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_TERMINAL} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_TERMINAL}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "grampus")
            stop $1
            for ip in ${GRAMPUS[@]};do
                echo "update feisi Grampus Client on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_GRAMPUS}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_GRAMPUS} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_GRAMPUS}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "gateway")
            stop $1
            for ip in ${GATEWAY[@]};do
                echo "update feisi Gateway Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_GATEWAY}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_GATEWAY} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_GATEWAY}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
		    	;;
	       "appserver")
            stop $1
            for ip in ${APPSERVER[@]};do
                echo "update feisi appserver Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_APPSERVER}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_APPSERVER} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_APPSERVER}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "files")
            stop $1
            for ip in ${FILES[@]};do
                echo "update feisi Files Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_FILES}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_FILES} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_FILES}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "message")
            stop $1
            for ip in ${MESSAGE[@]};do
                echo "update feisi Message Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_MESSAGE}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_MESSAGE} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_MESSAGE}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "monitor")
            stop $1
            for ip in ${MONITOR[@]};do
                echo "update feisi Monitor Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_MONITOR}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_MONITOR} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_MONITOR}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "process")
            stop $1
            for ip in ${PROCESS[@]};do
                echo "update feisi process Service on ${ip} "
                ssh ${ip} rm -rf ${ROOT_PACKAGET}/${PACKAGET_PROCESS}
                scp -q -r ${CURR_PACKAGET}/${PACKAGET_PROCESS} ${ip}:${ROOT_PACKAGET}
                scp ${CURR_PACKAGET}/${PACKAGET_PROCESS}.jar ${ip}:${ROOT_PACKAGET}
            done
            start $1
            ;;
        "html")
            for ip in ${HTML[@]};do
                echo "update Html on ${ip} "
                ssh ${ip} rm -rf ${ROOT_HTML}/html
                scp -q -r ${CURR}/html.zip ${ip}:${ROOT_HTML}
                ssh ${ip} unzip ${ROOT_HTML}/html.zip -d ${ROOT_HTML}
            done
            ;;
        "all")
            upgrade system
            upgrade vehicle
            upgrade terminal        
            upgrade grampus      
            upgrade gateway
            upgrade appserver
			upgrade files
			upgrade message
			upgrade monitor
			upgrade process

            ;;
    esac
    IFS=${OLD_IFS}
}

cmd=$1
shift
case ${cmd} in
    ("-deploy")
                deploy
    ;;
    ("-start")
                start $@
    ;;
    ("-status")
                status
    ;;
    ("-stop")
                stop $@
    ;;
    ("-clean")
                clean
    ;;
    ("-upgrade")
                upgrade $@
    ;;
    ("-restart")
                stop $@
                sleep 1
                start $@
    ;;
    (*)
        print_usage
    ;;
esac


