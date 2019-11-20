<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="<%=request.getContextPath()%>/image/png" sizes="16x16" href="<%=request.getContextPath()%>/images/favicon.png">
    <title>活动与任务平台 | 每日任务</title>

    <!-- Custom CSS -->
    <%@include file="../page_css.jsp"%>
</head>

<body class="header-fix fix-sidebar">
<!-- Main wrapper  -->
<div id="main-wrapper">
    <%@include file="../page_header.jsp"%>
    <%@include file="../page_sidebar_menu.jsp"%>
    <input type="hidden" id="ContextPath" name="ContextPath" value="<%=request.getContextPath()%>" />
    <input type="hidden" id="user_id" name="user_id" value="<%=session.getAttribute("id")%>" />
    <input type="hidden" id="auth" name="auth" value="<%=session.getAttribute("auth")%>" />
    <!-- Page wrapper  -->
    <div class="page-wrapper">
        <!-- Bread crumb -->
        <div class="row page-titles">
            <div class="col-md-5 align-self-center"></div>
            <div class="col-md-7 align-self-center">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="javascript:void(0)">活动与任务平台</a></li>
                    <li class="breadcrumb-item active">每日任务</li>
                </ol>
            </div>
        </div>
        <!-- End Bread crumb -->
        <!-- Container fluid  -->
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <div class="card-title">
                        <h4>任务</h4>
                    </div>
                    <div class="todo-list">
                        <div class="tdl-holder">
                            <div class="tdl-content">
                                <ul id="task_list_todo">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- End PAge Content -->
        <!-- End Container fluid  -->
        <%@include file="../page_footer.jsp"%>
    </div>
    <!-- End Page wrapper  -->
</div>
<!-- End Wrapper -->
<%@include file="../page_js.jsp"%>
<%@include file="../js/mobileclass.jsp"%>
<script src="<%=request.getContextPath()%>/js/tabview.js"></script>
<script src="<%=request.getContextPath()%>/js/user/todayTask.js"></script>
<%--<script src="<%=request.getContextPath()%>/js/lib/atatables/datatables-init.js"></script>--%>

</body>

</html>
<script type="text/javascript">
    jQuery(document).ready(function() {
        TabView.init();
        MobileClass.init();
    });
</script>