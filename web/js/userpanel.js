var Data=[];
var module="/UserPanel";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var taskurl=ContextPath+module;
var activityurl=ContextPath+"/ActivityManagement";
var commenturl=ContextPath+"/CommentManagement";
var UserId=$("#user_id").val();
var Auth=$("#auth").val();
function getAllRecord(){
    getTask();
    getActivity();
    // getComment();
}
function getTask(){
    var url=taskurl+"?action=get_task"+"&user_id="+UserId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        var html="";
        var html1="";
        for (var i = 0; i < json.length; i++) {
            var task_id = json[i]["id"];
            var group_id = json[i]["group_id"];
            var context = json[i]["context"];
            var creator = json[i]["creator"];
            var grades = json[i]["grades"];
            var create_time = json[i]["create_time"];
            var begin_time = json[i]["begin_time"];
            var end_time = json[i]["end_time"];
            var task_status = json[i]["task_status"];
            var my_status = json[i]["my_status"];
            var auth = json[i]["auth"];
            if(my_status==1){
                html+="<li class=\"color-primary\">"
                    +"<label>"
                    +"<input type=\"checkbox\" id=\""+"task-"+group_id+"-"+task_id+"\">"
                    +"<i class=\"bg-primary\"></i><span>"
                    +creator+"发布:     "+context+"</span>"
                    +"</label>"
                    +"</li>"
            }else if(my_status==0){
                html1+="<li class=\"color-primary\">"
                    +"<label>"
                    +"<input type=\"checkbox\" checked='true' disabled='true'><i class=\"bg-primary\"></i><span>"+creator+"发布:     "+context+"</span>"
                    +"</label>"
                    +"</li>"
            }
        }
        if(json.length==0){
            html="<div class='alert alert-light'>暂无任务</div>";
        }
        document.getElementById("task_list_todo").innerHTML=html+html1;

    });
};
function getActivity(){
    var url=activityurl+"?action=get_activity"+"&user_id="+UserId;
    $.post(url, function (json) {
        Data = json;
        var html="";
        var html1="";
        var html2="";
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
            var tmp="<tr>"
                +"<td>"+headline+"</td>"
                +"<td>"+publisher+"</td>"
                +"<td><span>"+tag+"</span></td>"
                +"<td><span>"+Time.MinToMinute(begin_time)+"</span></td>"
            if(act_status==0){
                html+=tmp+"<td><span class=\"badge badge-primary\">未开始</span></td>"
                +"</tr>"
            }else if(act_status==1){
                html1+=tmp+"<td><span class=\"badge badge-success\">进行中</span></td>"
                    +"</tr>"
            }else{
                html2+=tmp+"<td><span class=\"badge badge-danger\">已结束</span></td>"
                    +"</tr>"
            }
        }
        if(json.length==0){
            html="<div class='alert alert-light'>暂无关注活动</div>";
        }
        document.getElementById("focus_act").innerHTML=html+html1+html2;

    });
};
$('ul#task_list_todo').on('click','input',function () {
    var id=$(this)[0].id;
    [group_id,task_id]=getTaskByRegex(id);
    var url=taskurl+"?action=finish_task&group_id="+group_id+"&user_id="+UserId
        +"&task_id="+task_id;
    $.post(url, function (json) {
        getTask();
    });
});
function getTaskByRegex(input){
    if(input){
        input = input.split('-');
        return [input[1],input[2]];
    }
    return [0,0];
}
getAllRecord();