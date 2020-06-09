package cn.com.tiza;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 实时数据字段义
 */
public class DataConstants {

    /**
     * 国六发动机数据
     */
    public static final String MDT_EN_DA_GB6_TYPE = "MDT_EN_DA_GB6_TYPE";
    /**
     * 车辆信息配置
     */
    public static final String VEHICLE_INFO_CONFIG = "VEHICLE_INFO_CONFIG";
    /**
     * 车牌颜色
     */
    public static final String LICENSE_COLOR = "LICENSE_COLOR";
    /**
     * 购车领域
     */
    public static final String PURCHASE_RANGE = "PURCHASE_RANGE";
    /**
     * 车辆用途
     */
    public static final String VEHICLE_PURPOSE = "VEHICLE_PURPOSE";
    /**
     * 终端供应商
     */
    public static final String TERMINAL_VENDOR = "TERMINAL_VENDOR";
    /**
     * 新能源信息配置
     */
    public static final String ELECTRIC_INFO_CONFIG = "ELECTRIC_INFO_CONFIG";
    /**
     * 电池种类
     */
    public static final String BATTERY_TYPE = "BATTERY_TYPE";
    /**
     * 驱动电机种类
     */
    public static final String ELECTRIC_DRIVER_TYPE = "ELECTRIC_DRIVER_TYPE";
    /**
     * 驱动电机冷却方式
     */
    public static final String DRIVER_COOL_TYPE = "DRIVER_COOL_TYPE";
    /**
     * 续驶里程检测方式
     */
    public static final String ENDURANCE_CHECK = "ENDURANCE_CHECK";
    /**
     * 监控数据大类
     */
    public static final String MONITOR_DATA_TYPE = "MONITOR_DATA_TYPE";
    /**
     * 整车数据
     */
    public static final String MDT_O_DA_TYPE = "MDT_O_DA_TYPE";
    /**
     * 驱动电机数据
     */
    public static final String MDT_DR_DA_TYPE = "MDT_DR_DA_TYPE";
    /**
     * 发动机数据
     */
    public static final String MDT_EN_DA_TYPE = "MDT_EN_DA_TYPE";
    /**
     * 极值数据
     */
    public static final String MDT_P_DA_TYPE = "MDT_P_DA_TYPE";
    /**
     * 报警数据
     */
    public static final String MDT_A_DA_TYPE = "MDT_A_DA_TYPE";
    /**
     * 位置数据
     */
    public static final String MDT_PO_DA_TYPE = "MDT_PO_DA_TYPE";
    /**
     * 动力电池数据
     */
    public static final String MDT_BA_DA_TYPE = "MDT_BA_DA_TYPE";
    /**
     * 通用报警数据
     */
    public static final String CM_A_DA = "CM_A_DA";
    /**
     * 通信配置
     */
    public static final String COMMUNICATION_CONFIG = "COMMUNICATION_CONFIG";
    /**
     * 协议类型
     */
    public static final String PROTOCOL_TYPE = "PROTOCOL_TYPE";
    /**
     * 车辆状态
     */
    public static final String MDT_O_VEH_STA = "MDT_O_VEH_STA";
    /**
     * 充电状态
     */
    public static final String MDT_O_CHARGE_STA = "MDT_O_CHARGE_STA";
    /**
     * 运行模式
     */
    public static final String MDT_O_POW_MODE = "MDT_O_POW_MODE";
    /**
     * 车速
     */
    public static final String MDT_O_SPEED = "MDT_O_SPEED";
    /**
     * 累计里程
     */
    public static final String MDT_O_MILEAGE = "MDT_O_MILEAGE";
    /**
     * 总电压
     */
    public static final String MDT_O_TOTAL_VOL = "MDT_O_TOTAL_VOL";
    /**
     * 总电流
     */
    public static final String MDT_O_TOTAL_CUR = "MDT_O_TOTAL_CUR";
    /**
     * SOC
     */
    public static final String MDT_O_SOC = "MDT_O_SOC";
    /**
     * DC-DC状态
     */
    public static final String MDT_O_DC_STA = "MDT_O_DC_STA";
    /**
     * 档位
     */
    public static final String MDT_O_GEAR = "MDT_O_GEAR";
    /**
     * 绝缘电阻
     */
    public static final String MDT_O_RES = "MDT_O_RES";
    /**
     * 驱动状态
     */
    public static final String MDT_O_DRIVER_STA = "MDT_O_DRIVER_STA";
    /**
     * 制动状态
     */
    public static final String MDT_O_BRAKING_STA = "MDT_O_BRAKING_STA";
    /**
     * 加速踏板行程值
     */
    public static final String MDT_O_ASC_LEN = "MDT_O_ASC_LEN";
    /**
     * 制动踏板行程值
     */
    public static final String MDT_O_DEC_LEN = "MDT_O_DEC_LEN";
    /**
     * 驱动电机个数
     */
    public static final String MDT_DR_NO = "MDT_DR_NO";
    /**
     * 驱动电机序号
     */
    public static final String MDT_DR_SN = "MDT_DR_SN";
    /**
     * 驱动电机状态
     */
    public static final String MDT_DR_STA = "MDT_DR_STA";
    /**
     * 驱动电机控制器温度
     */
    public static final String MDT_DR_CTRL_TEM = "MDT_DR_CTRL_TEM";
    /**
     * 驱动电机转速
     */
    public static final String MDT_DR_ROTATE_SPEED = "MDT_DR_ROTATE_SPEED";
    /**
     * 驱动电机转炬
     */
    public static final String MDT_DR_ROTATE_ROTQUE = "MDT_DR_ROTATE_ROTQUE";
    /**
     * 驱动电机温度
     */
    public static final String MDT_DR_TEM = "MDT_DR_TEM";
    /**
     * 电机控制器输入电压
     */
    public static final String MDT_DR_CTRL_IN_VOL = "MDT_DR_CTRL_IN_VOL";
    /**
     * 电机控制器直流母线电流
     */
    public static final String MDT_DR_CTRL_IN_CUR = "MDT_DR_CTRL_IN_CUR";
    /**
     * 发动机状态
     */
    public static final String MDT_EN_STA = "MDT_EN_STA";
    /**
     * 曲轴转速
     */
    public static final String MDT_EN_CRANKSHAFT = "MDT_EN_CRANKSHAFT";
    /**
     * 燃料消耗率
     */
    public static final String MDT_EN_FUEL_COST_RATE = "MDT_EN_FUEL_COST_RATE";
    /**
     * 最高电压电池子系统号
     */
    public static final String MDT_P_MAX_VOL_SYS_SN = "MDT_P_MAX_VOL_SYS_SN";
    /**
     * 最高电压电池单体代号
     */
    public static final String MDT_P_MAX_VOL_CEL_SN = "MDT_P_MAX_VOL_CEL_SN";
    /**
     * 电池单体电压最高值
     */
    public static final String MDT_P_MAX_VOL_CEL_VLU = "MDT_P_MAX_VOL_CEL_VLU";
    /**
     * 最低电压电池子系统号
     */
    public static final String MDT_P_MIN_VOL_SYS_SN = "MDT_P_MIN_VOL_SYS_SN";
    /**
     * 最低电压电池单体代号
     */
    public static final String MDT_P_MIN_VOL_CEL_SN = "MDT_P_MIN_VOL_CEL_SN";
    /**
     * 电池单体电压最低值
     */
    public static final String MDT_P_MIN_VOL_CEL_VLU = "MDT_P_MIN_VOL_CEL_VLU";
    /**
     * 最高温度子系统号
     */
    public static final String MDT_P_MAX_TEM_SYS_SN = "MDT_P_MAX_TEM_SYS_SN";
    /**
     * 最高温度探针序号
     */
    public static final String MDT_P_MAX_TEM_NDL_SN = "MDT_P_MAX_TEM_NDL_SN";
    /**
     * 最高温度值
     */
    public static final String MDT_P_MAX_TEM = "MDT_P_MAX_TEM";
    /**
     * 最低温度子系统号
     */
    public static final String MDT_P_MIN_TEM_SYS_SN = "MDT_P_MIN_TEM_SYS_SN";
    /**
     * 最低温度探针序号
     */
    public static final String MDT_P_MIN_TEM_NDL_SN = "MDT_P_MIN_TEM_NDL_SN";
    /**
     * 最低温度值
     */
    public static final String MDT_P_MIN_TEM = "MDT_P_MIN_TEM";
    /**
     * 最高报警等级
     */
    public static final String MDT_A_MAX_ALARM_LEV = "MDT_A_MAX_ALARM_LEV";
    /**
     * 通用报警标志（共18项）
     */
    public static final String MDT_A_CM_ALARM_MARK = "MDT_A_CM_ALARM_MARK";
    /**
     * 动力电池故障总数
     */
    public static final String MDT_A_BAD_CEL_NO = "MDT_A_BAD_CEL_NO";
    /**
     * 动力电池故障代码列表
     */
    public static final String MDT_A_BAD_CEL_SN_LI = "MDT_A_BAD_CEL_SN_LI";
    /**
     * 驱动电机故障总数
     */
    public static final String MDT_A_BAD_DRIVER_NO = "MDT_A_BAD_DRIVER_NO";
    /**
     * 驱动电机故障代码列表
     */
    public static final String MDT_A_BAD_DRIVER_SN_LI = "MDT_A_BAD_DRIVER_SN_LI";
    /**
     * 发动机故障总数
     */
    public static final String MDT_A_BAD_EN_NO = "MDT_A_BAD_EN_NO";
    /**
     * 发动机故障列表
     */
    public static final String MDT_A_BAD_EN_SN_LI = "MDT_A_BAD_EN_SN_LI";
    /**
     * 其他故障总数
     */
    public static final String MDT_A_BAD_OTHERS_NO = "MDT_A_BAD_OTHERS_NO";
    /**
     * 其他故障代码列表
     */
    public static final String MDT_A_BAD_OTHERS_SN_LI = "MDT_A_BAD_OTHERS_SN_LI";
    /**
     * 定位状态
     */
    public static final String MDT_PO_GPS_STA = "MDT_PO_GPS_STA";
    /**
     * 经度
     */
    public static final String MDT_PO_LON = "MDT_PO_LON";
    /**
     * 纬度
     */
    public static final String MDT_PO_LAT = "MDT_PO_LAT";
    /**
     * 动力电池个数
     */
    public static final String MDT_BA_NO = "MDT_BA_NO";
    /**
     * 动力电池系统号
     */
    public static final String MDT_BA_SYS_SN = "MDT_BA_SYS_SN";
    /**
     * 动力电池电压
     */
    public static final String MDT_BA_VOL = "MDT_BA_VOL";
    /**
     * 动力电池电流
     */
    public static final String MDT_BA_CUR = "MDT_BA_CUR";
    /**
     * 单体电池总数
     */
    public static final String MDT_BA_CEL_NO = "MDT_BA_CEL_NO";
    /**
     * 本帧起始电池序号
     */
    public static final String MDT_BA_FRAME_BEGIN_SN = "MDT_BA_FRAME_BEGIN_SN";
    /**
     * 本帧起始单体电池总数
     */
    public static final String MDT_BA_FRAME_BEGIN_NO = "MDT_BA_FRAME_BEGIN_NO";
    /**
     * 单体电池电压
     */
    public static final String MDT_BA_CEL_VOL = "MDT_BA_CEL_VOL";
    /**
     * 动力电池温度探针个数
     */
    public static final String MDT_BA_NDL_NO = "MDT_BA_NDL_NO";
    /**
     * 各温度探针探测温度值
     */
    public static final String MDT_BA_NDL_TEM = "MDT_BA_NDL_TEM";
    /**
     * 温度差异报警
     */
    public static final String CM_A_DA_0 = "CM_A_DA_0";
    /**
     * 动力电池欠压报警
     */
    public static final String CM_A_DA_3 = "CM_A_DA_3";
    /**
     * 电池高温报警
     */
    public static final String CM_A_DA_1 = "CM_A_DA_1";
    /**
     * 动力电池过压报警
     */
    public static final String CM_A_DA_2 = "CM_A_DA_2";
    /**
     * SOC低报警
     */
    public static final String CM_A_DA_4 = "CM_A_DA_4";
    /**
     * 单体电池过压报警
     */
    public static final String CM_A_DA_5 = "CM_A_DA_5";
    /**
     * 单体电池欠压报警
     */
    public static final String CM_A_DA_6 = "CM_A_DA_6";
    /**
     * SOC过高报警
     */
    public static final String CM_A_DA_7 = "CM_A_DA_7";
    /**
     * SOC跳变报警
     */
    public static final String CM_A_DA_8 = "CM_A_DA_8";
    /**
     * 动力电池系统不匹配报警
     */
    public static final String CM_A_DA_9 = "CM_A_DA_9";
    /**
     * 电池单体一致性差报警
     */
    public static final String CM_A_DA_10 = "CM_A_DA_10";
    /**
     * 绝缘报警
     */
    public static final String CM_A_DA_11 = "CM_A_DA_11";
    /**
     * DC-DC温度报警
     */
    public static final String CM_A_DA_12 = "CM_A_DA_12";
    /**
     * 制动系统报警
     */
    public static final String CM_A_DA_13 = "CM_A_DA_13";
    /**
     * DC-DC状态报警
     */
    public static final String CM_A_DA_14 = "CM_A_DA_14";
    /**
     * 驱动电机控制器温度报警
     */
    public static final String CM_A_DA_15 = "CM_A_DA_15";
    /**
     * 高压互锁状态报警
     */
    public static final String CM_A_DA_16 = "CM_A_DA_16";
    /**
     * 驱动电机温度报警
     */
    public static final String CM_A_DA_17 = "CM_A_DA_17";
    /**
     * 驱动电机储能装置类型过充报警
     */
    public static final String CM_A_DA_18 = "CM_A_DA_18";
    /**
     * VIN
     */
    public static final String VIN = "VIN";
    /**
     * TIME
     */
    public static final String TIME = "TIME";

    /**
     * ====================GB6================================
     */

    /**
     * 国六诊断数据 MDT_OBD_DA_GB6_TYPE
     */
    public static final String MDT_OBD_DA_GB6_TYPE = "MDT_OBD_DA_GB6_TYPE";
    /**
     * 国六位置数据 MDT_OBD_DA_ALARM_GB6_TYPE
     */
    public static final String MDT_OBD_DA_ALARM_GB6_TYPE = "MDT_OBD_DA_ALARM_GB6_TYPE";
    /**
     * 国六基础数据 MDT_OBD_DA_BASE_GB6_TYPE
     */
    public static final String MDT_OBD_DA_BASE_GB6_TYPE = "MDT_OBD_DA_BASE_GB6_TYPE";
    /**
     * MDT_EN_GB6_SPEED 车速
     */
    public static final String MDT_EN_GB6_SPEED = "MDT_EN_GB6_SPEED";
    /**
     * MDT_EN_GB6_AIR_PRESS 大气压力
     */
    public static final String MDT_EN_GB6_AIR_PRESS = "MDT_EN_GB6_AIR_PRESS";
    /**
     * MDT_EN_GB6_TORQUE_OUT 发动机净输出扭矩
     */
    public static final String MDT_EN_GB6_TORQUE_OUT = "MDT_EN_GB6_TORQUE_OUT";
    /**
     * MDT_EN_GB6_TORQUE_RUB 摩擦扭矩
     */
    public static final String MDT_EN_GB6_TORQUE_RUB = "MDT_EN_GB6_TORQUE_RUB";
    /**
     * MDT_EN_GB6_ROTATE_SPEED 发动机转速
     */
    public static final String MDT_EN_GB6_ROTATE_SPEED = "MDT_EN_GB6_ROTATE_SPEED";
    /**
     * MDT_EN_GB6_FUEL_FLOW 发动机燃料流量
     */
    public static final String MDT_EN_GB6_FUEL_FLOW = "MDT_EN_GB6_FUEL_FLOW";
    /**
     * MDT_EN_GB6_SCR_NOX_UP SCR上游NOx传感器输出
     */
    public static final String MDT_EN_GB6_SCR_NOX_UP = "MDT_EN_GB6_SCR_NOX_UP";
    /**
     * MDT_EN_GB6_SCR_NOX_DOWN SCR下游NOx传感器输出
     */
    public static final String MDT_EN_GB6_SCR_NOX_DOWN = "MDT_EN_GB6_SCR_NOX_DOWN";
    /**
     * MDT_EN_GB6_REAGENT_ALLOWANCE 反应剂余量
     */
    public static final String MDT_EN_GB6_REAGENT_ALLOWANCE = "MDT_EN_GB6_REAGENT_ALLOWANCE";
    /**
     * MDT_EN_GB6_INTAKE_VOLUME 进气量
     */
    public static final String MDT_EN_GB6_INTAKE_VOLUME = "MDT_EN_GB6_INTAKE_VOLUME";
    /**
     * MDT_EN_GB6_SCR_TEM_IN SCR入口温度
     */
    public static final String MDT_EN_GB6_SCR_TEM_IN = "MDT_EN_GB6_SCR_TEM_IN";
    /**
     * MDT_EN_GB6_SCR_TEM_OUT SCR出口温度
     */
    public static final String MDT_EN_GB6_SCR_TEM_OUT = "MDT_EN_GB6_SCR_TEM_OUT";
    /**
     * MDT_EN_GB6_DPF_DIFF DPF压差
     */
    public static final String MDT_EN_GB6_DPF_DIFF = "MDT_EN_GB6_DPF_DIFF";
    /**
     * MDT_EN_GB6_COOLANT_TEM 发动机冷却液温度
     */
    public static final String MDT_EN_GB6_COOLANT_TEM = "MDT_EN_GB6_COOLANT_TEM";
    /**
     * MDT_EN_GB6_TANK_LEVEL 邮箱液位
     */
    public static final String MDT_EN_GB6_TANK_LEVEL = "MDT_EN_GB6_TANK_LEVEL";
    /**
     * MDT_EN_GB6_MILEAGE 累计里程
     */
    public static final String MDT_EN_GB6_MILEAGE = "MDT_EN_GB6_MILEAGE";
    /**
     * MDT_OBD_PROTOCOL OBD诊断协议
     */
    public static final String MDT_OBD_PROTOCOL = "MDT_OBD_PROTOCOL";
    /**
     * MDT_OBD_MIL MIL状态
     */
    public static final String MDT_OBD_MIL = "MDT_OBD_MIL";
    /**
     * MDT_OBD_FAILEDNUM 故障码数量
     */
    public static final String MDT_OBD_FAILEDNUM = "MDT_OBD_FAILEDNUM";
    /**
     * MDT_OBD_FAILEDNUM 故障码数量
     */
    public static final String MDT_OBD_FAILURES = "MDT_OBD_FAILURES";

    /**
     * =====================GB6===============================
     */

    /**
     * 单位用车（车辆用途）
     */
    public static final String VEHICLE_PURPOSE_COMPANY = "COMPANY";

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String ONE_1 = "-1";
    public static final String TWO = "2";

    /**
     * 异常
     */
    public static final String EXCEPTION = "254";
    /**
     * 无效
     */
    public static final String INVALID = "255";
    public static final String INVALID_1 = "65535";

    /**
     * @param key    工况数据key
     * @param value  key值
     * @param expand 扩展 key 对应值
     * @return
     */
    public static String hbaseTransform(String key, String value, String expand) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return value;
        }
        String transValue = value;

        switch (key) {
            case TIME:
                transValue = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Long.valueOf(value));
                break;
            case MDT_EN_GB6_SPEED:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_AIR_PRESS:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
            case MDT_EN_GB6_TORQUE_OUT:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_TORQUE_RUB:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_ROTATE_SPEED:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_FUEL_FLOW:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_SCR_NOX_UP:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_SCR_NOX_DOWN:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_REAGENT_ALLOWANCE:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_INTAKE_VOLUME:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_SCR_TEM_IN:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_SCR_TEM_OUT:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_DPF_DIFF:
                if (INVALID_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_COOLANT_TEM:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_TANK_LEVEL:
                if (INVALID.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_EN_GB6_MILEAGE:
                if (ONE_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_PO_GPS_STA:
                if (ZERO.equals(value)) {
                    transValue = "有效定位";
                } else {
                    transValue = "无效定位";
                }
                break;
            case MDT_PO_LON:
                if (ONE_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_PO_LAT:
                if (ONE_1.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_OBD_PROTOCOL:
                if (ONE.equals(value)) {
                    transValue = "IOS27145";
                } else if (ZERO.equals(value)) {
                    transValue = "IOS15765";
                } else if (TWO.equals(value)) {
                    transValue = "SAEJ1939";
                } else {
                    transValue = "无效";
                }
                break;
            case MDT_OBD_MIL:
                if (ZERO.equals(value)) {
                    transValue = "未点亮";
                } else if (ONE.equals(value)) {
                    transValue = "点亮";
                } else {
                    transValue = "无效";
                }
                break;
            case MDT_OBD_FAILEDNUM:
                if (EXCEPTION.equals(value)) {
                    transValue = "无效";
                }
                break;
            case MDT_OBD_FAILURES:
                transValue = intToHex(value);
                break;
            default:
                transValue = value;
                break;
        }

        //OBD诊断数据
        if (key.startsWith("MDT_OBD_SUPPORT_")) {
            if (ZERO.equals(value)) {
                transValue = "不支持";
            } else if (ONE.equals(value)) {
                if (ZERO.equals(expand)) {
                    transValue = "诊断就绪";
                } else {
                    transValue = "诊断未就绪";
                }
            }
        }

        return transValue;
    }

    public static String transObdSupport(List<Character> support, List<Character> ready, List<String> obdSupportNames) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obdSupportNames.size(); i++) {
            sb.append(obdSupportNames.get(i)).append(":");
            if (ZERO.equals(support.get(i))) {
                sb.append("不支持");
            } else if (ONE.equals(support.get(i))) {
                if (ZERO.equals(ready.get(i))) {
                    sb.append("诊断就绪");
                } else {
                    sb.append("诊断未就绪");
                }
            }
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * 十进制转16进制
     *
     * @param value
     */
    public static String intToHex(String value) {
        List<String> strings = Arrays.asList(value.replace("[", "").replace("]", "").replaceAll(" ", "").split(","));
        ArrayList<String> objects = Lists.newArrayList();
        strings.forEach(str -> {
            if ("null".equals(str)) {
                return;
            }
            Long target = new Long(str);
            objects.add(Long.toHexString(Math.abs(target)).toUpperCase());
        });

        return StringUtils.join(objects, ",");
    }
}
