var Data=[];
var module="/MemberPanel";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var GroupId=$("#group_id").val();
var UserId=$("#user_id").val();
var MemberId=$("#member_id").val();
function getAllRecord(){
    getUserData();
    getCommodity();
    // statisticRecord();
}
function getUserData(){
    var url=ContextPath+"/GroupMember?action=get_record&group_id="+GroupId+"&user_id="+MemberId;
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        var grades = json[0].grades;
        var commodity = json[0].commodity;
        document.getElementById("grades").innerHTML=grades;
        document.getElementById("commodity").innerHTML=commodity;
    });
}
function statisticRecord(){
};
function getCommodity(){
    var url=initurl+"?action=get_record&group_id="+GroupId+"&member_id="+MemberId;
    // alert(url);
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        var html="";
        for (var i = 0; i < json.length; i++) {
            if(json[i]==null)continue;
            var id = json[i]["id"];
            var group_id = json[i]["group_id"];
            var commodity_id = json[i]["commodity_id"];
            var commodity = json[i]["commodity"];
            var grades = json[i]["grades"];
            var count = json[i]["count"];
            var auth = json[i]["auth"];
            html+=
            "<tr>"
            +"<th scope=\"row\">"+commodity_id+"</th>"
            +"<td>"+commodity+"</td>"
            +"<td>"+grades+"</td>"
            +"<td>"
                +"<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\"ti-minus\"></i></button>"
                +"<span class=\"badge\">"+count+"</span>"
                +"<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\"ti-plus\"></i></button>"
            +"</td>"
            +"<td class=\"color-primary\">"
                +"<button type=\"button\" class=\"btn btn-info btn-xs\"><i class=\"ti-close\"></i></button>"
            +"</td>"
            +"</tr>"
        }
        document.getElementById("commodity_table").innerHTML=html;
    });
}
function buyCommdity(){
    
}
function sellCommdity() {
    
}
function useCommdity(){

}


function commodityRecord(){
    var group_id=$("#group_id").val();
    window.location.href="../commodity/list.jsp?group_id="+group_id;
}
function taskRecord(){
    var group_id=$("#group_id").val();
    window.location.href="../task/list.jsp?group_id="+group_id;
}
function ReturnBack(){
    history.go(-1);
}
getAllRecord();