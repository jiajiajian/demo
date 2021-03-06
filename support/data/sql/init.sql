INSERT INTO `base_sys_function`(`ID`, `FUNCTION_CODE`, `FUNCTION_NAME`, `PARENT_FUNCTION_ID`,  `NAVI_URL`, `ICON`, `LAYER_LEVEL`, `FUNCTION_TYPE`, `SORT_CODE`, `REMARK`, `IS_NEW_PAGE`)
 VALUES (10,'','非道路机械远程管理信息平台',-1, '','',1,1,1,'非道路机械远程管理信息平台', 0),
(1010,'首页','首页',10, '/dashboard','',2,2,1,'首页', 0),
(101011,'数据导出','数据导出',1010, '','',6,6,1,'数据导出', 0),
(1020,'生产调试','生产调试',10, '/vehicle','',2,2,2,'生产调试', 0),
(102010,'终端调试','终端调试',1020, '/vehicle/vehicleDebug','',3,3,1,'终端调试', 0),
(10201011,'调试','调试',102010, '','',6,6,1,'调试', 0),
(10201012,'新增调试车','新增调试车',102010, '','',6,6,2,'新增调试车', 0),
(10201013,'删除调试车','删除调试车',102010, '','',6,6,3,'删除调试车', 0),
(10201014,'导出','导出',102010, '','',6,6,4,'导出', 0),
(102020,'调试日志','调试日志',1020, '/vehicle/vehicleDebugLog','',3,3,1,'调试日志', 0),
(10202021,'导出','导出',102020, '','',6,6,1,'导出', 0),
(1021,'车辆监控','车辆监控',10, '/vehicle','',2,2,3,'车辆监控', 0),
(102110,'车辆状态','车辆状态',1021, '/vehicle/vehicleMonitor','',3,3,1,'车辆状态', 0),
(10211011,'导出','导出',102110, '','',6,6,1,'导出', 0),
(102120,'车辆工况','车辆工况',1021, '/vehicle/vehicleMonitor','',3,3,2,'车辆工况', 0),
(10211021,'导出','导出',102120, '','',6,6,1,'导出', 0),
(102130,'车辆巡检','车辆巡检',1021, '/vehicle/vehicleMonitor','',3,3,3,'车辆巡检', 0),
(10211031,'导出','导出',102130, '','',6,6,1,'导出', 0),
(1098,'','排放监控',10, '','',2,2,4,'排放监控', 0),
(109810,'','NOx浓度异常',1098, '','',3,3,1,'NOx浓度异常', 0),
(10981010,'','查询',109810, '','',6,6,1,'查询', 0),
(10981011,'','重置',109810, '','',6,6,2,'重置', 0),
(10981012,'','导出',109810, '','',6,6,3,'导出', 0),
(109811,'','尿素箱空异常',1098, '','',3,3,2,'尿素箱空异常', 0),
(10981110,'','查询',109811, '','',6,6,1,'查询', 0),
(10981111,'','重置',109811, '','',6,6,2,'重置', 0),
(10981112,'','导出',109811, '','',6,6,3,'导出', 0),
(109812,'','故障灯点亮异常',1098, '','',3,3,3,'故障灯点亮异常', 0),
(10981210,'','查询',109812, '','',6,6,1,'查询', 0),
(10981211,'','重置',109812, '','',6,6,2,'重置', 0),
(10981212,'','导出',109812, '','',6,6,3,'导出', 0),
(109813,'','监控参数配置',1098, '','',3,3,4,'监控参数配置', 0),
(1030,'故障报警','故障报警',10, '/alarm','',2,2,5,'故障报警', 0),
(103010,'车辆报警','车辆报警',1030, '/alarm/alarmHistory','',3,3,1,'车辆报警', 0),
(10301011,'导出','导出',103010, '','',6,6,1,'导出', 0),
(103020,'车辆报警','车辆故障',1030, '/alarm/alarmHistory','',3,3,2,'车辆故障', 0),
(10302021,'导出','导出',103020, '','',6,6,1,'导出', 0),
(103030,'通知策略','通知策略',1030, '/alarm/noticeStrategy','',3,3,3,'通知策略', 0),
(10303031,'编辑','编辑',103030, '','',6,6,1,'编辑', 0),
(1099,'','数据转发',10, '','',2,2,6,'数据转发', 0),
(109910,'','转发配置',1099, '','',3,3,1,'转发配置', 0),
(10991010,'','新增',109910, '','',6,6,1,'新增', 0),
(10991011,'','编辑',109910, '','',6,6,2,'编辑', 0),
(10991012,'','删除',109910, '','',6,6,3,'删除', 0),
(10991013,'','关联车辆',109910, '','',6,6,4,'关联车辆', 0),
(109911,'','转发记录',1099, '','',3,3,2,'转发记录', 0),
(10991110,'','查询',109911, '','',6,6,1,'查询', 0),
(10991111,'','重置',109911, '','',6,6,2,'重置', 0),
(1040,'远程控制','远程控制',10, '/control','',2,2,7,'远程控制', 0),
(104010,'远程锁车','远程锁车',1040, '/control/lock','',3,3,1,'远程锁车', 0),
(10401010,'查询','查询',104010, '','',6,6,0,'查询', 0),
(10401011,'远程控制','远程控制',104010, '','',6,6,1,'远程控制', 0),
(10401012,'新增控制车','新增控制车',104010, '','',6,6,2,'新增控制车', 0),
(10401013,'删除控制车','删除控制车',104010, '','',6,6,3,'删除控制车', 0),
(10401014,'导出','导出',104010, '','',6,6,4,'导出', 0),
(10401015,'指令历史','指令历史',104010, '','',6,6,5,'指令历史', 0),
(104020,'远程锁车日志','远程锁车日志',1040, '/control/lock_log','',3,3,2,'远程锁车日志', 0),
(10402010,'查询','查询',104020, '','',6,6,0,'查询', 0),
(10402011,'导出','导出',104020, '','',6,6,1,'导出', 0),
(104030,'审批锁车','审批锁车',1040, '/control/approval_lock','',3,3,3,'审批锁车', 0),
(10403010,'查询','查询',104030, '','',6,6,0,'查询', 0),
(10403011,'申请','申请',104030, '','',6,6,1,'申请', 0),
(10403012,'删除','删除',104030, '','',6,6,2,'删除', 0),
(10403013,'修改','修改',104030, '','',6,6,3,'修改', 0),
(10403014,'执行','执行',104030, '','',6,6,4,'执行', 0),
(104040,'审批锁车日志','审批锁车日志',1040, '/control/approval_log','',3,3,4,'审批锁车日志', 0),
(10404010,'查询','查询',104040, '','',6,6,0,'查询', 0),
(10404011,'导出','导出',104040, '','',6,6,1,'导出', 0),
(1025,'车辆服务','车辆服务',10, '/vehicles','',2,2,8,'车辆服务', 0),
(102510,'围栏策略','围栏策略',1025, '/monitor/fence','',3,3,1,'围栏策略', 0),
(10251011,'新增','新增',102510, '','',6,6,1,'新增', 0),
(10251012,'编辑','编辑',102510, '','',6,6,2,'编辑', 0),
(10251013,'删除','删除',102510, '','',6,6,3,'删除', 0),
(10251014,'关联车辆','关联车辆',102510, '','',6,6,4,'关联车辆', 0),
(10251015,'解除关联','解除关联',102510, '','',6,6,5,'解除关联', 0),
(102520,'围栏报警','围栏报警',1025, '/monitor/fence','',3,3,2,'围栏报警', 0),
(10252021,'导出','导出',102520, '','',6,6,1,'导出', 0),
(102530,'保养管理','保养管理',1025, '/vehicle/vehicleMaintenance','',3,3,2,'保养管理', 0),
(10253031,'新增','新增',102530, '','',6,6,1,'新增', 0),
(10253032,'编辑','编辑',102530, '','',6,6,2,'编辑', 0),
(10253033,'删除','删除',102530, '','',6,6,3,'删除', 0),
(10253034,'导出','导出',102530, '','',6,6,1,'导出', 0),
(102540,'保养管理','保养策略',1025, '/vehicle/maintenanceTactics','',3,3,2,'保养策略', 0),
(10254041,'新增','新增',102540, '','',6,6,1,'新增', 0),
(10254042,'编辑','编辑',102540, '','',6,6,2,'编辑', 0),
(10254043,'删除','删除',102540, '','',6,6,3,'删除', 0),
(10254044,'保养设置','保养设置',102540, '','',6,6,4,'保养设置', 0),
(10254045,'导出','导出',102540, '','',6,6,5,'导出', 0),
(102550,'保养日志','保养日志',1025, '/vehicle/maintenanceLog','',3,3,2,'保养日志', 0),
(10255051,'保养管理','保养管理',102550, '','',6,6,1,'保养管理', 0),
(1070,'统计分析','统计分析',10, '/report','',2,2,9,'统计分析', 0),
(107010,'区域车辆统计','区域车辆统计',1070, '','',3,3,1,'区域车辆统计', 0),
(10701011,'导出','导出',107010, '','',6,6,1,'导出', 0),
(107020,'机型统计','机型统计',1070, '','',3,3,1,'机型统计', 0),
(10702021,'导出','导出',107010, '','',6,6,1,'导出', 0),
(107030,'总工时统计','总工时统计',1070, '','',3,3,1,'总工时统计', 0),
(10703031,'导出','导出',107030, '','',6,6,1,'导出', 0),
(107040,'工作时间统计','工作时间统计',1070, '','',3,3,1,'工作时间统计', 0),
(10704041,'导出','导出',107040, '','',6,6,1,'导出', 0),
(107050,'在线率统计','在线率统计',1070, '','',3,3,1,'在线率统计', 0),
(10705051,'导出','导出',107050, '','',6,6,1,'导出', 0),
(1097,'统计分析（扩展）','统计分析(扩展)',10, '','',2,2,10,'统计分析(扩展)', 0),
(109710,'','单车分析',1097, '','',3,3,1,'单车分析', 0),
(10971010,'','查询',109710, '','',6,6,1,'查询', 0),
(10971011,'','重置',109710, '','',6,6,2,'重置', 0),
(10971012,'','导出',109710, '','',6,6,3,'导出', 0),
(10971013,'','工作时长统计',109710, '','',6,6,4,'工作时长统计', 0),
(10971014,'','发动机转速/状态统计',109710, '','',6,6,5,'发动机转速/状态统计', 0),
(10971015,'','温度曲线图',109710, '','',6,6,6,'温度曲线图', 0),
(10971016,'','油耗/油位曲线图',109710, '','',6,6,7,'油耗/油位曲线图', 0),
(10971015,'','功率/工作模式统计',109710, '','',6,6,8,'功率/工作模式统计', 0),
(10971018,'','风扇控制',109710, '','',6,6,9,'风扇控制', 0),
(10971019,'','挖掘/行驶状态统计',109710, '','',6,6,10,'挖掘/行驶状态统计', 0),
(109711,'','月平均工时统计',1097, '','',3,3,2,'月平均工时统计', 0),
(10971110,'','查询',109711, '','',6,6,1,'查询', 0),
(10971111,'','重置',109711, '','',6,6,2,'重置', 0),
(109712,'','活跃车辆分析',1097, '','',3,3,3,'活跃车辆分析', 0),
(10971210,'','查询',109712, '','',6,6,1,'查询', 0),
(10971211,'','重置',109712, '','',6,6,2,'重置', 0),
(10971212,'','导出',109712, '','',6,6,3,'导出', 0),
(109713,'','车辆油耗分布',1097, '','',3,3,4,'车辆油耗分布', 0),
(10971310,'','查询',109713, '','',6,6,1,'查询', 0),
(10971311,'','重置',109713, '','',6,6,2,'重置', 0),
(10971312,'','导出',109713, '','',6,6,3,'导出', 0),
(109714,'','机型油耗分析',1097, '','',3,3,5,'机型油耗分析', 0),
(10971410,'','查询',109714, '','',6,6,1,'查询', 0),
(10971411,'','重置',109714, '','',6,6,2,'重置', 0),
(10971412,'','导出',109714, '','',6,6,3,'导出', 0),
(109715,'','各吨位小时数统计',1097, '','',3,3,6,'各吨位小时数统计', 0),
(10971510,'','查询',109715, '','',6,6,1,'查询', 0),
(10971511,'','重置',109715, '','',6,6,2,'重置', 0),
(10971512,'','导出',109715, '','',6,6,3,'导出', 0),
(109716,'','DTC影响统计',1097, '','',3,3,7,'DTC影响统计', 0),
(10971610,'','查询',109716, '','',6,6,1,'查询', 0),
(10971611,'','重置',109716, '','',6,6,2,'重置', 0),
(10971612,'','导出',109716, '','',6,6,3,'导出', 0),
(109717,'','工作时间分析',1097, '','',3,3,8,'工作时间分析', 0),
(10971710,'','查询',109717, '','',6,6,1,'查询', 0),
(10971711,'','重置',109717, '','',6,6,2,'重置', 0),
(10971712,'','导出',109717, '','',6,6,3,'导出', 0),
(109718,'','型号工时统计',1097, '','',3,3,9,'型号工时统计', 0),
(10971810,'','查询',109718, '','',6,6,1,'查询', 0),
(10971811,'','重置',109718, '','',6,6,2,'重置', 0),
(10971812,'','导出',109718, '','',6,6,3,'导出', 0),
(109719,'','代理商工时统计',1097, '','',3,3,10,'代理商工时统计', 0),
(10971910,'','查询',109719, '','',6,6,1,'查询', 0),
(10971911,'','重置',109719, '','',6,6,2,'重置', 0),
(10971912,'','导出',109719, '','',6,6,3,'导出', 0),
(109720,'','代理商车辆数统计',1097, '','',3,3,11,'代理商车辆数统计', 0),
(1071,'审批管理','审批管理',10, '/approval','',2,2,11,'审批管理', 0),
(107110,'审批流程配置','审批流程配置',1071, '/approval/process_config','',3,3,1,'审批流程配置', 0),
(10711010,'查询','查询',107110, '','',6,6,1,'查询', 0),
(10711011,'新增','新增',107110, '','',6,6,2,'新增', 0),
(10711012,'删除','删除',107110, '','',6,6,3,'删除', 0),
(10711013,'修改','修改',107110, '','',6,6,4,'修改', 0),
(10711014,'封存','封存',107110, '','',6,6,5,'封存', 0),
(10711015,'解封','解封',107110, '','',6,6,6,'解封', 0),
(107120,'事项审批','事项审批',1071, '/approval/matters','',3,3,2,'事项审批', 0),
(107130,'审批状态查询','审批状态查询',1071, '/approval/approval_status','',3,3,3,'审批状态查询', 0),
(10713010,'查询','查询',107130, '','',6,6,1,'查询', 0),
(1072,'客户服务','客户服务',10, '/vehicle','',2,2,12,'客户服务', 0),
(107210,'结算清单(预付费)','结算清单(预付费)',1072, '/vehicle/prePaid','',3,3,1,'结算清单(预付费)', 0),
(10721011,'导出','导出',107210, '','',6,6,1,'导出', 0),
(107220,'结算清单(后付费)','结算清单(后付费)',1072, '/vehicle/prePaid','',3,3,2,'结算清单(后付费)', 0),
(10722011,'导出','导出',107220, '','',6,6,1,'导出', 0),
(107230,'结算费用设置','结算费用设置',1072, 'vehicle/charge','',3,3,3,'结算费用设置', 0),
(10723011,'新增','新增',107230, '','',6,6,1,'新增', 0),
(10723012,'删除','删除',107230, '','',6,6,2,'删除', 0),
(10723013,'添加费用配置项','添加费用配置项',107230, '','',6,6,3,'添加费用配置项', 0),
(10723014,'删除费用配置项','删除费用配置项',107230, '','',6,6,4,'删除费用配置项', 0),
(10723015,'修改费用配置项','修改费用配置项',107230, '','',6,6,5,'修改费用配置项', 0),
(107240,'服务到期提醒','服务到期提醒',1072, '','',3,3,4,'服务到期提醒', 0),
(10724011,'导出','导出',107240, '','',6,6,1,'导出', 0),
(10100,'','备案管理',10, '','',2,2,13,'备案管理', 0),
(1010010,'','车辆型号备案',10100, '','',3,3,1,'车辆型号备案', 0),
(101001010,'','备案',1010010, '','',6,6,1,'备案', 0),
(101001011,'','导出',1010010, '','',6,6,2,'导出', 0),
(1010011,'','终端型号备案',10100, '','',3,3,2,'终端型号备案', 0),
(101001110,'','终端型号授权备案',1010011, '','',6,6,1,'终端型号授权备案', 0),
(101001111,'','终端型号备案',1010011, '','',6,6,2,'终端型号备案', 0),
(101001112,'','导出',1010011, '','',6,6,3,'导出', 0),
(1010012,'','加密芯片备案',10100, '','',3,3,3,'加密芯片备案', 0),
(101001210,'','芯片型号备案',1010012, '','',6,6,1,'芯片型号备案', 0),
(101001211,'','标识符申请',1010012, '','',6,6,2,'标识符申请', 0),
(101001212,'','导出',1010012, '','',6,6,3,'导出', 0),
(1010013,'','车辆信息备案',10100, '','',3,3,4,'车辆信息备案', 0),
(101001310,'','备案',1010013, '','',6,6,1,'备案', 0),
(101001311,'','导出',1010013, '','',6,6,2,'导出', 0),
(1080,'资源管理','资源管理',10, '/vehicle','',2,2,14,'资源管理', 0),
(108010,'车辆类型','车辆类型',1080, '/vehicle/vehicleType','',3,3,1,'车辆类型', 0),
(10801011,'新增','新增',108010, '','',6,6,1,'新增', 0),
(10801012,'编辑','编辑',108010, '','',6,6,2,'编辑', 0),
(10801013,'删除','删除',108010, '','',6,6,3,'删除', 0),
(10801014,'导出','导出',108010, '','',6,6,4,'导出', 0),
(108020,'车辆型号','车辆型号',1080, '/vehicle/vehicleModel','',3,3,2,'车辆型号', 0),
(10802021,'新增','新增',108020, '','',6,6,1,'新增', 0),
(10802022,'编辑','编辑',108020, '','',6,6,2,'编辑', 0),
(10802023,'删除','删除',108020, '','',6,6,3,'删除', 0),
(10802024,'导出','导出',108020, '','',6,6,4,'导出', 0),
(108030,'车辆管理','车辆信息',1080, '/vehicle/vehicle','',3,3,3,'车辆信息', 0),
(10803031,'批量查询','批量查询',108030, '','',6,6,1,'批量查询', 0),
(10803032,'编辑','编辑',108030, '','',6,6,2,'编辑', 0),
(10803033,'删除','删除',108030, '','',6,6,3,'删除', 0),
(10803034,'销售信息编辑','销售信息编辑',108030, '','',6,6,4,'销售信息编辑', 0),
(10803035,'修改调试时间','修改调试时间',108030, '','',6,6,5,'修改调试时间', 0),
(10803036,'修改服务状态','修改服务状态',108030, '','',6,6,6,'修改服务状态', 0),
(10803037,'更换终端','更换终端',108030, '','',6,6,7,'更换终端', 0),
(10803038,'双车互换终端','双车互换终端',108030, '','',6,6,8,'双车互换终端', 0),
(10803039,'移机','移机',108030, '','',6,6,9,'移机', 0),
(10803040,'延长服务期','延长服务期',108030, '','',6,6,10,'延长服务期', 0),
(10803041,'暂停','暂停',108030, '','',6,6,11,'暂停', 0),
(10803042,'暂停恢复','暂停恢复',108030, '','',6,6,12,'暂停恢复', 0),
(10803043,'销户','销户',108030, '','',6,6,13,'销户', 0),
(10803044,'导入','导入',108030, '','',6,6,14,'导入', 0),
(10803045,'导出','导出',108030, '','',6,6,15,'导出', 0),
(108040,'报警项配置','报警项配置',1080, '/vehicle/alarmItem','',3,3,4,'报警项配置', 0),
(10804041,'新增','新增',108040, '','',6,6,1,'新增', 0),
(10804042,'编辑','编辑',108040, '','',6,6,2,'编辑', 0),
(10804043,'删除','删除',108040, '','',6,6,3,'删除', 0),
(108050,'调试项配置','调试项配置',1080, '','',3,3,5,'调试项配置', 0),
(10805051,'新增','新增',108050, '','',6,6,1,'新增', 0),
(10805052,'编辑','编辑',108050, '','',6,6,2,'编辑', 0),
(10805053,'删除','删除',108050, '','',6,6,3,'删除', 0),
(108060,'故障字典项配置','故障字典项配置',1080, '/vehicle/faultDict','',3,3,6,'故障字典项配置', 0),
(10806061,'新增','新增',108060, '','',6,6,1,'新增', 0),
(10806062,'编辑','编辑',108060, '','',6,6,2,'编辑', 0),
(10806063,'删除','删除',108060, '','',6,6,3,'删除', 0),
(10806064,'导入','导入',108060, '','',6,6,4,'导入', 0),
(10806065,'导出','导出',108060, '','',6,6,5,'导出', 0),
(1045,'终端管理','终端管理',10, '/terminal','',2,2,15,'终端管理', 0),
(104510,'终端信息','终端信息',1045, '/terminal/ter_info','',3,3,1,'终端信息', 0),
(10451010,'查询','查询',104510, '','',6,6,0,'查询', 0),
(10451011,'新增','新增',104510, '','',6,6,1,'新增', 0),
(10451012,'编辑','编辑',104510, '','',6,6,2,'编辑', 0),
(10451013,'删除','删除',104510, '','',6,6,3,'删除', 0),
(10451014,'参数设置','参数设置',104510, '','',6,6,4,'参数设置', 0),
(10451015,'终端检测','终端检测',104510, '','',6,6,5,'终端检测', 0),
(10451016,'导入','导入',104510, '','',6,6,6,'导入', 0),
(10451017,'导出','导出',104510, '','',6,6,7,'导出', 0),
(104520,'SIM卡信息','SIM卡信息',1045, '/terminal/sim_info','',3,3,2,'SIM卡信息', 0),
(10452010,'查询','查询',104520, '','',6,6,0,'查询', 0),
(10452021,'新增','新增',104520, '','',6,6,1,'新增', 0),
(10452022,'编辑','编辑',104520, '','',6,6,2,'编辑', 0),
(10452023,'删除','删除',104520, '','',6,6,3,'删除', 0),
(10452024,'废弃','废弃',104520, '','',6,6,4,'废弃', 0),
(10452025,'导入','导入',104520, '','',6,6,5,'导入', 0),
(10452026,'导出','导出',104520, '','',6,6,6,'导出', 0),
(104530,'终端版本管理','终端版本管理',1045, '/terminal/ter_version','',3,3,3,'终端版本管理', 0),
(10453030,'查询','查询',104530, '','',6,6,0,'查询', 0),
(10453031,'新增','新增',104530, '','',6,6,1,'新增', 0),
(10453032,'编辑','编辑',104530, '','',6,6,2,'编辑', 0),
(10453033,'删除','删除',104530, '','',6,6,3,'删除', 0),
(104540,'功能集管理','功能集管理',1045, '/terminal/feature','',3,3,4,'功能集管理', 0),
(10454040,'查询','查询',104540, '','',6,6,0,'查询', 0),
(10454041,'新增','新增',104540, '','',6,6,1,'新增', 0),
(10454042,'编辑','编辑',104540, '','',6,6,2,'编辑', 0),
(10454043,'删除','删除',104540, '','',6,6,3,'删除', 0),
(10454044,'配置功能集','配置功能集',104540, '','',6,6,4,'配置功能集', 0),
(104550,'报文查询','报文查询',1045, '/terminal/mes_query','',3,3,5,'报文查询', 0),
(10455050,'查询','查询',104550, '','',6,6,0,'查询', 0),
(10455051,'导出','导出',104550, '','',6,6,1,'导出', 0),
(1090,'系统管理','系统管理',10, '/system','',2,2,16,'系统管理', 0),
(109010,'用户管理','用户管理',1090, '/system/user','',3,3,1,'用户管理', 0),
(10901010,'查询','查询',109010, '','',6,6,1,'查询', 0),
(10901011,'新增','新增',109010, '','',6,6,2,'新增', 0),
(10901012,'修改','修改',109010, '','',6,6,3,'修改', 0),
(10901013,'删除','删除',109010, '','',6,6,4,'删除', 0),
(10901020,'重置密码','重置密码',109010, '','',6,6,5,'重置密码', 0),
(10901021,'导出','导出',109010, '','',6,6,6,'导出', 0),
(109020,'机构管理','角色管理',1090, '/system/role','',3,3,1,'角色管理', 0),
(10902010,'查询','查询',109020, '','',6,6,1,'查询', 0),
(10902011,'新增','新增',109020, '','',6,6,2,'新增', 0),
(10902012,'修改','修改',109020, '','',6,6,3,'修改', 0),
(10902013,'删除','删除',109020, '','',6,6,4,'删除', 0),
(10902014,'授权','授权',109020, '','',6,6,5,'授权', 0),
(109030,'用户管理','机构管理',1090, '/system/organization','',3,3,2,'机构管理', 0),
(10903010,'查询','查询',109030, '','',6,6,1,'查询', 0),
(10903011,'新增','新增',109030, '','',6,6,2,'新增', 0),
(10903012,'修改','修改',109030, '','',6,6,3,'修改', 0),
(10903013,'删除','删除',109030, '','',6,6,4,'删除', 0),
(109040,'权限管理','融资机构管理',1090, '/system/finance','',3,3,3,'融资机构管理', 0),
(10904010,'查询','查询',109040, '','',6,6,1,'查询', 0),
(10904011,'新增','新增',109040, '','',6,6,2,'新增', 0),
(10904012,'修改','修改',109040, '','',6,6,3,'修改', 0),
(10904013,'删除','删除',109040, '','',6,6,4,'删除', 0),
(109050,'权限管理','客户管理',1090, '/system/customer','',3,3,3,'客户管理', 0),
(10905010,'查询','查询',109050, '','',6,6,1,'查询', 0),
(10905011,'新增','新增',109050, '','',6,6,2,'新增', 0),
(10905012,'修改','修改',109050, '','',6,6,3,'修改', 0),
(10905013,'删除','删除',109050, '','',6,6,4,'删除', 0),
(109060,'系统日志','系统日志',1090, '/system/log','',3,3,4,'系统日志', 0),
(109070,'业务日志','业务日志',1090, '','',3,3,5,'业务日志', 0),
(1095,'单车信息','单车信息',10, '/bicycle','',2,2,17,'单车信息', 0),
(109511,'基本信息','基本信息',1095, '','',3,3,1,'基本信息', 0),
(109512,'当前工况','当前工况',1095, '','',3,3,2,'当前工况', 0),
(109513,'当前故障','当前故障',1095, '','',3,3,3,'当前故障', 0),
(109514,'轨迹回放','轨迹回放',1095, '','',3,3,4,'轨迹回放', 0),
(109515,'参数设置','参数设置',1095, '','',3,3,5,'参数设置', 0),
(109516,'指令日志','指令日志',1095, '','',3,3,6,'指令日志', 0),
(1096,'其他','其他',10, '','',2,2,18,'其他', 0),
(109610,'经纬度','经纬度',1096, '','',6,6,1,'经纬度', 0),
(109611,'位置','位置',1096, '','',6,6,2,'位置', 0),
(109612,'修改密码','修改密码',1096, '','',6,6,3,'修改密码', 0),
(109613,'停车场','停车场',1096, '','',6,6,4,'停车场', 0),
(109614,'','修改语言',1096, '','',6,6,5,'修改语言', 0),
(109615,'','切换主题',1096, '','',6,6,6,'切换主题', 0),
(109616,'','退出',1096, '','',6,6,7,'退出', 0);
INSERT INTO `base_dictionary`(`ID`,`ITEM_CODE`, ITEM_NAME, SORT_CODE)
 VALUES (1000,'ORG_TYPE','组织类型',1);
INSERT INTO `base_dic_item`(`ID`, DIC_CODE, `ITEM_CODE`, ITEM_NAME, SORT_CODE)
 VALUES (1000,'ORG_TYPE','SC','生产',1),
(1001,'ORG_TYPE','ZZ','制造',2),
(1002,'ORG_TYPE','XS','销售',3),
(1003,'ORG_TYPE','FW','服务',4),
(1004,'ORG_TYPE','WX','维修站',5),
(1005,'ORG_TYPE','JX','经销商',6),
(1006,'ORG_TYPE','KH','大客户',7),
(1007,'ORG_TYPE','RZ','融资',8);
