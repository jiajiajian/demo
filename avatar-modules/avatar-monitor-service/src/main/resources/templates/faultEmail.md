<%
var date = date(beginTime);
%>
尊敬的用户，您关注的车辆产生了故障，详细信息如下：
机编号：${vin}
时间：${date,"yyyy-MM-dd HH:mm:ss"}
故障码：${spn}.${fmi}
故障部件：${tla}
故障地点：${address}