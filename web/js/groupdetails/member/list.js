var Data=[];
var module="/GroupMember";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
function Record(){
    $.fn.dataTable.ext.errMode = "none";
    var dataTable=$('#example23').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend: 'excel',
                title: 'groupmemberinfo',
                className: 'buttons-excel hidden',
                exportOptions: {
                    columns: [ 0,1,2,3,4 ]
                }
            },
        ],
        fixedHeader: true,
        fixedColumns: {
            leftColumns: 1
        },
        // ordering: false,
        "oLanguage": {
            "aria": {
                "sortAscending": ": activate to sort column ascending",
                "sortDescending": ": activate to sort column descending"
            },
            "sProcessing":   "处理中...",
            "sLengthMenu":   "_MENU_ 组/页",
            "sZeroRecords":  "<span'>没有找到对应的成员！</span>",
            "sInfo":         "显示第 _START_ 至 _END_ 个成员，共 _TOTAL_ 个",
            "sInfoEmpty":    "显示第 0 至 0 个成员，共 0 个",
            "sInfoFiltered": "(由 _MAX_ 个成员过滤)",
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
                "targets":5,
                "mRender":
                    function(data, type, full) {
                        sReturn=""
                        if(full[5]==1) {
                            sReturn = sReturn +
                                "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>"
                            sReturn=sReturn+"<button type=\"button\" class=\"enter-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">详情</button>";
                        }
                        return sReturn;
                    },
            },
            {
                "targets":[1,5],
                "orderable": false,
            },
        ],
        "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有成员"]],
    });
    getAllRecord();

    $('#example23 tbody').on('click', '.enter-button', function (event) {
        var group_id = $("#group_id").val();
        var user_id = $("#id").val();
        enterDetails(group_id,user_id);
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        Dialog.showComfirm("确定要删除吗？", "警告", function(){
            var id = $(_this).parent().prev().text();
            console.log("id"+id);
            deleteRecord(id);
            var table = $('#example23').DataTable();
            table.row($(_this).parents('tr')).remove().draw();
            event.preventDefault();
            Dialog.showSuccess("已删除", "操作成功");
        });

    });

}
function enterDeatails(group_id,user_id) {

}
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
    var url=initurl+"?action=get_record";
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
    var url =initurl+"?action=get_record";
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
    var url =initurl+"?action=get_record";
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
