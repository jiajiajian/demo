<%
var date = date(beginTime);
var fenceTypeName ;
    if(fenceType == 0){
        fenceTypeName = "出";
    } else {
        fenceTypeName = "进";
     }
%>
尊敬的用户，您关注的车辆发生了${fenceTypeName}围栏报警，详细信息如下：
机编号：${vin}
动作时间：${date,"yyyy-MM-dd HH:mm:ss"}
动作类型：${fenceTypeName}