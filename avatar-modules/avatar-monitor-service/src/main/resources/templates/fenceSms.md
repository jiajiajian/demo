<%
var date = date(beginTime);
var fenceTypeName ;
    if(fenceType == 0){
        fenceTypeName = "出";
    } else {
        fenceTypeName = "进";
     }
%>
["${vin}","${date,"yyyy/MM/dd"}","${date,"HH:mm:ss"}","${fenceTypeName}"]