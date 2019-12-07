<%--
  Created by IntelliJ IDEA.
  User: silenus
  Date: 2019/6/12
  Time: 21:01
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if(session.getAttribute("id") == null || session.getAttribute("check") == null)
    {
%>
        <script type="text/javascript">
            console.log("check login failed");
            swal({
                        title: "请先登录",
                        type: "error",
                        closeOnConfirm: true,
                        showLoaderOnConfirm: true,
                    },
                    function(){
                        window.location.href="<%=request.getContextPath()%>/login.jsp";
                    }
            );
        </script>
<%
        return;
    }
%>

<script type="text/javascript">
var MobileClass = function(){
    var init_header = function(){
        console.log("init_header");
        document.getElementById("header_username").innerHTML="<%=session.getAttribute("username")%>";
    };
    var init_sidebar_menu = function(){
        console.log("page_sidebar_menu action getmenu");
        $.post("<%=request.getContextPath()%>/AccountAction?action=getmenu", function(menuList){
            console.log("init_sidebar_menu getmenu callback");
            html = "";
            for(var i in menuList)
            {
                var menuItem = menuList[i];
                if(menuItem==null)continue;
                html +=
                        "<li class='nav-devider'></li><li>"
                        +"   <a class='has-arrow' href='#' aria-expanded='false'>"
                        +"   <i class='fa fa-tachometer'></i>"
                        +"   <span class='hide-menu'>"
                        +       menuItem.title
                        +"  </span></a>"
                        +"	<ul aria-expanded='false' class='collapse'>";
                for(var j in menuItem.sub)
                {
                    var subMenuItem = menuItem.sub[j];
                    html += "<li><a href='<%=request.getContextPath()%>" + subMenuItem.href + "'>" + subMenuItem.title + "</a></li>";
                }
                html += "</ul></li>";
            }
            document.getElementById("sidebar-menu").innerHTML = html;
            $("#sidebar-menu").metisMenu();
        });
    };
    return {
        init: function(){
            console.log("enter MobileClass.init");
            init_header();
            init_sidebar_menu();
            console.log("exit MobileClass.init");
        }
    };
}();

function sendRedirect(URL, PARAMTERS) {
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    temp_form.target = "_self";
    temp_form.method = "post";
    temp_form.acceptCharset = "UTF-8";
    temp_form.style.display = "none";
    for (var name in PARAMTERS) {
        var opt = document.createElement("textarea");
        opt.name = name;
        opt.value = PARAMTERS[name];
        temp_form.appendChild(opt);
        console.log(opt);
    }
    document.body.appendChild(temp_form);
    temp_form.submit();
};

String.format = function() {
    if( arguments.length == 0 )
        return null;

    var str = arguments[0];
    for(var i=1;i<arguments.length;i++) {
        var re = new RegExp('\\{' + (i-1) + '\\}','gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
}

var Dialog = function() {
    var showSuccess = function(sMsg, sTitle){
        toastr.success(sMsg, sTitle,{
            "positionClass": "toast-top-full-width",
            timeOut: 1000,
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "tapToDismiss": false
        });
    };

    var showError = function(sMsg, sTitle){
        toastr.error(sMsg, sTitle,{
            "positionClass": "toast-top-full-width",
            timeOut: 5000,
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "tapToDismiss": false
        });
    };

    var showInfo = function(sMsg, sTitle) {
        toastr.info(sMsg,sTitle,{
            "positionClass": "toast-top-full-width",
            timeOut: 3000,
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "tapToDismiss": false
        })
    }

    var showWarning = function(sMsg, sTitle) {
        toastr.warning(sMsg, sTitle,{
            "positionClass": "toast-top-full-width",
            timeOut: 3000,
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "tapToDismiss": false
        })
    }

    var showComfirm = function(sText, sTitle, fnCallback) {
        swal({
                    title: sTitle,
                    text: sText,
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定",
                    cancelButtonText: "放弃",
                    closeOnConfirm: true
                }
            ,fnCallback
        );
    };
    var showText = function(sText, sTitle, fnCallback) {
        swal(sText,sTitle);
    };
    var showPrompt= function(sText, sTitle, fnCallback) {
        swal({
                title: sTitle,
                text: sText,
                type: "input",
                showCancelButton: true,
                closeOnConfirm: true,
                confirmButtonText: "确定",
                cancelButtonText: "放弃",
                animation: "slide-from-top",
                inputPlaceholder: "输入区"
            },
        function(inputValue){
            $("input[name='inputTmp']").val(inputValue);
            if (inputValue === false) return false;
            if (inputValue === "") {
                swal.showInputError("输入不能为空");
                return false;
            }
            swal("Hey !!", "You wrote: " + inputValue, "success");
        }
        );
    };

    return {
        showSuccess: showSuccess,
        showError: showError,
        showWarning: showWarning,
        showInfo: showInfo,
        showComfirm: showComfirm,
        showText:showText,
        showPrompt:showPrompt
    };
}();
var Time=function(){
    var MinToMinute=function(time){
        time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
        time = time.split(':');
        return time[1]+"月"+time[2]+"日  "+time[3]+":"+time[4];
    };
    var MinToHour=function(time){
        time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
        time = time.split(':');
        return time[1]+"月"+time[2]+"日"+time[3]+"时";
    };
    var MinToDay=function(time){
        time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
        time = time.split(':');
        return time[1]+"月"+time[2]+"日";
    };
    var StdToMinute=function(time){
        time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
        time = time.split(':');
        return time[0]+"-"+time[1]+"-"+time[2]+" "+time[3]+":"+time[4];
    };
    var StdToDay=function(time){
        time = time.replace(/-/g,':').replace(' ',':'); // 注意，第二个replace里，是' '，中间有个空格，千万不能遗漏
        time = time.split(':');
        return time[0]+"-"+time[1]+"-"+time[2];
    };
    return{
        MinToMinute:MinToMinute,
        MinToHour:MinToHour,
        MinToDay:MinToDay,
        StdToMinute:StdToMinute,
        StdToDay:StdToDay,
    };
}();
var DatePicker=function(){
    var DateRangeFromToday=function (target){
        var locale = {
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        };
        $(target).daterangepicker({
            "locale": locale,
            "separator":" to ",
            "opens":"left",
            "format":"YYYY-MM-DD",
            "ranges" : {
                '一周': [ moment(),moment().add('days', 7)],
                '本月': [moment(),moment().endOf("month")],
            },
            startDate: moment(),
            endDate: moment().add(3,'days'),
            minDate:moment(),
            maxDate: moment().add(90,'days'),
        });
    }
    var DateTimeRangeFromToday=function (target){
        var locale = {
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        };
        $(target).daterangepicker({
            "separator":" to ",
            "locale": locale,
            "format":"YYYY-MM-DD HH:mm",
            "ranges" : {
                '一周': [ moment(),moment().add('days', 7)],
                '本月': [moment(),moment().endOf("month")],
            },
            startDate: moment(),
            endDate: moment().add(3,'days'),
            minDate:moment(),
            maxDate: moment().add(90,'days'),
            "opens":"left",
            "timePicker":true,
            "timePickerIncrement" : 1,
            "timePicker12Hour":false,
        });
    }
    var DateRangeToToday=function (target){
        var locale = {
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        };
        $(target).daterangepicker({
            "locale": locale,
            "separator":" to ",
            "opens":"left",
            "format":"YYYY-MM-DD",
            "ranges" : {
                '今日': [moment().startOf('day'), moment()],
                '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()],
                '本月': [moment().startOf("month"),moment().endOf("month")],
                '上个月': [moment().subtract(1,"month").startOf("month"),moment().subtract(1,"month").endOf("month")]
            },
            startDate: moment().subtract(3,'days'),
            endDate: moment(),
            maxDate:moment(),
            minDate: moment().subtract(90,'days'),
        });
    }
    var DateTimeRangeToToday=function (target){
        var locale = {
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        };
        $(target).daterangepicker({
            "locale": locale,
            "separator":" to ",
            "opens":"left",
            "format":"YYYY-MM-DD HH:mm",
            "ranges" : {
                '今日': [moment().startOf('day'), moment()],
                '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
                '最近7日': [moment().subtract('days', 6), moment()],
                '最近30日': [moment().subtract('days', 29), moment()],
                '本月': [moment().startOf("month"),moment().endOf("month")],
                '上个月': [moment().subtract(1,"month").startOf("month"),moment().subtract(1,"month").endOf("month")]
            },
            startDate: moment().subtract(3,'days'),
            endDate: moment(),
            maxDate:moment(),
            minDate: moment().subtract(90,'days'),
            "timePicker":true,
            "timePickerIncrement" : 1,
            "timePicker12Hour":false,
        });
    }
    var DateTimeFromToday=function (target) {
        $(target).datetimepicker({
            opens : 'right', //日期选择框的弹出位置,
            format: 'yyyy-mm-dd hh:ii',
            autoclose: true,   //选择后自动关闭当前时间控件
            startDate:new Date(),
            todayBtn:'linked',
            language: 'cn',  //修改默认为cn
            todayHighlight: true,
            forceParse:true,
        });
    }
    var DateTimeToToday=function (target) {
        $(target).datetimepicker({
            opens : 'right', //日期选择框的弹出位置,
            format: 'yyyy-mm-dd hh:ii',
            autoclose: true,   //选择后自动关闭当前时间控件
            endDate:new Date(),
            todayBtn:'linked',
            language: 'cn',  //修改默认为cn
            todayHighlight: true,
            forceParse:true,
        });
    }
    var DateTime=function (target) {
        $(target).datetimepicker({
            opens : 'right', //日期选择框的弹出位置,
            format: 'yyyy-mm-dd hh:ii',
            autoclose: true,   //选择后自动关闭当前时间控件
            todayBtn:'linked',
            language: 'cn',  //修改默认为cn
            todayHighlight: true,
            forceParse:true,
            isVisible:false,
        });
    }
    return{
        DateRangeFromToday:DateRangeFromToday,
        DateTimeRangeFromToday:DateTimeRangeFromToday,
        DateRangeToToday:DateRangeToToday,
        DateTimeRangeToToday:DateTimeRangeToToday,
        DateTimeFromToday:DateTimeFromToday,
        DateTimeToToday:DateTimeToToday,
        DateTime:DateTime,
    };
}();


</script>