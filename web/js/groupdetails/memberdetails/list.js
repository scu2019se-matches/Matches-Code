var Data=[];
var module="/GroupMember";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var GroupId=$("#group_id").val();
var UserId=$("#user_id").val();
var MemberId=$("#member_id").val();
function modifyRecord(url) {
    $.post(url, function (json) {

    });
}
function deleteRecord(id) {
    var url=ContextPath+module+"?action=delete_record";
    if (id !="") {
        url += "&id=" + id;
    }
    // console.log("删除操作url"+url);
    $.post(url, function (jsonObject) {

    });
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_record&group_id="+GroupId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        for (var i = 0; i < json.length; i++) {
            var user_id = json[i]["user_id"];
            var user = json[i]["user"];
            var creator_id = json[i]["creator_id"];
            var create_time = json[i]["create_time"];
            var grades = json[i]["grades"];
            var commodity = json[i]["commodity"];
            var auth = json[i]["auth"];
            dataTable.row.add([user_id, user,create_time,grades,commodity,auth]).draw().node();
        }
    });
}
function getSelectedRecord(url){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    $.post(url, function (json) {
        Data = json;
        for (var i = 0; i < json.length; i++) {
            var user_id = json[i]["user_id"];
            var user = json[i]["user"];
            var creator_id = json[i]["creator_id"];
            var create_time = json[i]["create_time"];
            var grades = json[i]["grades"];
            var commodity = json[i]["commodity"];
            var auth = json[i]["auth"];
            dataTable.row.add([user_id, user,create_time,grades,commodity,auth]).draw().node();
        }
    });
}


function statisticRecord(){
    window.location.href="statistic.jsp";
};
function printRecord(){
    window.location.href="print.jsp";
};
function expordExcel(){
    $(".dt-buttons .buttons-excel").click();
};

function sortRecord(){
    var key1 = $("#key1").val();
    var key2 = $("#key2").val();
    var key3 = $("#key3").val();
    var rule1 = $("#rule1").val();
    var rule2 = $("#rule2").val();
    var rule3 = $("#rule3").val();
    var url =initurl+"?action=get_record&group_id="+GroupId;
    var user = $("#user").val();
    if (user != "") {
        url += "&user=" + title;
    }
    var tmp="&orderby=";
    var flag=0;
    if (key1 != "") {
        if(flag){
            tmp += " ," + key1;
            tmp += " " + rule1;
        }else{
            tmp += " " + key1;
            tmp += " " + rule1;
            flag=1;
        }
    }
    if (key2 != "") {
        if(flag){
            tmp += " ," + key2;
            tmp += " " + rule2;
        }else{
            tmp += " " + key2;
            tmp += " " + rule2;
            flag=1;
        }
    }
    if (key3 != "") {
        if(flag){
            tmp += " ," + key3;
            tmp += " " + rule3;
        }else{
            tmp += " " + key3;
            tmp += " " + rule3;
            flag=1;
        }
    }
    url=url+tmp;
    getSelectedRecord(url);

};
function searchRecord(){
    var user = $("#user").val();
    var url =initurl+"?action=get_record&group_id="+GroupId;
    if (user != "") {
        url += "&user=" + user;
    }
    getSelectedRecord(url);

};
Record();

function commodityRecord(){

}
function taskRecord(){
    var group_id=$("#group_id").val();
    window.location.href="../task/list.jsp?group_id="+group_id;
}
