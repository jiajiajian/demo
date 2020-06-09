<%
var date = date(beginTime);
%>
尊敬的用户，您关注的车辆产生了报警，详细信息如下：
机编号：${vin}
时间：${date,"yyyy-MM-dd HH:mm:ss"}
报警项：${alarmItemName}
报警地点：${address}
