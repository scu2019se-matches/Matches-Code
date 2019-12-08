var Data=[];
var module="/GroupTask";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var GroupId=$("#group_id").val();
var UserId=$("#user_id").val();
var initurl=ContextPath+module;
var CreatorId="";
var Auth=$("#auth").val();
var lasturl="";
var LookFlag=0;
function Record(){
    $.post(String.format("{0}?action={1}&groupId={2}",initurl,"getValid",GroupId),function(res){
        console.log(res);
        if(res.errno == 1){
            if(res.auth<2&&res.creatorId!=UserId){
                history.go(-1);
            }else{
                LookFlag=1;
            }
        }else{
            $('#MyDetails').css('display','inline');
            $('#Addrecord').css('display','inline');
        }
    });

    $.fn.dataTable.ext.errMode = "none";
    var dataTable=$('#example23').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend: 'excel',
                title: 'groupinfo',
                className: 'buttons-excel hidden',
                exportOptions: {
                    columns: [ 0,1,2]
                }
            },
        ],
        fixedHeader: true,
        fixedColumns: {
            leftColumns: 1
        },
        ordering: false,
        "oLanguage": {
            "aria": {
                "sortAscending": ": activate to sort column ascending",
                "sortDescending": ": activate to sort column descending"
            },
            "sProcessing":   "处理中...",
            "sLengthMenu":   "_MENU_ 任务/页",
            "sZeroRecords":  "<span'>没有找到对应的任务！</span>",
            "sInfo":         "显示第 _START_ 至 _END_ 个任务，共 _TOTAL_ 个",
            "sInfoEmpty":    "显示第 0 至 0 个任务，共 0 个",
            "sInfoFiltered": "(由 _MAX_ 个任务过滤)",
            "sInfoPostFix":  "",
            "sSearch":       "搜索:",
            "oPaginate": {
                "sFirst":    "首页",
                "sPrevious": "上页",
                "sNext":     "下页",
                "sLast":     "末页"
            }
        },
        "columnDefs": [
            {
                "targets":3,
                "mRender":
                    function(data, type, full) {
                        sReturn="";
                        if(full[3]==0) {
                            sReturn=
                                '<span class="badge badge-primary">'+
                                "将开始"+'</span>';
                        }else if(full[3]==1){
                            sReturn='<span class="badge badge-success">'+
                                "进行中"+'</span>';
                        }else{
                            sReturn=
                                '<span class="badge badge-danger">'+
                                "已结束"+'</span>';
                        }
                        if(full[4]==2||LookFlag==1){
                            sReturn+=
                                '<span class="badge badge-danger m-l-5">'+
                                "不可操作"+'</span>';
                        }else if(full[4]==1){
                            sReturn+='<span class="badge badge-success m-l-5">'+
                                "今日可完成"+'</span>';
                        }else{
                            sReturn+=
                                '<span class="badge badge-primary  m-l-5">'+
                                "今日已完成"+'</span>';
                        }
                        return sReturn;
                    },
            },
            {
                "targets":4,
                "mRender":
                    function(data, type, full) {
                        sReturn="";
                        if(full[5]==1) {
                            sReturn = sReturn +
                                "<button type=\"button\" class=\"edit-button btn btn-success btn-sm btn-rounded m-b-10 m-l-5\">修改</button>" +
                                "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>";
                        }
                        if(full[4]==1&&LookFlag!=1){
                            sReturn=sReturn+"<button type=\"button\" class=\"finish-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">标记完成</button>";
                        }
                        return sReturn;
                    },
            },
            {
                "targets":[0],
                "orderable": false,
            },
        ],
        "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有任务"]],
    });
    getAllRecord();

    $('#example23 tbody').on('click', '.finish-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        finishTask(data[0],data[1],data[6]);
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        Dialog.showComfirm("确定要删除吗？", "警告", function(){
            var row = dataTable.row($(_this).parents("tr"));
            var data = row.data();
            deleteRecord(data[6]);
            var table = $('#example23').DataTable();
            table.row($(_this).parents('tr')).remove().draw();
            event.preventDefault();
        });

    });
    $("#example23 tbody").on("click", ".edit-button", function (event) {
        var tds = $(this).parents("tr").children();
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if(i==1){
                var txt = jqob.text();
                var put = $("<input type='number'>");
                put.val(txt);
                jqob.html(put);
            } else if (i == 2 ) {
                var txt = jqob.text();
                var put = $("<input type='text' class='dateSelect'>");
                put.val(txt);
                jqob.html(put);
            }

        });
        $(this).html("保存");
        $(this).toggleClass("edit-button");
        $(this).toggleClass("save-button");
        event.preventDefault();
    });
    $("#example23 tbody").on("click", ".dateSelect", function (event) {
        DatePicker.DateTime(".dateSelect");
    });
    $("#example23 tbody").on("click", ".save-button", function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var tds = $(this).parents("tr").children();
        var valid_flag=1;
        $.each(tds, function (i, val) {
            var jqob = $(val);
            var txt = jqob.children("input").val();
            if(i==1){
                if(txt<1||txt>100){
                    Dialog.showWarning("积分1-100","提示");
                    valid_flag=0;
                }else{
                    jqob.html(txt);
                    dataTable.cell(jqob).data(txt);
                }
            }else{
                jqob.html(txt);
                dataTable.cell(jqob).data(txt);
            }
        });
        if(!valid_flag){
            return;
        }
        var data = row.data();
        var task_id = data[6];
        var grades = data[1];
        var end_time = data[2];

        modifyRecord(task_id,grades,end_time);
    });
}
function finishTask(task,grades,task_id){
    var url=ContextPath+module+"?action=finish_task&group_id="+GroupId+
        "&task_id="+task_id+"&grades="+grades+"&task="+task;
    $.post(url, function (jsonObject) {
        getAllRecord();
    });
}
function modifyRecord(task_id,grades,end_time){
    url =initurl+"?action=modify_record&group_id="+GroupId+"&task_id="+task_id
        +"&grades="+grades+"&end_time="+end_time;
    $.post(url, function (json) {
        getSelectedRecord(lasturl);
        Dialog.showSuccess("修改成功","操作成功");
    });
}
function deleteRecord(task_id) {
    var url=ContextPath+module+"?action=delete_record&group_id="+GroupId
    +"&task_id="+task_id;
    $.post(url, function (jsonObject) {
        Dialog.showSuccess("已删除", "操作成功");
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
            var task_id = json[i]["id"];
            var creator_id = json[i]["creator_id"];
            var context = json[i]["context"];
            var grades = json[i]["grades"];
            var create_time = json[i]["create_time"];
            var begin_time = json[i]["begin_time"];
            var end_time = json[i]["end_time"];
            var task_status = json[i]["task_status"];
            var my_status = json[i]["my_status"];
            var auth = json[i]["auth"];
            // var user_id = json[i]["user_id"];
            dataTable.row.add([context, grades,Time.StdToMinute(end_time),task_status,my_status,auth,task_id]).draw().node();

            if(CreatorId==null){
                CreatorId=creator_id;
            }
        }
    });
}
function getSelectedRecord(url){
    if(url.length<1){
        getAllRecord();
        return;
    }
    lasturl=url;
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    $.post(url, function (json) {
        Data = json;
        for (var i = 0; i < json.length; i++) {
            var task_id = json[i]["id"];
            var creator_id = json[i]["creator_id"];
            var context = json[i]["context"];
            var grades = json[i]["grades"];
            var create_time = json[i]["create_time"];
            var begin_time = json[i]["begin_time"];
            var end_time = json[i]["end_time"];
            var task_status = json[i]["task_status"];
            var my_status = json[i]["my_status"];
            var auth = json[i]["auth"];
            // var user_id = json[i]["user_id"];
            dataTable.row.add([context, grades,Time.StdToMinute(end_time),task_status,my_status,auth,task_id]).draw().node();

            if(CreatorId==null){
                CreatorId=creator_id;
            }
        }
    });
}
function addRecord(){
    // console.log($("#newTask #grades").val());
    if($("#newTask #context").val()==null||$("#newTask #context").val()==""){
        Dialog.showWarning("内容不能为空","提示");
        return;
    }else if($("#newTask #grades").val()==null||$("#newTask #grades").val()==""){
        Dialog.showWarning("需要设置积分","提示");
        return;
    }

    var form = document.getElementById('newTask');
    var daterange=$("#newTask #dateRangeSelect").val();
    if(daterange==null||daterange==""){
        Dialog.showWarning("需要设置时间范围","提示");
        return;
    }
    var date=daterange.split(" to ");
    $("#newTask #group_id").val(GroupId);
    $("#newTask #begin_time").val(date[0]);
    $("#newTask #end_time").val(date[1]);
    form.submit();
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
    var context = $("#context").val();
    var grades = $("#grades").val();
    if (context != "") {
        url += "&context=" + context;
    }
    if (grades != "") {
        url += "&grades=" + grades;
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
    var context = $("#context").val();
    var grades = $("#grades").val();
    var url =initurl+"?action=get_record&group_id="+GroupId;
    if (context != "") {
        url += "&context=" + context;
    }
    if (grades != "") {
        url += "&grades=" + grades;
    }
    getSelectedRecord(url);
};
function toMyDetails(){
    var url="../memberdetails/list.jsp?group_id="+GroupId+"&member_id="+UserId;
    window.location.href=url;
}
function toMemberList(){
    var url="../member/list.jsp?group_id="+GroupId;
    window.location.href=url;
}
function ReturnBack(){
    history.go(-1);
}




DatePicker.DateTimeRangeFromToday("#dateRangeSelect");
Record();
