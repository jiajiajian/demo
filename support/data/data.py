import xlrd
import os
import logging

# logger = logging.getLogger("datatosql")
# logger.setLevel(logging.DEBUG)

def writeFile(str, folder, fileName) :
    if os.path.exists(folder) :
        folder += "/"
    else :
        idx = folder.find("/")
        print(idx)
        if idx > 0 :
            parent = folder[0: idx]
            if os.path.exists(parent) == False :
                os.mkdir(parent)
        os.mkdir(folder)
        folder += "/"
    fileName = folder + fileName

    if os.path.exists(fileName) :
        os.remove(fileName)

    fw = open(fileName,"w", encoding="utf-8") #打开要写入的文件
    fw.write(str) #写入文件
    fw.close()

def insertFunctionDataSql(sheet) :
    tableName = sheet.name    
    print(tableName)
    print("write insert sql for base_sys_function")
    sql = "INSERT INTO `base_sys_function`(`ID`, `FUNCTION_CODE`, `FUNCTION_NAME`, `PARENT_FUNCTION_ID`,  `NAVI_URL`, `ICON`, `LAYER_LEVEL`, `FUNCTION_TYPE`, `SORT_CODE`, `REMARK`, `IS_NEW_PAGE`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    # print(nrows)
    # logger.info(nrows)
    valueStr = "({0},'{1}','{2}',{3}, '{4}','{5}',{6},{7},{8},'{9}', {10})"
    for i in range(1, nrows) :
        rowvalues = sheet.row_values(i)
        print(rowvalues)
        # logger.info(row)
        colIdx = 0
        _id = int(sheet.cell_value(i, colIdx))
        colIdx += 2
        _parentId = int(sheet.cell_value(i, colIdx))
        colIdx += 1
        _name = sheet.cell_value(i, colIdx)
        colIdx += 1
        _level = int(sheet.cell_value(i, colIdx))
        colIdx += 2
        _orderNum = int(sheet.cell_value(i, colIdx))
        colIdx += 1
        _url = sheet.cell_value(i, colIdx)
        colIdx += 1
        _icon = sheet.cell_value(i, colIdx)
        colIdx += 1
        _i18n = sheet.cell_value(i, colIdx)
        sql += valueStr.format(_id, _i18n, _name, _parentId,  _url, _icon, _level, _level, _orderNum, _name, 0)
        sql += ",\n"
    print(sql)
    return sql[0:len(sql) - 2] + ";\n"

def insertUserSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `t_user`(`LOGIN_NAME`,`NAME`, PASSWORD, SALT, `ENABLED`, ORG_ID, ROLE_ID, `DELETE_FLAG`)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    valueStr = "({0},'{1}','{2}',{3}, '{4}','{5}',{6},{7},{8},'{9}', {10})"
    for i in range(1, nrows) :
        _name = sheet.row(i)[0].value
        _org = sheet.row(i)[1].value
        _role = sheet.row(i)[2].value
        sql += "('" + _name + "', '" + _name + "', 'EEEC178AA771E4DC913CB4D72B68E837435B2CCD', 'DE56F0259A57992F', 1,"
        sql += "(select id from t_org_unit where name = '" + _org + "')"
        sql += "(select id from t_role where code = '" + _role + "')"
        sql += "0),\n"
        
    return sql[0:len(sql) - 2] + ";\n"


def insertDictSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `base_dictionary`(`ID`,`ITEM_CODE`, ITEM_NAME, SORT_CODE)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    valueStr = "({0},'{1}','{2}',{3})"
    for i in range(1, nrows) :
        colIdx = 0
        _id = int(sheet.cell_value(i, colIdx))
        _code = sheet.row(i)[1].value
        _name = sheet.row(i)[2].value
        _pid = sheet.row(i)[3].value
        
        colIdx = 4
        _sort = int(sheet.cell_value(i, colIdx))
        sql += valueStr.format(_id, _code, _name, _sort)
        sql += ",\n"
        
    return sql[0:len(sql) - 2] + ";\n"


def insertDictItemSql(sheet) :
    tableName = sheet.name
    print("write insert sql for " + tableName)
    sql = "INSERT INTO `base_dic_item`(`ID`, DIC_CODE, `ITEM_CODE`, ITEM_NAME, SORT_CODE)\n"
    sql += " VALUES "
    nrows = sheet.nrows
    valueStr = "({0},'{1}','{2}','{3}',{4})"
    for i in range(1, nrows) :
        colIdx = 0
        _id = int(sheet.cell_value(i, colIdx))
        _code = sheet.row(i)[1].value
        _name = sheet.row(i)[2].value
        _pid = sheet.row(i)[3].value
        
        colIdx = 3
        _pcode = sheet.cell_value(i, colIdx)
        colIdx = 4
        _sort = int(sheet.cell_value(i, colIdx))
        sql += valueStr.format(_id, _pcode, _code, _name, _sort)
        sql += ",\n"
        
    return sql[0:len(sql) - 2] + ";\n"


class Permission(object) :
    def __init__(self, id, name, parentId):
        self.id = id
        self.name = name
        self.parentId = parentId


def genPermission(sheet) :
    tableName = sheet.name
    print("generate permission class")
    nrows = sheet.nrows
    arr = []
    funcs = {
        "查询": "Query",
        "新增": "Create",
        "修改": "Update",
        "编辑": "Update",
        "删除": "Delete",
        "导入": "Import",
        "导出": "Export",
        "下载": "Download",
        "追溯": "Trace",
        "启用/停用": "Active",
        "启动": "Start",
        "处理": "Deal",
        "关联车辆": "Relate",
        "备案": "Record",
        "指令下发": "Command",
        "标识符申请": "Apply",
        "授权": "Auth",
        "保存": "Save",
        "启用": "Enable",
        "禁用": "Disable",
        "重置密码": "ResetPwd",
		"人员关联": "Relate",
		"参数设置": "ParamSet",
		"终端检测": "TerminalCheck",
		"废弃": "Abandon",
		"新增控制车": "AddControlVehicle",
		"删除控制车": "DeleteControlVehicle",
		"远程控制": "RemoteControl",
		"指令历史": "CmdHistory",
		"配置功能集": "ConfigFunctionSet",
		"批量查询":"BatchQuery",
		"销售信息编辑":"EditSaleInfo",
		"修改调试时间":"updateDebugTime",
		"修改服务状态":"updateServiceStatus",
		"更换终端":"changeTerminal",
		"双车互换终端":"swapTerminal",
		"移机":"moveVin",
		"延长服务期":"updateServiceDate",
		"暂停":"suspendedVehicleService",
		"暂停恢复":"vehicleServiceRestore",
		"销户":"closeAccount",
		"关联车辆":"relateVehicles",
		"解除关联":"unRelateVehicles",
		"添加费用配置项":"addChargeConfig",
		"删除费用配置项":"editChargeConfig",
		"修改费用配置项":"delChargeConfig",
		"数据导出":"dataExport",
		"调试":"debug",
		"新增调试车":"addDebugCar",
		"删除调试车":"deleteDebugCar",
		"保养设置":"maintenanceSetting",
		"解封":"hangUp",
		"封存":"hide",
		"申请":"apply",
		"执行":"run",
		"保养管理":"maintenanceManage",
		"车辆信息":"vehicleInfo",
		"当前工况":"realCondition",
		"当前故障":"realFault",
		"轨迹回放":"reTracdata",
		"参数设置":"parameterSetting",
		"经纬度":"lon_lat",
		"位置":"location",
		"修改密码":"modifyPasswd",
		"停车场":"parkingLot",
		"指令日志":"cmdLog",
        "重置":"reset",
        "工作时长统计":"workTimeStat",
        "发动机转速/状态统计":"engineStatus",
        "温度曲线图":"temperatureProfile",
        "油耗/油位曲线图":"fuelConsumption",
        "功率/工作模式统计":"powerStatistics",
        "风扇控制":"fanControl",
        "挖掘/行驶状态统计":"drivingStatusStat",
        "功能":"function",
        "修改语言":"changeLang",
        "切换主题":"changeTheme",
        "退出":"logout",
        "终端型号授权备案":"terminalRecord",
        "终端型号备案":"terminalTypeRecord",
        "芯片型号备案":"chipTypeRecord",
        "标识符申请":"ddentifierRequest"
    }
    clazz = "package cn.com.tiza.constant;\n"
    clazz += "/** \n"
    clazz += "* 权限定义\n"
    clazz += "* @author tiza\n"
    clazz += "*/\n"
    clazz += "public final class Permissions {\n"
    clazz += ("    private Permissions() {}\n")
    module = ""
    for i in range(1, nrows) :
        rowvalues = sheet.row_values(i)
        colIdx = 0
        _id = int(sheet.cell_value(i, colIdx))
        colIdx += 2
        _parentId = int(sheet.cell_value(i, colIdx))
        colIdx += 1
        _name = sheet.cell_value(i, colIdx)
        colIdx += 1
        _level = int(sheet.cell_value(i, colIdx))
        colIdx += 6
        _clazz = sheet.cell_value(i, colIdx)
        if(_clazz) :
            module = _name
            clazz += ("}\n")
            clazz += ("/** {0}  */\n".format(_name) )
            clazz += ("public static final class {0} ".format(_clazz) + " {\n")
            clazz += ("    private {0}() ".format(_clazz) + " {}\n")
        if(_level == 6) :
            p = Permission(_id, _name, _parentId)
            clazz += ("\tpublic static final class {0} ".format(funcs[p.name]) + " {\n")
            clazz += ("\t    public static final int VALUE = {0};\n".format(p.id))
            clazz += ("\t    public static final String DESCRIPTION = \"{0}\";\n".format(module + "-" + p.name))
            clazz += ("\t    private {0}() ".format(funcs[p.name]) + " {}\n")
            clazz += ("\t}\n")
    clazz += ("\t}\n}")
    return clazz


def writeInitData(data) :
    # table = data.sheets()[0] #通过索引顺序获取
    # table = data.sheet_by_index(0) #通过索引顺序获取
    sql = insertFunctionDataSql(data.sheets()[0])
    sql += insertDictSql(data.sheets()[3])
    sql += insertDictItemSql(data.sheets()[4])
    clazz = genPermission(data.sheets()[0])
        # break
    writeFile(sql, "sql", "init.sql")
    writeFile(clazz, "sql", "Permissions.java")


data = xlrd.open_workbook("非4平台数据.xlsx")
writeInitData(data)