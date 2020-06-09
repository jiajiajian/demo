call rimraf .\packages\*
call mvn clean install -Dmaven.test.skip

mkdir .\packages\feisi-system
move .\avatar-modules\avatar-system-service\target\*.jar .\packages\
move .\avatar-modules\avatar-system-service\target\lib\*.jar .\packages\feisi-system\

mkdir .\packages\feisi-vehicle
move .\avatar-modules\avatar-vehicle-service\target\*.jar .\packages\
move .\avatar-modules\avatar-vehicle-service\target\lib\*.jar .\packages\feisi-vehicle\

mkdir .\packages\feisi-terminal
move .\avatar-modules\avatar-terminal-service\target\*.jar .\packages\
move .\avatar-modules\avatar-terminal-service\target\lib\*.jar .\packages\feisi-terminal\

mkdir .\packages\feisi-grampus
move .\avatar-modules\avatar-grampus-service\target\*.jar .\packages\
move .\avatar-modules\avatar-grampus-service\target\lib\*.jar .\packages\feisi-grampus\

mkdir .\packages\feisi-gateway
move .\avatar-gateway\target\*.jar .\packages\
move .\avatar-gateway\target\lib\*.jar .\packages\feisi-gateway\

mkdir .\packages\feisi-appserver
move .\avatar-appserver\target\*.jar .\packages\
move .\avatar-appserver\target\lib\*.jar .\packages\feisi-appserver\

mkdir .\packages\feisi-files
move .\avatar-modules\avatar-files\target\*.jar .\packages\
move .\avatar-modules\avatar-files\target\lib\*.jar .\packages\feisi-files\

mkdir .\packages\feisi-monitor
move .\avatar-modules\avatar-monitor-service\target\*.jar .\packages\
move .\avatar-modules\avatar-monitor-service\target\lib\*.jar .\packages\feisi-monitor\

mkdir .\packages\feisi-message
move .\avatar-modules\avatar-message-service\target\*.jar .\packages\
move .\avatar-modules\avatar-message-service\target\lib\*.jar .\packages\feisi-message\

mkdir .\packages\feisi-activiti
move .\avatar-modules\avatar-activiti-service\target\*.jar .\packages\
move .\avatar-modules\avatar-activiti-service\target\lib\*.jar .\packages\feisi-activiti\

mkdir .\packages\feisi-protocol
move .\avatar-modules\avatar-protocol-service\target\*.jar .\packages\
move .\avatar-modules\avatar-protocol-service\target\lib\*.jar .\packages\feisi-protocol\

call cmd
