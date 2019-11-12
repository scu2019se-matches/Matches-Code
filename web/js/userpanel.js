var Data=[];
var module="/UserPanel";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var UserId=$("#user_id").val();
var Auth=$("#auth").val();
function getAllRecord(){
    getTask();
    // getActivity();
}
function getTask(){
    var url=initurl+"?action=get_task"+"&user_id="+UserId;
    $.post(url, function (json) {
        Data = json;
        // console.log(json);
        var html="";
        var html1="";
        for (var i = 0; i < json.length; i++) {
            var task_id = json[i]["task_id"];
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
                    +"<input type=\"checkbox\"><i class=\"bg-primary\"></i><span>"+creator+"发布:     "+context+"</span>"
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
        document.getElementById("task_list_todo").innerHTML=html+html1;
    });
};

function changeTimeFormat(time){
    return time;
    time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
    time = time.split(':');
    return time[1]+"月"+time[2]+"日  "+time[3]+":"+time[4];
}
getAllRecord();