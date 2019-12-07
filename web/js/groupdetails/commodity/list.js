var Data=[];
var module="/Commodity";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var group_id=$("#group_id").val();
var user_id=$("#user_id").val();
var initurl=ContextPath+module;
function Record(){
    console.log("group_id = " + group_id);
    console.log("user_id = " + user_id);

    $.post(String.format("{0}?action={1}&groupId={2}",initurl,"getOwner",group_id),function(res){
        if(res.errno == 0 && res.owner == user_id){
            $('#navAddCommodity').css('visibility','visible');
        }
    });

    $.fn.dataTable.ext.errMode = "none";
    var dataTable=$('#example23').DataTable({
        order: [],
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
            "sZeroRecords":  "<span'>没有找到对应的商品！</span>",
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
                "targets": 3,
                "orderable": false,
                "mRender":
                    function(data, type, full) {
                        sReturn = ""
                        if(full[3] == user_id) {
                            sReturn = sReturn +
                                "<button type=\"button\" class=\"edit-button btn btn-success btn-sm btn-rounded m-b-10 m-l-5\">修改</button>" +
                                "<button type=\"button\" class=\"delete-button btn btn-info btn-sm btn-rounded m-b-10 m-l-5\">删除</button>"
                        }
                        sReturn = sReturn+"<button type=\"button\" class=\"enter-button btn btn-primary btn-sm btn-rounded m-b-10 m-l-5\">兑换</button>";
                        return sReturn;
                    },
            }
        ],
        "aLengthMenu": [[10,15,20,25,40,50,-1],[10,15,20,25,40,50,"所有商品"]],
    });
    getAllRecord();

    $('#example23 tbody').on('click', '.enter-button', function (event) {
        var row = dataTable.row($(this).parents("tr"));
        var data = row.data();
        var id = data[0];
        // console.log(data);
        buyCommodity(id);
    });
    $('#example23 tbody').on('click', '.delete-button', function (event) {
        var _this=this;
        Dialog.showComfirm("确定要删除吗？", "警告", function(){
            var id = $(_this).parent().prev().prev().prev().text();
            console.log("id"+id);
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
            if (i != 1 && i != 2 ) {
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
        var grades_format = /\d+/;
        $.each(tds, function (i, val) {
            var jqob = $(val);
            if(i==1){
                var txt = jqob.children("input").val();
                // console.log("length"+txt.length);
                if(txt.length<1){
                    Dialog.showWarning("商品名不能为空哦","提示");
                    valid_flag=0;
                }else{
                    jqob.html(txt);
                    dataTable.cell(jqob).data(txt);
                }
            }
            else if(i==2){
                var txt = jqob.children("input").val();
                if(!grades_format.test(txt)){
                    Dialog.showWarning("分数格式不正确哦","提示");
                    valid_flag=0;
                }else{
                    jqob.html(txt);
                    dataTable.cell(jqob).data(txt);
                }
            }
            else if (!jqob.has('button').length) {
                var txt = jqob.children("input").val();
                jqob.html(txt);
                dataTable.cell(jqob).data(txt);
            }
        });
        if(!valid_flag){
            return;
        }
        var data = row.data();
        var id = data[0];
        var context = data[1];
        var grades = data[2];
        var url = String.format(
            "{0}?action={1}&groupId={2}&id={3}&context={4}&grades={5}",
            initurl, "modifyRecord", group_id, id, context, grades
        );
        $.post(url, function(res){
            Dialog.showSuccess("修改成功","操作成功");
            sortRecord();
        });
    });
}
function buyCommodity(sender, commodityId) {
    var url = String.format("{0}{1}?action={2}&commodityId={3}&groupId={4}",
        ContextPath, module, "buyCommodity", commodityId, group_id);
    alert(url);
    $.post(url, function (json) {
        if(json.errno != 0){
            Dialog.showWarning(json.msg, "");
        }else{
            Dialog.showSuccess("兑换成功", "");
        }
    });
}
function modifyRecord(url) {
    $.post(url, function (json) {
        sortRecord();
    });
}
function deleteRecord(id) {
    var url=ContextPath+module+"?action=deleteCommodity";
    if (id !="") {
        url += "&id=" + id;
    }
    // console.log("删除操作url"+url);
    $.post(url, function (jsonObject) {
        sortRecord();
    });
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    var url = String.format("{0}?action={1}&groupId={2}",
        initurl, "getRecord", group_id);
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var title = json[i]["context"];
            var grades = json[i]["grades"];
            var creatorId = json[i]["creatorId"];
            dataTable.row.add([id, title, grades, creatorId]).draw().node();
        }
    });
}
function getSelectedRecord(url){
    var dataTable = $('#example23').DataTable();
    dataTable.clear().draw(); //清除表格数据
    $.post(url, function (json) {
        Data = json;
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var context = json[i]["context"];
            var creatorId = json[i]["creatorId"];
            var grades = json[i]["grades"];
            dataTable.row.add([id, context, grades,creatorId]).draw().node();
        }
    });
}

function addRecord(){
    var form = document.getElementById('newCommodity');
    if(form.add_context.value != "" && form.add_grades.value != ""){
        var url = String.format(
            "{0}?action={1}&groupId={2}&context={3}&grades={4}",
            initurl, "addCommodity", group_id, form.add_context.value, form.add_grades.value
        )
        $.get(url, function(res){
            Dialog.showSuccess("添加成功", "");
            sortRecord();
        });
    }
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
    var url = String.format(
        "{0}?action={1}&groupId={2}",
        initurl, "getRecord", group_id
    );
    var query_context = $("#query_context").val();
    var query_grades = $("#query_grades").val();
    if (query_context != "") {
        url += "&context=" + query_context;
    }
    if (query_grades != "") {
        url += "&grades=" + query_grades;
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

function goback(){
    sendRedirect('../member/list.jsp', {'group_id':group_id});
}

function searchRecord(){
    sortRecord();
    //var query_context = $("#query_context").val();
    //var query_grades = $("#query_grades").val();
    //var url = String.format(
    //    "{0}?action={1}&groupId={2}",
    //    initurl, "getRecord", group_id);
    //if (query_context != "") {
    //    url += "&context=" + query_context;
    //}
    //if (query_grades != "") {
    //    url += "&grades=" + query_grades;
    //}
    //getSelectedRecord(url);
};

Record();
