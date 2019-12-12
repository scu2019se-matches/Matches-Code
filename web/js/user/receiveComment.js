var Data=[];
var ContextPath=$("#ContextPath").val();
var commenturl=ContextPath+"/CommentManagement";
var UserId=$("#user_id").val();
var ActivityId=$("#activity_id").val();
function getRecord() {
    getComment();
}
function getComment(){
    var url=commenturl+"?action=get_receiveComment";
    $.post(url, function (json) {
        var html="";
        for(var i=0;i<json.length;i++){
            Data = json[i];
            let tmp="";
            var id=Data["id"];
            var creator=Data["creator"];
            var cite_id=Data["cite_id"];
            var citeuser=Data["citeuser"];
            var create_time=Time.MinToMinute(Data["create_time"]);
            var context=Data["context"];
            var auth=Data["auth"];

            tmp="<div class=\"media\"><div class=\"media-body\">" +
                "<a href=\"#\"><h4 onclick=enterAct("+activity_id+") class=\"media-heading\">"+context+"</h4></a>"
            if(cite_id==0){
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
function reply_comment(id){
    var id="block"+id;
    $('#'+id).css('display','inline');
}
function cancel_comment(id){
    var id="block"+id;
    $('#'+id).css('display','none');
}
function enterAct(id){
    window.location.href="../activity/detail.jsp?activity_id="+id;
}
getRecord();

