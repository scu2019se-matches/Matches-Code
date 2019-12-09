var Data=[];
var module="/ActivityManagement";
var existResultset="0";
var ContextPath=$("#ContextPath").val();
var initurl=ContextPath+module;
var commenturl=ContextPath+"/CommentManagement";
var UserId=$("#user_id").val();
var ActivityId=$("#activity_id").val();
function getRecord() {
    getDetail();
    getComment();
}
function getDetail(){
    var url=initurl+"?action=get_record&activity_id="+ActivityId;
    $.post(url, function (json) {
        Data = json[0];
        console.log(json)

        $("#headline").html(Data["headline"]);
        $("#publisher").html(Data["publisher"]);
        $("#create_time").html("发布于"+Time.StdToMinute(Data["create_time"]));
        $("#site").html(Data["site"]);
        $("#time_range").html(Time.StdToMinute(Data["begin_time"])+" 至 "
            +Time.StdToMinute(Data["end_time"]));
        $("#tag").html(Data["tag"]);
        $("#description").html("简介："+Data["description"]);
    });
    // alert(url);
}
function getComment(){
    var url=commenturl+"?action=get_comment&activity_id="+ActivityId;
    $.post(url, function (json) {
        var html="";
        for(var i=0;i<json.length;i++){
            Data = json[i];
            let tmp="";
            var id=Data["id"];
            var creator=Data["creator"];
            var citeuser=Data["citeuser"];
            var create_time=Time.MinToMinute(Data["create_time"]);
            var context=Data["context"];
            var auth=Data["auth"];

            tmp="<div class=\"media\"><div class=\"media-body\"><h4 class=\"media-heading\">"+context+"</h4>"
            if(citeuser==""||citeuser==null){
                tmp+="<div><span>"+creator+"</span>"
            }else{
                tmp+="<div><span>"+creator+"</span> @ <span>"+citeuser+"</span>"
            }
            tmp+="<p style='float:right'>"
            if(auth>1){
                tmp+="<a href=\"#\"><i onclick=del_comment("+id+") class=\"ti-close color-danger\"></i></a>"
            }
            tmp+="<a href=\"#\"><i onclick=reply_comment("+id+") class=\"fa fa-reply color-primary\"></i></a>"
                +"</p></div>"
            tmp+="<p class=\"comment-date\">"+create_time+"</p>"
            tmp+="<div style=\"display: none\" id=block"+id+">"
            +"<input type=\"text\" id=input"+id+" class=\"form-control\" placeholder=\"输入评论\">"
            +"<p></p><button type=\"button\" onclick=\"submit_comment("+id+")\" class=\"btn btn-success\">确认</button>"
            +"<button type=\"button\" onclick=\"cancel_comment("+id+")\" class=\"btn btn-inverse\">取消</button>"
            +"</div></div> </div>"
            html+=tmp;
        }
        document.getElementById("comment_list").innerHTML=html;
    });
}
function del_comment(id){
    Dialog.showComfirm("确定要删除吗？", "警告", function(){
        var url=commenturl+"?action=del_comment&comment_id="+id;
        $.post(url, function (json) {
            Dialog.showSuccess("已删除", "操作成功");
            getRecord();
        });
    });

}
function reply_comment(id){
    var id="block"+id;
    $('#'+id).css('display','inline');
}
function submit_comment(id){
    var context=$('#input'+id).val();
    if(context==null||context==""){
        Dialog.showWarning("回复不能为空","提示");
        return;
    }
    if(context.length>30){
        Dialog.showWarning("回复长度限制30","提示");
        return;
    }
    var url=commenturl+"?action=reply_comment&comment_id="+id+"&context="+context;
    $.post(url, function (json) {
        Dialog.showSuccess("回复成功", "操作成功");
        getRecord();
    });
}
function cancel_comment(id){
    var id="block"+id;
    $('#'+id).css('display','none');
}
getRecord();


function add_comment(){
    var context=$("#context").val();
    if(context==null||context==""){
        Dialog.showWarning("水评警告","提示");
        return;
    }
    if(context.length>30){
        Dialog.showWarning("长度限制30","提示");
        return;
    }
    var url=commenturl+"?action=add_comment&activity_id="+ActivityId+"&context="+context;
    $.post(url, function (json) {
        Dialog.showSuccess("评论成功", "操作成功");
        getRecord();
    });
}
function returnBack(){
    window.location.href=ContextPath+"/activity/list.jsp";
};

