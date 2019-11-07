var Data=[];
var module="/GroupManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;

function Record(){
    $(document).ready(function() {
        $('#myTable').DataTable();
        $(document).ready(function() {
            var table = $('#example').DataTable({
                "columnDefs": [{
                    "visible": false,
                    "targets": 2
                }],
                "order": [
                    [2, 'asc']
                ],
                "displayLength": 25,
                "drawCallback": function(settings) {
                    var api = this.api();
                    var rows = api.rows({
                        page: 'current'
                    }).nodes();
                    var last = null;
                    api.column(2, {
                        page: 'current'
                    }).data().each(function(group, i) {
                        if (last !== group) {
                            $(rows).eq(i).before('<tr class="group"><td colspan="5">' + group + '</td></tr>');
                            last = group;
                        }
                    });
                }
            });
            // Order by the grouping
            $('#example tbody').on('click', 'tr.group', function() {
                var currentOrder = table.order()[0];
                if (currentOrder[0] === 2 && currentOrder[1] === 'asc') {
                    table.order([2, 'desc']).draw();
                } else {
                    table.order([2, 'asc']).draw();
                }
            });
        });
    });
    $.fn.dataTable.ext.errMode = "none";
    // var dataTable=$('#example23').DataTable({
    //     dom: 'Bfrtip',
    //     buttons:[
    //         {
    //             extend: 'print',
    //             className: 'buttons-print hidden',
    //             messageTop: '移动互动课堂|文件信息列表',
    //             exportOptions: {
    //                 columns: [ 0,2,3,4,5,6,7,8 ]
    //             }
    //         },
    //         {
    //             extend: 'excel',
    //             title: 'fileinfo',
    //             className: 'buttons-excel hidden',
    //             exportOptions: {
    //                 columns: [ 0,2,3,4,5,6,7,8 ]
    //             }
    //         },
    //         {
    //             extend: 'csv',
    //             title: 'fileinfo',
    //             className: 'buttons-csv hidden',
    //             exportOptions: {
    //                 columns: [ 0,2,3,4,5,6,7,8 ]
    //             }
    //         },
    //
    //     ],
    //     fixedHeader: true,
    //     fixedColumns: {
    //         leftColumns: 1
    //     },
    //     ordering: false,
    //     "oLanguage": {
    //         "aria": {
    //             "sortAscending": ": activate to sort column ascending",
    //             "sortDescending": ": activate to sort column descending"
    //         },
    //         "sProcessing":   "处理中...",
    //         "sLengthMenu":   "_MENU_ 文件/页",
    //         "sZeroRecords":  "<span'>没有找到对应的文件！</span>",
    //         "sInfo":         "显示第 _START_ 至 _END_ 个文件，共 _TOTAL_ 个",
    //         "sInfoEmpty":    "显示第 0 至 0 个文件，共 0 个",
    //         "sInfoFiltered": "(由 _MAX_ 个文件过滤)",
    //         "sInfoPostFix":  "",
    //         "sSearch":       "搜索:",
    //         "oPaginate": {
    //             "sFirst":    "首页",
    //             "sPrevious": "上页",
    //             "sNext":     "下页",
    //             "sLast":     "末页"
    //         }
    //     },
    //     "columnDefs": [
    //         {
    //             "targets":1,
    //             "mRender":
    //                 function(data, type, full) {
    //                     // console.log("这是文件url:"+data);
    //                     // console.log("这是文件full:"+full);
    //                     sReturn=""
    //                     if(full[9]==1) {
    //                         sReturn = sReturn +
    //                             "<button type=\"button\" class=\"edit-button btn btn-success btn-sm btn-rounded m-b-10 m-l-5\">修改</button>" +
    //                             "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>"
    //                     }
    //                     // sReturn=sReturn+"<button type=\"button\" onclick=\"downloadRecord('"+data+"')\" class=\"download-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">下载</button>";
    //                     sReturn=sReturn+"<button type=\"button\" class=\"download-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">下载</button>";
    //                     return sReturn;
    //                 },
    //         },
    //         {
    //             "targets":9,
    //             "bVisible":false,
    //         },
    //     ],
    //     "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有文件"]],
    // });
    getAllRecord();

    $('#example23 tbody').on('click', '.download-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        console.log(data);
        var guid = data[0];
        var fileurl = data[1];
        var downloadNum = data[7];
        var tds = $(this).parents("tr").children();
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if (i==7) {
                var txt = downloadNum+1;
                jqob.html(txt);
                dataTable.cell(jqob).data(txt);
            }
        });
        url =ContextPath+module+"?action=modify_record";
        if (guid != "") {
            url += "&guid=" + guid;
        }
        url += "&downloadNum=" + (downloadNum+1);
        window.open(fileurl);
        event.preventDefault();
        $.post(url, function (json) {

        });
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        Dialog.showComfirm("确定要删除这条记录吗？", "警告", function(){
            var id = $(_this).parent().prev().text();
            console.log("id"+id);
            deleteRecord(id);
            var table = $('#example23').DataTable();
            table.row($(_this).parents('tr')).remove().draw();
            event.preventDefault();
            Dialog.showSuccess("记录已删除", "操作成功");
        });

    });
    $("#example23 tbody").on("click", ".edit-button", function (event) {
        var tds = $(this).parents("tr").children();
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if (i != 2 &&i!=3) {
                return true;
            }
            var txt = jqob.text();
            var put = $("<input type='text'>");
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
        var valid_flag=1;
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if(i==2){
                var txt = jqob.children("input").val();
                console.log("length"+txt.length);
                if(txt.length<1){
                    Dialog.showWarning("文件名不能为空哦","提示");
                    valid_flag=0;
                }else{
                    jqob.html(txt);
                    dataTable.cell(jqob).data(txt);
                }
            }else if (!jqob.has('button').length) {
                var txt = jqob.children("input").val();
                jqob.html(txt);
                dataTable.cell(jqob).data(txt);
            }
        });
        if(!valid_flag){
            return;
        }
        var data = row.data();
        var guid = data[0];
        var title = data[2];
        var context = data[3];
        url =ContextPath+module+"?action=modify_record";
        if (guid != "") {
            url += "&guid=" + guid;
        }
        if (title != "") {
            url += "&title=" + title;
        }
        if (context != "") {
            url += "&context=" + context;
        }
        getSelectedRecord(url);
        Dialog.showSuccess("修改记录成功","操作成功");
    });
}
function downloadRecord(url) {
    window.open(url);
}
function modifyRecord(url) {
    $.post(url, function (json) {

    });
}
function deleteRecord(id) {
    var url=ContextPath+module+"?action=delete_record";
    if (id !="") {
        url += "&guid=" + id;
    }
    console.log("删除操作url"+url);
    $.post(url, function (jsonObject) {

    });
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url=initurl+"?action=get_record";
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var title = json[i]["title"];
            var creator = json[i]["creator"];
            var create_time = json[i]["create_time"];
            var user_number = json[i]["user_number"];
            var auth = json[i]["auth"];
            var password = json[i]["password"];
            dataTable.row.add([id, title, creator,create_time,user_number,auth,password]).draw().node();
        }
    });
}
function getSelectedRecord(url){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["guid"];
            var title = json[i]["title"];
            var context = json[i]["context"];
            var user_name = json[i]["user_name"];
            var create_time = json[i]["create_time"];
            var change_time = json[i]["change_time"];
            var download_num = json[i]["download_num"];
            var change_num = json[i]["change_num"];
            var file_url = json[i]["file_url"];
            var authorization = json[i]["authorization"];
            dataTable.row.add([id,file_url, title, context, user_name,create_time, change_time,download_num,change_num,authorization]).draw().node();
        }
    });
}

function addRecord(){

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
function expordCsv(){
    $(".dt-buttons .buttons-csv").click();

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
    if (user_name != "") {
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
