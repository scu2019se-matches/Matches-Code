var Data=[];
var module="/MemberPanel";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var GroupId=$("#group_id").val();
var UserId=$("#user_id").val();
var MemberId=$("#member_id").val();
var Auth=$("#auth").val();
function getAllRecord(){
    getUserData();
    getCommodity();
    statisticRecord();
}
function getUserData(){
    var url=ContextPath+"/GroupMember?action=get_record&group_id="+GroupId+"&user_id="+MemberId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        var grades = json[0].grades;
        var commodity = json[0].commodity;
        document.getElementById("grades").innerHTML=grades;
        document.getElementById("commodity").innerHTML=commodity;
    });
}
function statisticRecord(){
    var url=initurl+"?action=getStatistics&group_id="+GroupId+"&member_id="+MemberId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        var html="";
        for (var i = 0; i < json.length; i++) {
            var id = json[i]["id"];
            var operator_id = json[i]["operator_id"];
            var operator = json[i]["operator"];
            var member_id = json[i]["member_id"];
            var member = json[i]["member"];
            var object = json[i]["object"];
            var type = json[i]["type"];
            var context = json[i]["context"];
            var remarks = json[i]["remarks"];
            var create_time = json[i]["create_time"];
            html+= "<li>"
            if(object=="task"){
               html+="    <div class=\"timeline-badge success\"><i class=\"fa fa-check-circle-o\"></i></div>"
            }else if(object=="grades"){
                html+="    <div class=\"timeline-badge warning\"><i class=\"fa fa-sun-o\"></i></div>"
            }else if(type=="buy"){
                html+="    <div class=\"timeline-badge warning\"><i class=\"fa fa-sun-o\"></i></div>"
            }else if(type=="sell"){
                html+="    <div class=\"timeline-badge danger\"><i class=\"fa fa-times-circle-o\"></i></div>"
            }else if(type="use"){
                html+="    <div class=\"timeline-badge primary\"><i class=\"fa fa-smile-o\"></i></div>"
            }
            html+= "    <div class=\"timeline-panel\">"
            +"        <div class=\"timeline-heading\">"
            if(object=="task"){
                html+="          <h5 class=\"timeline-title\">"+operator+"完成了任务 "+context+"</h5>"
            }else if(object=="grades"){
                html+="          <h5 class=\"timeline-title\">"+operator+"改动了积分 "+context+"</h5>"
                html+="<p class=\"badge badge-success\">"+"备注:"+remarks+"</p>"

            }else if(type=="buy"){
                html+="          <h5 class=\"timeline-title\">"+operator+"购买了物品 "+context+"</h5>"
            }else if(type=="sell"){
                html+="          <h5 class=\"timeline-title\">"+operator+"售出了物品 "+context+"</h5>"
            }else if(type="use"){
                html+="          <h5 class=\"timeline-title\">"+operator+"使用了物品 "+context+"</h5>"
            }
            html+= "        </div>"
            +"       <div class=\"timeline-body\">"
            +"          <p>"+changeTimeFormat(create_time)+"</p>"
            +       "</div>"
            +      "</div>"
            +"</li>"
        }
        document.getElementById("member_record").innerHTML=html;
    });
};
function getCommodity(){
    var url=initurl+"?action=get_record&group_id="+GroupId+"&member_id="+MemberId;
    // alert(url);
    $.post(url, function (json) {
        Data = json;
        console.log(json);
        var html="";
        for (var i = 0; i < json.length; i++) {
            if(json[i]==null)continue;
            var id = json[i]["id"];
            var group_id = json[i]["group_id"];
            var commodity_id = json[i]["commodity_id"];
            var commodity = json[i]["commodity"];
            var grades = json[i]["grades"];
            var count = json[i]["count"];
            var auth = json[i]["auth"];
            html+=
            "<tr>"
            +"<th scope=\"row\">"+commodity_id+"</th>"
            +"<td>"+commodity+"</td>"
            +"<td>"+grades+"</td>"
            +"<td>"
            if(auth==1){
                html+="<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\"ti-minus\"></i></button>"
            }
            html+="<span class=\"badge\">"+count+"</span>"
            if(auth==1){
                html+="<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\"ti-plus\"></i></button>"
                +"</td>"
                +"<td class=\"color-primary\">"
                +"<button type=\"button\" class=\"btn btn-info btn-xs\"><i class=\"ti-close\"></i></button>"
                +"</td>"
                +"</tr>"
            }else{
                html+="</td>"
                    +"<td><p class=\"badge badge-warning\">无此权限</p></td>";
            }
        }
        document.getElementById("commodity_table").innerHTML=html;
    });
}
function buyCommdity(){
    
}
function sellCommdity() {
    
}
function useCommdity(){

}


function commodityRecord(){
    var group_id=$("#group_id").val();
    window.location.href="../commodity/list.jsp?group_id="+group_id;
}
function taskRecord(){
    var group_id=$("#group_id").val();
    window.location.href="../task/list.jsp?group_id="+group_id;
}
function ReturnBack(){
    history.go(-1);
}
getAllRecord();

function addGrades(){
    if(Auth>1){
        swal({
                title: "输入奖赏积分",
                text: "1-1000",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                confirmButtonText: "确定",
                cancelButtonText: "放弃",
                animation: "slide-from-top",
                inputPlaceholder: "输入区"
            },function (num){
                if (num<1||num>1000) {
                    swal.showInputError("范围1-1000");
                    return false;
                }
                swal({
                    title: "输入备注",
                    text: "可不填",
                    type: "input",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonText: "确定",
                    cancelButtonText: "放弃",
                    animation: "slide-from-top",
                    inputPlaceholder: "输入区"
                },function (context) {
                    url=ContextPath+"/MemberPanel?action=modify_grades&operator_id="+UserId+"&group_id="+GroupId+
                    "&member_id="+MemberId+"&grades="+num+"&context="+context;
                    console.log(url);
                    $.post(url, function (json2) {
                        swal({
                            title : "操作完成",
                            type : "success",
                        }, function() {
                            location.reload();
                        });
                    });
                })
            }
        );
    }else{
        Dialog.showError("提示","你无权进行此操作");
    }
}
function subGrades(){
    if(Auth>1){
        swal({
                title: "输入惩罚积分",
                text: "1-1000",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                confirmButtonText: "确定",
                cancelButtonText: "放弃",
                animation: "slide-from-top",
                inputPlaceholder: "输入区"
            },function (num){
                if (num<1||num>1000) {
                    swal.showInputError("范围1-1000");
                    return false;
                }
                swal({
                    title: "输入备注",
                    text: "可不填",
                    type: "input",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonText: "确定",
                    cancelButtonText: "放弃",
                    animation: "slide-from-top",
                    inputPlaceholder: "输入区"
                },function (context) {
                    num=-num;
                    url=ContextPath+"/MemberPanel?action=modify_grades&operator_id="+UserId+"&group_id="+GroupId+
                        "&member_id="+MemberId+"&grades="+num+"&context="+context;
                    console.log(url);
                    $.post(url, function (json2) {
                        swal({
                            title : "操作完成",
                            type : "success",
                        }, function() {
                            location.reload();
                        });
                    });
                })
            }
        );
    }else{
        Dialog.showError("提示","你无权进行此操作");
    }
}


function changeTimeFormat(time){
    return time;
    time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
    time = time.split(':');
    return time[1]+"月"+time[2]+"日  "+time[3]+":"+time[4];
}
