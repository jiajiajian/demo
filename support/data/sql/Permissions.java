package cn.com.tiza.constant;
/** 
* 权限定义
* @author tiza
*/
public final class Permissions {
    private Permissions() {}
}
/** 首页  */
public static final class Main  {
    private Main()  {}
	public static final class dataExport  {
	    public static final int VALUE = 101011;
	    public static final String DESCRIPTION = "首页-数据导出";
	    private dataExport()  {}
	}
}
/** 生产调试  */
public static final class Debug  {
    private Debug()  {}
	public static final class debug  {
	    public static final int VALUE = 10201011;
	    public static final String DESCRIPTION = "生产调试-调试";
	    private debug()  {}
	}
	public static final class addDebugCar  {
	    public static final int VALUE = 10201012;
	    public static final String DESCRIPTION = "生产调试-新增调试车";
	    private addDebugCar()  {}
	}
	public static final class deleteDebugCar  {
	    public static final int VALUE = 10201013;
	    public static final String DESCRIPTION = "生产调试-删除调试车";
	    private deleteDebugCar()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10201014;
	    public static final String DESCRIPTION = "生产调试-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10202021;
	    public static final String DESCRIPTION = "生产调试-导出";
	    private Export()  {}
	}
}
/** 车辆监控  */
public static final class Vehicle  {
    private Vehicle()  {}
	public static final class Export  {
	    public static final int VALUE = 10211011;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10211021;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10211031;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10981010;
	    public static final String DESCRIPTION = "车辆监控-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10981011;
	    public static final String DESCRIPTION = "车辆监控-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10981012;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10981110;
	    public static final String DESCRIPTION = "车辆监控-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10981111;
	    public static final String DESCRIPTION = "车辆监控-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10981112;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10981210;
	    public static final String DESCRIPTION = "车辆监控-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10981211;
	    public static final String DESCRIPTION = "车辆监控-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10981212;
	    public static final String DESCRIPTION = "车辆监控-导出";
	    private Export()  {}
	}
}
/** 故障报警  */
public static final class Alarm  {
    private Alarm()  {}
}
/** 车辆报警  */
public static final class AlarmHistory  {
    private AlarmHistory()  {}
	public static final class Export  {
	    public static final int VALUE = 10301011;
	    public static final String DESCRIPTION = "车辆报警-导出";
	    private Export()  {}
	}
}
/** 车辆故障  */
public static final class AlarmHistory  {
    private AlarmHistory()  {}
	public static final class Export  {
	    public static final int VALUE = 10302021;
	    public static final String DESCRIPTION = "车辆故障-导出";
	    private Export()  {}
	}
}
/** 通知策略  */
public static final class NoticeStrategy  {
    private NoticeStrategy()  {}
	public static final class Update  {
	    public static final int VALUE = 10303031;
	    public static final String DESCRIPTION = "通知策略-编辑";
	    private Update()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10991010;
	    public static final String DESCRIPTION = "通知策略-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10991011;
	    public static final String DESCRIPTION = "通知策略-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10991012;
	    public static final String DESCRIPTION = "通知策略-删除";
	    private Delete()  {}
	}
	public static final class relateVehicles  {
	    public static final int VALUE = 10991013;
	    public static final String DESCRIPTION = "通知策略-关联车辆";
	    private relateVehicles()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10991110;
	    public static final String DESCRIPTION = "通知策略-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10991111;
	    public static final String DESCRIPTION = "通知策略-重置";
	    private reset()  {}
	}
}
/** 远程锁车  */
public static final class RemoteLock  {
    private RemoteLock()  {}
	public static final class Query  {
	    public static final int VALUE = 10401010;
	    public static final String DESCRIPTION = "远程锁车-查询";
	    private Query()  {}
	}
	public static final class RemoteControl  {
	    public static final int VALUE = 10401011;
	    public static final String DESCRIPTION = "远程锁车-远程控制";
	    private RemoteControl()  {}
	}
	public static final class AddControlVehicle  {
	    public static final int VALUE = 10401012;
	    public static final String DESCRIPTION = "远程锁车-新增控制车";
	    private AddControlVehicle()  {}
	}
	public static final class DeleteControlVehicle  {
	    public static final int VALUE = 10401013;
	    public static final String DESCRIPTION = "远程锁车-删除控制车";
	    private DeleteControlVehicle()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10401014;
	    public static final String DESCRIPTION = "远程锁车-导出";
	    private Export()  {}
	}
	public static final class CmdHistory  {
	    public static final int VALUE = 10401015;
	    public static final String DESCRIPTION = "远程锁车-指令历史";
	    private CmdHistory()  {}
	}
}
/** 远程锁车日志  */
public static final class RemoteLockLog  {
    private RemoteLockLog()  {}
	public static final class Query  {
	    public static final int VALUE = 10402010;
	    public static final String DESCRIPTION = "远程锁车日志-查询";
	    private Query()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10402011;
	    public static final String DESCRIPTION = "远程锁车日志-导出";
	    private Export()  {}
	}
}
/** 审批锁车  */
public static final class ApprovalLock  {
    private ApprovalLock()  {}
	public static final class Query  {
	    public static final int VALUE = 10403010;
	    public static final String DESCRIPTION = "审批锁车-查询";
	    private Query()  {}
	}
	public static final class apply  {
	    public static final int VALUE = 10403011;
	    public static final String DESCRIPTION = "审批锁车-申请";
	    private apply()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10403012;
	    public static final String DESCRIPTION = "审批锁车-删除";
	    private Delete()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10403013;
	    public static final String DESCRIPTION = "审批锁车-修改";
	    private Update()  {}
	}
	public static final class run  {
	    public static final int VALUE = 10403014;
	    public static final String DESCRIPTION = "审批锁车-执行";
	    private run()  {}
	}
}
/** 审批锁车日志  */
public static final class ApprovalLockLog  {
    private ApprovalLockLog()  {}
	public static final class Query  {
	    public static final int VALUE = 10404010;
	    public static final String DESCRIPTION = "审批锁车日志-查询";
	    private Query()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10404011;
	    public static final String DESCRIPTION = "审批锁车日志-导出";
	    private Export()  {}
	}
}
/** 围栏策略  */
public static final class Fence  {
    private Fence()  {}
	public static final class Create  {
	    public static final int VALUE = 10251011;
	    public static final String DESCRIPTION = "围栏策略-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10251012;
	    public static final String DESCRIPTION = "围栏策略-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10251013;
	    public static final String DESCRIPTION = "围栏策略-删除";
	    private Delete()  {}
	}
	public static final class relateVehicles  {
	    public static final int VALUE = 10251014;
	    public static final String DESCRIPTION = "围栏策略-关联车辆";
	    private relateVehicles()  {}
	}
	public static final class unRelateVehicles  {
	    public static final int VALUE = 10251015;
	    public static final String DESCRIPTION = "围栏策略-解除关联";
	    private unRelateVehicles()  {}
	}
}
/** 围栏报警  */
public static final class AlarmHistory  {
    private AlarmHistory()  {}
	public static final class Export  {
	    public static final int VALUE = 10252021;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10253031;
	    public static final String DESCRIPTION = "围栏报警-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10253032;
	    public static final String DESCRIPTION = "围栏报警-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10253033;
	    public static final String DESCRIPTION = "围栏报警-删除";
	    private Delete()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10253034;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10254041;
	    public static final String DESCRIPTION = "围栏报警-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10254042;
	    public static final String DESCRIPTION = "围栏报警-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10254043;
	    public static final String DESCRIPTION = "围栏报警-删除";
	    private Delete()  {}
	}
	public static final class maintenanceSetting  {
	    public static final int VALUE = 10254044;
	    public static final String DESCRIPTION = "围栏报警-保养设置";
	    private maintenanceSetting()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10254045;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class maintenanceManage  {
	    public static final int VALUE = 10255051;
	    public static final String DESCRIPTION = "围栏报警-保养管理";
	    private maintenanceManage()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10701011;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10702021;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10703031;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10704041;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10705051;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971010;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971011;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971012;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class workTimeStat  {
	    public static final int VALUE = 10971013;
	    public static final String DESCRIPTION = "围栏报警-工作时长统计";
	    private workTimeStat()  {}
	}
	public static final class engineStatus  {
	    public static final int VALUE = 10971014;
	    public static final String DESCRIPTION = "围栏报警-发动机转速/状态统计";
	    private engineStatus()  {}
	}
	public static final class temperatureProfile  {
	    public static final int VALUE = 10971015;
	    public static final String DESCRIPTION = "围栏报警-温度曲线图";
	    private temperatureProfile()  {}
	}
	public static final class fuelConsumption  {
	    public static final int VALUE = 10971016;
	    public static final String DESCRIPTION = "围栏报警-油耗/油位曲线图";
	    private fuelConsumption()  {}
	}
	public static final class powerStatistics  {
	    public static final int VALUE = 10971015;
	    public static final String DESCRIPTION = "围栏报警-功率/工作模式统计";
	    private powerStatistics()  {}
	}
	public static final class fanControl  {
	    public static final int VALUE = 10971018;
	    public static final String DESCRIPTION = "围栏报警-风扇控制";
	    private fanControl()  {}
	}
	public static final class drivingStatusStat  {
	    public static final int VALUE = 10971019;
	    public static final String DESCRIPTION = "围栏报警-挖掘/行驶状态统计";
	    private drivingStatusStat()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971110;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971111;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971210;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971211;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971212;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971310;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971311;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971312;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971410;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971411;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971412;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971510;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971511;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971512;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971610;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971611;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971612;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971710;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971711;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971712;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971810;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971811;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971812;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10971910;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class reset  {
	    public static final int VALUE = 10971911;
	    public static final String DESCRIPTION = "围栏报警-重置";
	    private reset()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10971912;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10711010;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10711011;
	    public static final String DESCRIPTION = "围栏报警-新增";
	    private Create()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10711012;
	    public static final String DESCRIPTION = "围栏报警-删除";
	    private Delete()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10711013;
	    public static final String DESCRIPTION = "围栏报警-修改";
	    private Update()  {}
	}
	public static final class hide  {
	    public static final int VALUE = 10711014;
	    public static final String DESCRIPTION = "围栏报警-封存";
	    private hide()  {}
	}
	public static final class hangUp  {
	    public static final int VALUE = 10711015;
	    public static final String DESCRIPTION = "围栏报警-解封";
	    private hangUp()  {}
	}
	public static final class Query  {
	    public static final int VALUE = 10713010;
	    public static final String DESCRIPTION = "围栏报警-查询";
	    private Query()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10721011;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10722011;
	    public static final String DESCRIPTION = "围栏报警-导出";
	    private Export()  {}
	}
}
/** 结算费用设置  */
public static final class Cahrge  {
    private Cahrge()  {}
	public static final class Create  {
	    public static final int VALUE = 10723011;
	    public static final String DESCRIPTION = "结算费用设置-新增";
	    private Create()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10723012;
	    public static final String DESCRIPTION = "结算费用设置-删除";
	    private Delete()  {}
	}
	public static final class addChargeConfig  {
	    public static final int VALUE = 10723013;
	    public static final String DESCRIPTION = "结算费用设置-添加费用配置项";
	    private addChargeConfig()  {}
	}
	public static final class editChargeConfig  {
	    public static final int VALUE = 10723014;
	    public static final String DESCRIPTION = "结算费用设置-删除费用配置项";
	    private editChargeConfig()  {}
	}
	public static final class delChargeConfig  {
	    public static final int VALUE = 10723015;
	    public static final String DESCRIPTION = "结算费用设置-修改费用配置项";
	    private delChargeConfig()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10724011;
	    public static final String DESCRIPTION = "结算费用设置-导出";
	    private Export()  {}
	}
	public static final class Record  {
	    public static final int VALUE = 101001010;
	    public static final String DESCRIPTION = "结算费用设置-备案";
	    private Record()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 101001011;
	    public static final String DESCRIPTION = "结算费用设置-导出";
	    private Export()  {}
	}
	public static final class terminalRecord  {
	    public static final int VALUE = 101001110;
	    public static final String DESCRIPTION = "结算费用设置-终端型号授权备案";
	    private terminalRecord()  {}
	}
	public static final class terminalTypeRecord  {
	    public static final int VALUE = 101001111;
	    public static final String DESCRIPTION = "结算费用设置-终端型号备案";
	    private terminalTypeRecord()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 101001112;
	    public static final String DESCRIPTION = "结算费用设置-导出";
	    private Export()  {}
	}
	public static final class chipTypeRecord  {
	    public static final int VALUE = 101001210;
	    public static final String DESCRIPTION = "结算费用设置-芯片型号备案";
	    private chipTypeRecord()  {}
	}
	public static final class ddentifierRequest  {
	    public static final int VALUE = 101001211;
	    public static final String DESCRIPTION = "结算费用设置-标识符申请";
	    private ddentifierRequest()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 101001212;
	    public static final String DESCRIPTION = "结算费用设置-导出";
	    private Export()  {}
	}
	public static final class Record  {
	    public static final int VALUE = 101001310;
	    public static final String DESCRIPTION = "结算费用设置-备案";
	    private Record()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 101001311;
	    public static final String DESCRIPTION = "结算费用设置-导出";
	    private Export()  {}
	}
}
/** 车辆类型  */
public static final class VehicleType  {
    private VehicleType()  {}
	public static final class Create  {
	    public static final int VALUE = 10801011;
	    public static final String DESCRIPTION = "车辆类型-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10801012;
	    public static final String DESCRIPTION = "车辆类型-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10801013;
	    public static final String DESCRIPTION = "车辆类型-删除";
	    private Delete()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10801014;
	    public static final String DESCRIPTION = "车辆类型-导出";
	    private Export()  {}
	}
}
/** 车辆型号  */
public static final class VehicleModel  {
    private VehicleModel()  {}
	public static final class Create  {
	    public static final int VALUE = 10802021;
	    public static final String DESCRIPTION = "车辆型号-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10802022;
	    public static final String DESCRIPTION = "车辆型号-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10802023;
	    public static final String DESCRIPTION = "车辆型号-删除";
	    private Delete()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10802024;
	    public static final String DESCRIPTION = "车辆型号-导出";
	    private Export()  {}
	}
}
/** 车辆信息  */
public static final class Vehicle  {
    private Vehicle()  {}
	public static final class BatchQuery  {
	    public static final int VALUE = 10803031;
	    public static final String DESCRIPTION = "车辆信息-批量查询";
	    private BatchQuery()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10803032;
	    public static final String DESCRIPTION = "车辆信息-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10803033;
	    public static final String DESCRIPTION = "车辆信息-删除";
	    private Delete()  {}
	}
	public static final class EditSaleInfo  {
	    public static final int VALUE = 10803034;
	    public static final String DESCRIPTION = "车辆信息-销售信息编辑";
	    private EditSaleInfo()  {}
	}
	public static final class updateDebugTime  {
	    public static final int VALUE = 10803035;
	    public static final String DESCRIPTION = "车辆信息-修改调试时间";
	    private updateDebugTime()  {}
	}
	public static final class updateServiceStatus  {
	    public static final int VALUE = 10803036;
	    public static final String DESCRIPTION = "车辆信息-修改服务状态";
	    private updateServiceStatus()  {}
	}
	public static final class changeTerminal  {
	    public static final int VALUE = 10803037;
	    public static final String DESCRIPTION = "车辆信息-更换终端";
	    private changeTerminal()  {}
	}
	public static final class swapTerminal  {
	    public static final int VALUE = 10803038;
	    public static final String DESCRIPTION = "车辆信息-双车互换终端";
	    private swapTerminal()  {}
	}
	public static final class moveVin  {
	    public static final int VALUE = 10803039;
	    public static final String DESCRIPTION = "车辆信息-移机";
	    private moveVin()  {}
	}
	public static final class updateServiceDate  {
	    public static final int VALUE = 10803040;
	    public static final String DESCRIPTION = "车辆信息-延长服务期";
	    private updateServiceDate()  {}
	}
	public static final class suspendedVehicleService  {
	    public static final int VALUE = 10803041;
	    public static final String DESCRIPTION = "车辆信息-暂停";
	    private suspendedVehicleService()  {}
	}
	public static final class vehicleServiceRestore  {
	    public static final int VALUE = 10803042;
	    public static final String DESCRIPTION = "车辆信息-暂停恢复";
	    private vehicleServiceRestore()  {}
	}
	public static final class closeAccount  {
	    public static final int VALUE = 10803043;
	    public static final String DESCRIPTION = "车辆信息-销户";
	    private closeAccount()  {}
	}
	public static final class Import  {
	    public static final int VALUE = 10803044;
	    public static final String DESCRIPTION = "车辆信息-导入";
	    private Import()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10803045;
	    public static final String DESCRIPTION = "车辆信息-导出";
	    private Export()  {}
	}
}
/** 报警项配置  */
public static final class AlarmItem  {
    private AlarmItem()  {}
	public static final class Create  {
	    public static final int VALUE = 10804041;
	    public static final String DESCRIPTION = "报警项配置-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10804042;
	    public static final String DESCRIPTION = "报警项配置-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10804043;
	    public static final String DESCRIPTION = "报警项配置-删除";
	    private Delete()  {}
	}
}
/** 调试项配置  */
public static final class CmdDebug  {
    private CmdDebug()  {}
	public static final class Create  {
	    public static final int VALUE = 10805051;
	    public static final String DESCRIPTION = "调试项配置-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10805052;
	    public static final String DESCRIPTION = "调试项配置-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10805053;
	    public static final String DESCRIPTION = "调试项配置-删除";
	    private Delete()  {}
	}
}
/** 故障字典项配置  */
public static final class FaultDict  {
    private FaultDict()  {}
	public static final class Create  {
	    public static final int VALUE = 10806061;
	    public static final String DESCRIPTION = "故障字典项配置-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10806062;
	    public static final String DESCRIPTION = "故障字典项配置-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10806063;
	    public static final String DESCRIPTION = "故障字典项配置-删除";
	    private Delete()  {}
	}
	public static final class Import  {
	    public static final int VALUE = 10806064;
	    public static final String DESCRIPTION = "故障字典项配置-导入";
	    private Import()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10806065;
	    public static final String DESCRIPTION = "故障字典项配置-导出";
	    private Export()  {}
	}
}
/** 终端信息  */
public static final class Terminal  {
    private Terminal()  {}
	public static final class Query  {
	    public static final int VALUE = 10451010;
	    public static final String DESCRIPTION = "终端信息-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10451011;
	    public static final String DESCRIPTION = "终端信息-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10451012;
	    public static final String DESCRIPTION = "终端信息-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10451013;
	    public static final String DESCRIPTION = "终端信息-删除";
	    private Delete()  {}
	}
	public static final class parameterSetting  {
	    public static final int VALUE = 10451014;
	    public static final String DESCRIPTION = "终端信息-参数设置";
	    private parameterSetting()  {}
	}
	public static final class TerminalCheck  {
	    public static final int VALUE = 10451015;
	    public static final String DESCRIPTION = "终端信息-终端检测";
	    private TerminalCheck()  {}
	}
	public static final class Import  {
	    public static final int VALUE = 10451016;
	    public static final String DESCRIPTION = "终端信息-导入";
	    private Import()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10451017;
	    public static final String DESCRIPTION = "终端信息-导出";
	    private Export()  {}
	}
}
/** SIM卡信息  */
public static final class SIM  {
    private SIM()  {}
	public static final class Query  {
	    public static final int VALUE = 10452010;
	    public static final String DESCRIPTION = "SIM卡信息-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10452021;
	    public static final String DESCRIPTION = "SIM卡信息-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10452022;
	    public static final String DESCRIPTION = "SIM卡信息-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10452023;
	    public static final String DESCRIPTION = "SIM卡信息-删除";
	    private Delete()  {}
	}
	public static final class Abandon  {
	    public static final int VALUE = 10452024;
	    public static final String DESCRIPTION = "SIM卡信息-废弃";
	    private Abandon()  {}
	}
	public static final class Import  {
	    public static final int VALUE = 10452025;
	    public static final String DESCRIPTION = "SIM卡信息-导入";
	    private Import()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10452026;
	    public static final String DESCRIPTION = "SIM卡信息-导出";
	    private Export()  {}
	}
}
/** 终端版本管理  */
public static final class TerminalVersion  {
    private TerminalVersion()  {}
	public static final class Query  {
	    public static final int VALUE = 10453030;
	    public static final String DESCRIPTION = "终端版本管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10453031;
	    public static final String DESCRIPTION = "终端版本管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10453032;
	    public static final String DESCRIPTION = "终端版本管理-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10453033;
	    public static final String DESCRIPTION = "终端版本管理-删除";
	    private Delete()  {}
	}
}
/** 功能集管理  */
public static final class FunctionSet  {
    private FunctionSet()  {}
	public static final class Query  {
	    public static final int VALUE = 10454040;
	    public static final String DESCRIPTION = "功能集管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10454041;
	    public static final String DESCRIPTION = "功能集管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10454042;
	    public static final String DESCRIPTION = "功能集管理-编辑";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10454043;
	    public static final String DESCRIPTION = "功能集管理-删除";
	    private Delete()  {}
	}
	public static final class ConfigFunctionSet  {
	    public static final int VALUE = 10454044;
	    public static final String DESCRIPTION = "功能集管理-配置功能集";
	    private ConfigFunctionSet()  {}
	}
}
/** 报文查询  */
public static final class ReportQuery  {
    private ReportQuery()  {}
	public static final class Query  {
	    public static final int VALUE = 10455050;
	    public static final String DESCRIPTION = "报文查询-查询";
	    private Query()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10455051;
	    public static final String DESCRIPTION = "报文查询-导出";
	    private Export()  {}
	}
}
/** 用户管理  */
public static final class User  {
    private User()  {}
	public static final class Query  {
	    public static final int VALUE = 10901010;
	    public static final String DESCRIPTION = "用户管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10901011;
	    public static final String DESCRIPTION = "用户管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10901012;
	    public static final String DESCRIPTION = "用户管理-修改";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10901013;
	    public static final String DESCRIPTION = "用户管理-删除";
	    private Delete()  {}
	}
	public static final class ResetPwd  {
	    public static final int VALUE = 10901020;
	    public static final String DESCRIPTION = "用户管理-重置密码";
	    private ResetPwd()  {}
	}
	public static final class Export  {
	    public static final int VALUE = 10901021;
	    public static final String DESCRIPTION = "用户管理-导出";
	    private Export()  {}
	}
}
/** 角色管理  */
public static final class Role  {
    private Role()  {}
	public static final class Query  {
	    public static final int VALUE = 10902010;
	    public static final String DESCRIPTION = "角色管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10902011;
	    public static final String DESCRIPTION = "角色管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10902012;
	    public static final String DESCRIPTION = "角色管理-修改";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10902013;
	    public static final String DESCRIPTION = "角色管理-删除";
	    private Delete()  {}
	}
	public static final class Auth  {
	    public static final int VALUE = 10902014;
	    public static final String DESCRIPTION = "角色管理-授权";
	    private Auth()  {}
	}
}
/** 机构管理  */
public static final class Organization  {
    private Organization()  {}
	public static final class Query  {
	    public static final int VALUE = 10903010;
	    public static final String DESCRIPTION = "机构管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10903011;
	    public static final String DESCRIPTION = "机构管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10903012;
	    public static final String DESCRIPTION = "机构管理-修改";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10903013;
	    public static final String DESCRIPTION = "机构管理-删除";
	    private Delete()  {}
	}
}
/** 融资机构管理  */
public static final class Finance  {
    private Finance()  {}
	public static final class Query  {
	    public static final int VALUE = 10904010;
	    public static final String DESCRIPTION = "融资机构管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10904011;
	    public static final String DESCRIPTION = "融资机构管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10904012;
	    public static final String DESCRIPTION = "融资机构管理-修改";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10904013;
	    public static final String DESCRIPTION = "融资机构管理-删除";
	    private Delete()  {}
	}
}
/** 客户管理  */
public static final class Customer  {
    private Customer()  {}
	public static final class Query  {
	    public static final int VALUE = 10905010;
	    public static final String DESCRIPTION = "客户管理-查询";
	    private Query()  {}
	}
	public static final class Create  {
	    public static final int VALUE = 10905011;
	    public static final String DESCRIPTION = "客户管理-新增";
	    private Create()  {}
	}
	public static final class Update  {
	    public static final int VALUE = 10905012;
	    public static final String DESCRIPTION = "客户管理-修改";
	    private Update()  {}
	}
	public static final class Delete  {
	    public static final int VALUE = 10905013;
	    public static final String DESCRIPTION = "客户管理-删除";
	    private Delete()  {}
	}
}
/** 系统日志  */
public static final class SystemLog  {
    private SystemLog()  {}
}
/** 单车信息  */
public static final class Single  {
    private Single()  {}
}
/** 其他  */
public static final class Other  {
    private Other()  {}
	public static final class lon_lat  {
	    public static final int VALUE = 109610;
	    public static final String DESCRIPTION = "其他-经纬度";
	    private lon_lat()  {}
	}
	public static final class location  {
	    public static final int VALUE = 109611;
	    public static final String DESCRIPTION = "其他-位置";
	    private location()  {}
	}
	public static final class modifyPasswd  {
	    public static final int VALUE = 109612;
	    public static final String DESCRIPTION = "其他-修改密码";
	    private modifyPasswd()  {}
	}
	public static final class parkingLot  {
	    public static final int VALUE = 109613;
	    public static final String DESCRIPTION = "其他-停车场";
	    private parkingLot()  {}
	}
	public static final class changeLang  {
	    public static final int VALUE = 109614;
	    public static final String DESCRIPTION = "其他-修改语言";
	    private changeLang()  {}
	}
	public static final class changeTheme  {
	    public static final int VALUE = 109615;
	    public static final String DESCRIPTION = "其他-切换主题";
	    private changeTheme()  {}
	}
	public static final class logout  {
	    public static final int VALUE = 109616;
	    public static final String DESCRIPTION = "其他-退出";
	    private logout()  {}
	}
	}
}