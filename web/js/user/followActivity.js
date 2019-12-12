var Data=[];
var module="/ActivityManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var UserId=$("#user_id").val();
var lasturl="";


function Record(){
    $.fn.dataTable.ext.errMode = "none";
    var dataTable=$('#example23').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend: 'excel',
                title: '活动列表',
                className: 'buttons-excel hidden',
                exportOptions: {
                    columns: [ 0,1,2,3,4 ]
                }
            },
            {
                extend: 'print',
                title: '活动列表',
                className: 'buttons-print hidden',
                exportOptions: {
                    columns: [ 0,1,2,3,4]
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
            "sLengthMenu":   "_MENU_ 活动/页",
            "sZeroRecords":  "<span'>没有找到对应的活动！</span>",
            "sInfo":         "显示第 _START_ 至 _END_ 个活动，共 _TOTAL_ 个",
            "sInfoEmpty":    "显示第 0 至 0 个活动，共 0 个",
            "sInfoFiltered": "(由 _MAX_ 个活动过滤)",
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
                "targets":4,
                "mRender":
                    function(data, type, full) {
                    var sReturn="<span class=\"badge badge-primary\">"
                        + data+'</span>';
                        return sReturn;
                    },
            },
            {
                "targets":5,
                "mRender":
                    function(data, type, full) {
                        if(full[5]==0) {
                            sReturn=
                                '<span class="badge badge-primary">'+
                                "将开始"+'</span>';
                        }else if(full[5]==1){
                            sReturn='<span class="badge badge-success">'+
                                "进行中"+'</span>';
                        }else{
                            sReturn=
                                '<span class="badge badge-danger">'+
                                "已结束"+'</span>';
                        }
                        if(full[6]==0){
                            sReturn+=
                                '<span class="badge badge-danger m-l-5">'+
                                "可关注"+'</span>';
                        }else{
                            sReturn+='<span class="badge badge-success m-l-5">'+
                                "已关注"+'</span>';
                        }
                        return sReturn;
                    },
            },
            {
                "targets":6,
                "mRender":
                    function(data, type, full) {
                        sReturn="";
                        if(full[7]==1) {
                            sReturn = sReturn +
                                "<button type=\"button\" class=\"modify-button btn btn-success btn-sm btn-rounded m-b-10 m-l-5\">修改</button>" +
                                "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>"
                        }
                        if(full[6]==0){
                            sReturn=sReturn+"<button type=\"button\" class=\"follow-button btn btn-arrow-link btn-sm btn-rounded m-b-10 m-l-5\">关注</button>";
                        }else{
                            sReturn=sReturn+"<button type=\"button\" class=\"unfollow-button btn btn-arrow-link btn-sm btn-rounded m-b-10 m-l-5\">取消关注</button>";
                        }

                        sReturn=sReturn+"<button type=\"button\" class=\"enter-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">详情</button>";
                        return sReturn;
                    },
            },
            {
                "targets":[1,2,4,6],
                "orderable": false,
            },
        ],
        "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有活动"]],
    });
    getAllRecord();

    $('#example23 tbody').on('click', '.enter-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        enterAct(id);
    });
    $('#example23 tbody').on('click', '.follow-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        followAct(id);
    });
    $('#example23 tbody').on('click', '.unfollow-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        unfollowAct(id);
    });
    $('#example23 tbody').on('click', '.modify-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        modifyRecord(id);
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        // alert(1);
        Dialog.showComfirm("确定要删除吗？", "警告", function(){
            // alert(2);
            var row = dataTable.row($(_this).parents("tr"));
            var data = row.data();
            var group_id = data[0];
            // var group_id = $(_this).parent().prev().text();
            var table = $('#example23').DataTable();
            table.row($(_this).parents('tr')).remove().draw();
            event.preventDefault();
            deleteRecord(group_id);
        });

    });
}
function enterAct(id) {
    window.location.href="../activity/detail.jsp?activity_id="+id;
}
function followAct(id) {
    url =initurl+"?action=focus_record&activity_id="+id;
    $.post(url, function (json) {
        Dialog.showSuccess("关注成功","操作成功");
        getAllRecord();
    });
}
function unfollowAct(id) {
    url =initurl+"?action=unfocus_record&activity_id="+id;
    $.post(url, function (json) {
        Dialog.showSuccess("取消关注成功","操作成功");
        getAllRecord();
    });
}
function modifyRecord(activity_id) {
    window.location.href="../activity/modify.jsp?activity_id="+activity_id;
}
function deleteRecord(activity_id) {
    var url=initurl+"?action=delete_record&activity_id="+activity_id;
    // alert(url);
    $.post(url, function (jsonObject) {
        Dialog.showSuccess("已删除", "操作成功");
    });
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_activity&user_id="+UserId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var headline = json[i]["headline"];
            var publisher_id = json[i]["publisher_id"];
            var publisher = json[i]["publisher"];
            var create_time = json[i]["create_time"];
            var begin_time = json[i]["begin_time"];
            var end_time = json[i]["end_time"];
            var site = json[i]["site"];
            var tag = json[i]["tag"];
            var auth = json[i]["auth"];
            var act_status = json[i]["act_status"];
            var focus_status = json[i]["focus_status"];
            dataTable.row.add([id, headline, publisher,Time.StdToMinute(create_time),tag,act_status,focus_status,auth]).draw().node();
        }
    });
}
function addRecord(){
    window.location.href="../activity/add.jsp";
}
function printRecord(){
    $(".dt-buttons .buttons-print").click();
};
function expordExcel(){
    $(".dt-buttons .buttons-excel").click();
};


Record();
