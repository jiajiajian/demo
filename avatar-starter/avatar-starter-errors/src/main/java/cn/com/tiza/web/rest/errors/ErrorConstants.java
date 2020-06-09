package cn.com.tiza.web.rest.errors;

public interface ErrorConstants {

    String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    String ERR_VALIDATION = "error.validation";
    String ENTITY_NOT_FOUND_TYPE = "entity.not_found";
    String INVALID_PASSWORD_TYPE = "invalid_password";
    String CONSTRAINT_VIOLATION_TYPE = "constraint_violation";
    String PARAMETERIZED_TYPE = "parameterized";
    String EMAIL_ALREADY_USED_TYPE = "user.email.already_used";
    String LOGIN_ALREADY_USED_TYPE = "user.login.already_used";
    String EMAIL_NOT_FOUND_TYPE = "user.email.not_found";
    String ADMIN_CAN_NOT_BE_DELETED = "admin.can.not.be.deleted";
    String TENANT_CAN_NOT_BE_DELETED = "tenant.can.not.be.deleted";
    String DRIVER_MODEL_ALREADY_BIND = "driver.model.already.bind";
    String ROOT_CANNOT_DELETE = "root.cannot.delete";
    String PASSWORD_NOT_EQUAL = "password.not.equal";
    String PASSWORD_ERROR = "password.error";
    /**
     * 系统管理
     */

    String ROLENAME_ALREADY_USERD_TYPPE = "role.name.already_used";
    String ROLE_HAS_RELATION_WITH_USER_TYPE = "role.hasRelation_withUser";
    String USER_ALREADY_DISABLED = "user.disabled";
    String ORGANIZATION_CAN_NOT_DELETE = "organization.can.not.delete";
    String AUTH_NOT_ALLOW = "auth.not.allow";
    /**
     * 车辆模块
     */
    String VEHICLE_TYPE_NAME_ALREADY_USED = "vehicleTypeName.already_used";
    String VEHICLE_TYPE_NOT_EXIST = "vehicleType.notExist";
    String VEHICLE_TYPE_HAS_RELATION_WITH_VEHICLE_MODEL = "vehicleType.hasRelation_withVehicleModel";
    String VEHICLE_TYPE_CHANGE_ORG__HAS_RELATION_WITH_VEHICLE_MODEL = "vehicleType.changeOrg.hasRelation_withVehicleModel";
    String VEHICLE_MODEL_NAME_ALREADY_USED = "vehicleModelName.already_used";
    String VEHICLE_MODEL_HAS_RELATION_WITH_VEHICLE = "vehicleModel.hasRelation_withVehicle";
    String VEHICLE_TYPE_HAS_RELATION_WITH_CMDDEBUG = "vehicleType.hasRelation_withCmdDebug";
    String VEHICLE_MODEL_NOT_EXIST = "vehicleModel.notExist";
    String ALARM_ITEM_HAS_CREATED_FOR_THIS_ORGANIZATION = "alarmItem.hasCreated_forThisOrganization";
    String CMD_DEBUG_HAS_CREATED_FOR_THIS_ORGANIZATION_VEHICLE_TYPE = "cmdDebug.hasCreated_forThisOrganization_vehicleType";
    String FAULT_DICT_HAS_CREATED = "faultDict.hasCreated";
    String FAULT_DICT_NAME_ALREADY_USED = "faultDictName.already_used";
    String VEHICLE_SERVICE_STATUS_CAN_NOT_DELETE = "vehicleServiceStatus.can_not_delete";
    String FAIL_TO_DELETE_TERMINAL = "fail.delete_terminal";
    String VEHICLE_IS_NOT_IN_DEBUG = "vehicle.isNotDebug";
    String DEBUG_END_TIME_LESS_THAN_START_TIME = "debug.endTime_lessThanStartTime";
    String VEHICLE_NOT_EXIST = "vehicle.not_exist";
    String TERMINAL_HAS_RELATION_WITH_REAL_VEHICLE = "terminal.hasRelation_withRealVehicle";
    String VIN_HAS_EXIST = "vin.hasExist";
    String VIRTUAL_VIN_CAN_NOT_MOVE = "virtualVin.cannot_move";
    String VIRTUAL_VIN_CAN_NOT_CHANGE_TERMINAL = "virtualVin.cannot_change_terminal";
    String NOT_HAVE_PERMISSION_FOR_VIN = "notHavePermission_forVin";
    String CAN_NOT_CHANGE_ROOT_ORG = "cannot.change_rootOrg";
    String CHARGE_HAS_CREATED = "charge.has_created";
    String CHARGE_DETAIL_DATE_DOUBLICATION = "charge.detail.date_doublication";
    String VEHICLE_DO_NOT_HAVE_PROTOCAL_TYPE = "vehicle.doNotHave_protocalType";
    String VEHICLE_DO_NOT_FIND_API_KEY = "vehicle.doNotFind_ApiKey";
    String FAULT_DICT_ITEM_CAN_NOT_BE_CREAT = "faultDictItem.cannotBe_created";
    /**
     * 报警模块
     */
    String PRESENT_ORG_IS_NOT_ROOT_ORG = "presentOrg_isNotRootOrg";
    String ADMIN_CAN_NOT_UPDATE_NOTICE = "admin_cannot_updateNotice";
    String FENCE_NAME_EXIST = "fenceName_exist";
    String ADMIN_CAN_NOT_OPERATE_FENCE = "admin_cannot_operateFence";
    String TLA_NAME_EXIST = "tla.name_exist";
    String TLA_ID_EXIST = "tla.id_exist";
    String REMIND_WAY_SIZE_ZERO = "remindWay_size_zero";


    /**
     * 共通
     * */
    /**
     * 导入文件出错
     */
    String IMPORT_EXCEL_FILE_ERROR = "import.excel.file.error";

    /**
     * 终端不存在
     */
    String TERMINAL_NOT_EXIST = "terminal.notExist";


    /**
     * 维保服务
     */
    String MAINTENANCE_PLAN_ALREADY_EXIST = "MAINTENANCE_PLAN_ALREADY_EXIST";
    String MAINTENANCE_PLAN_NOT_EXIST = "MAINTENANCE_PLAN_NOT_EXIST";
    String VEHICLE_MODEL_MAINTENANCE_PLAN_EXIST = "VEHICLE_MODEL_MAINTENANCE_PLAN_EXIST";

    String AREA_AND_MODEL_REPEAT = "area.and.model.repeat";
    String DO_NOT_HAVE_PERMISSION_FOR_THIS_VIN = "notHave.permission_withThisVin";

    /**
     * 单车分析
     */
    String VEHICLE_HAS_NO_FUNCTION="vehicle.has.no.function";

}
