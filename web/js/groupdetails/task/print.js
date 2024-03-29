var Data=[];
var module="/GroupManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
function Record() {
    $(document).ready(function () {
        $('#myTable').DataTable();
        $(document).ready(function () {
            var table = $('#example').DataTable({
                "columnDefs": [{
                    "visible": false,
                    "targets": 2
                }],
                "order": [
                    [2, 'asc']
                ],
                "displayLength": 25,
                "drawCallback": function (settings) {
                    var api = this.api();
                    var rows = api.rows({
                        page: 'current'
                    }).nodes();
                    var last = null;
                    api.column(2, {
                        page: 'current'
                    }).data().each(function (group, i) {
                        if (last !== group) {
                            $(rows).eq(i).before('<tr class="group"><td colspan="5">' + group + '</td></tr>');
                            last = group;
                        }
                    });
                }
            });
            // Order by the grouping
            $('#example tbody').on('click', 'tr.group', function () {
                var currentOrder = table.order()[0];
                if (currentOrder[0] === 2 && currentOrder[1] === 'asc') {
                    table.order([2, 'desc']).draw();
                } else {
                    table.order([2, 'asc']).draw();
                }
            });
        });
    });
    var dataTable = $('#example23').DataTable({
        dom: 'Bfrtip',
        buttons:[],
        searching: false,  //不显示搜索框
        bLengthChange: false,  //不显示有多少条数据
        paging: false, //不显示分页按钮
        info: false, //不显示下端信息
    });
    getAllRecord();
}
function getAllRecord(){
    var dataTable = $('#example23').DataTable();
        var json =$("#jsonData").val();
        var json =jsonData;
        console.log(jsonData);
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["guid"];
            var title = json[i]["title"];
            var context = json[i]["context"];
            var user_name = json[i]["user_name"];
            var create_time = json[i]["create_time"];
            var change_time = json[i]["change_time"];
            var download_num = json[i]["download_num"];
            var change_num = json[i]["change_num"];
            dataTable.row.add([id, title, context, user_name, create_time, change_time, download_num, change_num]).draw().node();
        }
}
function printRecord(){
    window.print();
}
function returnBack(){
    window.location.href="list.jsp";
}
Record();
