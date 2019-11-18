var Data=[];
var module="/GroupManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var lasturl="";


function Record(){
    $.fn.dataTable.ext.errMode = "none";
    var dataTable=$('#example23').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend: 'excel',
                title: 'groupinfo',
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
            "sZeroRecords":  "<span'>没有找到对应的组！</span>",
            "sInfo":         "显示第 _START_ 至 _END_ 个组，共 _TOTAL_ 个",
            "sInfoEmpty":    "显示第 0 至 0 个组，共 0 个",
            "sInfoFiltered": "(由 _MAX_ 个组过滤)",
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
                                "<button type=\"button\" class=\"edit-button btn btn-success btn-sm btn-rounded m-b-10 m-l-5\">修改</button>" +
                                "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>"
                        }
                        sReturn=sReturn+"<button type=\"button\" class=\"enter-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">进入</button>";
                        return sReturn;
                    },
            },
            {
                "targets":[1,2,5],
                "orderable": false,
            },
        ],
        "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有小组"]],
    });
    getAllRecord();

    $('#example23 tbody').on('click', '.enter-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        var user_id=$("#userId").val();
        enterGroup(id,user_id);
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        Dialog.showComfirm("确定要删除吗？", "警告", function(){
            var id = $(_this).parent().prev().text();
            // console.log("id"+id);
            deleteRecord(id);
            var table = $('#example23').DataTable();
            table.row($(_this).parents('tr')).remove().draw();
            event.preventDefault();
            Dialog.showSuccess("已删除", "操作成功");
        });

    });
    $("#example23 tbody").on("click", ".edit-button", function (event) {
        var tds = $(this).parents("tr").children();
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if (i != 1 ) {
                return true;
            }
            var txt = jqob.text();
            var put = $("<input type=\"text\">");
            put.val(txt);
            jqob.html(put);
        });
        $(this).html("保存");
        $(this).toggleClass("edit-button");
        $(this).toggleClass("save-button");
        // event.preventDefault();
    });
    $("#example23 tbody").on("click", ".save-button", function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var tds = $(this).parents("tr").children();
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if(i==1){
                var txt = jqob.children("input").val();
                if(txt.length<1){
                    Dialog.showWarning("不能为空哦","提示");
                    return;
                }
            }
        });
        var data = row.data();
        var id = data[0];
        var title = data[1];
        url =initurl+"?action=modify_record&group_id="+id+"&title="+title;
        modifyRecord(url);
        Dialog.showSuccess("修改成功","操作成功");
        getSelectedRecord(lasturl);
    });

}
function enterGroup(id,user_id) {
    url =ContextPath+"/GroupMember?action=get_record&group_id="+id+"&user_id="+user_id;
    $.post(url, function (json) {
        if(json.length<1){
            swal({
                title: "输入密码",
                text: "你还不是该组成员",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                confirmButtonText: "确定",
                cancelButtonText: "放弃",
                animation: "slide-from-top",
                inputPlaceholder: "输入区"
            },function (inputValue) {
                url=initurl+"?action=get_record&group_id="+id+"&password="+inputValue;
                $.post(url, function (json1) {
                    console.log(json1);
                    if(json1.length<1){
                        swal.showInputError("密码错误");
                    }else{
                        var creator_id = json1[0]["creator_id"];
                        url=ContextPath+"/GroupMember?action=add_record&creator_id="+creator_id+"&group_id="+id;

                        $.post(url, function (json2) {
                            swal({
                                title : "密码正确",
                                text : "你已成功加入该分组！",
                                type : "success",
                            }, function() {
                                window.location.href="../groupdetails/member/list.jsp?group_id="+id;
                            });
                        })
                    }
                });
            }
            );
        }else{
            window.location.href="../groupdetails/member/list.jsp?group_id="+id;
        }
    });
}
function modifyRecord(url) {
    $.post(url, function (json) {
    });
}
function deleteRecord(id) {
    var url=initurl+"?action=delete_record&group_id="+id;
    $.post(url, function (jsonObject) {});
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_record";
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var title = json[i]["title"];
            var creator = json[i]["creator"];
            var create_time = json[i]["create_time"];
            var user_number = json[i]["user_number"];
            var auth = json[i]["auth"];
            // var user_id = json[i]["user_id"];
            dataTable.row.add([id, title, creator,Time.StdToMinute(create_time),user_number,auth]).draw().node();
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
            var id = json[i]["id"];
            var title = json[i]["title"];
            var creator = json[i]["creator"];
            var create_time = json[i]["create_time"];
            var user_number = json[i]["user_number"];
            var auth = json[i]["auth"];
            var password = json[i]["password"];
            dataTable.row.add([id, title, creator,Time.StdToMinute(create_time),user_number,auth,password]).draw().node();
        }
    });
}
function addRecord(){
    var form = document.getElementById('newGroup');
    // console.log("group form"+form);
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
    var rule1 = $("#rule1").val();
    var rule2 = $("#rule2").val();
    var url =initurl+"?action=get_record";
    var title = $("#title").val();
    var creator = $("#creator").val();
    if (title != "") {
        url += "&title=" + title;
    }
    if (creator != "") {
        url += "&creator=" + creator;
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
    url=url+tmp;
    getSelectedRecord(url);
};
function searchRecord(){
    var title = $("#title").val();
    var creator = $("#creator").val();
    var url =initurl+"?action=get_record";
    if (title != "") {
        url += "&title=" + title;
    }
    if (creator != "") {
        url += "&creator=" + creator;
    }
    getSelectedRecord(url);
};

Record();
