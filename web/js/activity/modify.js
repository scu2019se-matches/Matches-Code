var Data=[];
var module="/ActivityManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var UserId=$("#user_id").val();
var ActivityId=$("#activity_id").val();
var BeginTime="";
var EndTime="";

function getRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_record&activity_id="+ActivityId;
    $.post(url, function (json) {
        Data = json[0];
        // console.log(json);
        BeginTime=Data["begin_time"];
        AAAAAA=Data["begin_time"];
        EndTime=Data["end_time"];
        $("#headline").val(Data["headline"]);
        $("#site").val(Data["site"]);
        $("#time_range").val(Time.StdToMinute(BeginTime)+" to "
            +Time.StdToMinute(EndTime));
        $("#tag").val(Data["tag"]);
        $("#description").val(Data["description"]);
    });
}
function modifyRecord(){
    var form = document.getElementById('newAct');
    // console.log(form);
    // console.log($("#site").val());
    if($("#site").val()==null||$("#site").val()==""){
        Dialog.showWarning("地点不能为空","提示");
        return;
    }else if($("#headline").val()==null||$("#headline").val()==""){
        Dialog.showWarning("需要设置标题","提示");
        return;
    }
    var daterange=$("#time_range").val();
    if(daterange==null||daterange==""){
        Dialog.showWarning("需要设置时间范围","提示");
        return;
    }
    var date=daterange.split(" to ");
    $("#newAct,#begin_time").val(date[0]);
    $("#newAct,#end_time").val(date[1]);

    // console.log(form);
    form.submit();
}

getRecord();

var locale = {
    "applyLabel": "确定",
    "cancelLabel": "取消",
    "fromLabel": "起始时间",
    "toLabel": "结束时间",
    "customRangeLabel": "自定义",
    "weekLabel": "W",
    "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
    "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    "firstDay": 1
};
$(".time_range").daterangepicker({
    "locale": locale,
    "separator":" to ",
    "opens":"left",
    "format":"YYYY-MM-DD HH:mm",
    "ranges" : {
        '一周': [ moment(),moment().add('days', 7)],
        '本月': [moment(),moment().endOf("month")],
    },

    "timePicker":true,
    "timePickerIncrement" : 1,
    "timePicker12Hour":false,
});

function returnBack(){
    window.location.href=ContextPath+"/activity/list.jsp";
};

