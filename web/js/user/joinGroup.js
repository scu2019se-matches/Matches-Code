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
    $('#example23 tbody').on('click', '.look-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        var user_id=$("#userId").val();
        modifyAct(id,user_id);
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
        event.preventDefault();
    });
    $("#example23 tbody").on("click", ".save-button", function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var tds = $(this).parents("tr").children();
        var flag=1;
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if(i==1){
                var txt = jqob.children("input").val();
                if(txt.length<1){
                    Dialog.showWarning("不能为空哦","提示");
                    flag=0;
                    return;
                }else{
                    jqob.html(txt);
                    dataTable.cell(jqob).data(txt);
                }
            }
        });
        if(!flag){
            return;
        }
        var data = row.data();
        var id = data[0];
        var title = data[1];
        modifyRecord(id,title);

        $(this).html("修改");
        $(this).toggleClass("edit-button");
        $(this).toggleClass("save-button");
        event.preventDefault();
    });

}

function enterGroup(id,user_id) {
    window.location.href="../groupdetails/member/list.jsp?group_id="+id;
}
function modifyRecord(group_id,title) {
    var url =initurl+"?action=modify_record&group_id="+group_id+"&title="+title;
    // alert(url);
    $.post(url, function (json) {
        Dialog.showSuccess("修改成功","操作成功");
        // getSelectedRecord(lasturl);
    });
}
function deleteRecord(group_id) {
    var url=initurl+"?action=delete_record&group_id="+group_id;
    // alert(url);
    $.post(url, function (jsonObject) {
        Dialog.showSuccess("已删除", "操作成功");
    });
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_mygroup";
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
function statisticRecord(){
    window.location.href="statistic.jsp";
};
function printRecord(){
    window.location.href="print.jsp";
};
function expordExcel(){
    $(".dt-buttons .buttons-excel").click();
};

Record();
