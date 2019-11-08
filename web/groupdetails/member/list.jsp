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
    <title>活动与任务平台 | 小组成员</title>
    <link href="<%=request.getContextPath()%>/css/lib/dropzone/dropzone.css" rel="stylesheet">
    <!-- Custom CSS -->
    <%@include file="../../page_css.jsp"%>
</head>

<body class="header-fix fix-sidebar">
<!-- Main wrapper  -->
<div id="main-wrapper">
    <%@include file="../../page_header.jsp"%>
    <%@include file="../../page_sidebar_menu.jsp"%>
    <input type="hidden" id="ContextPath" name="ContextPath" value="<%=request.getContextPath()%>" />
    <input type="hidden" id="group_id" name="group_id" value="<%=request.getParameter("group_id")%>" />
    <input type="hidden" id="user_id" name="user_id" value="<%=session.getAttribute("id")%>" />

    <!-- Page wrapper  -->
    <div class="page-wrapper">
        <!-- Bread crumb -->
        <div class="row page-titles">
            <div class="col-md-5 align-self-center"></div>
            <div class="col-md-7 align-self-center">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="javascript:void(0)">活动与任务平台</a></li>
                    <li class="breadcrumb-item active">小组成员</li>
                </ol>
            </div>
        </div>
        <!-- End Bread crumb -->
        <!-- Container fluid  -->
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <label id="fold-btn"> <a class="nav-link" href="#"><span><i id="fold-icon" style="font-size:20px" class="fa fa-angle-down"></i></span></a> </label>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="commodityRecord()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">商城</span></a> </li>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="taskRecord()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">任务</span></a> </li>
                        <li class="nav-item"> <a class="nav-link" data-toggle="tab" href="#tab-query" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">查询</span></a> </li>
                        <li class="nav-item"> <a class="nav-link" data-toggle="tab" href="#tab-sort" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">排序</span></a> </li>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="getAllRecord()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">显示所有</span></a> </li>


                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                                <span class="hidden-sm-up"></span> <span class="hidden-xs-down">更多</span>
                            </a>
                            <div class="dropdown-menu">
                                <span class="dropdown-item" onclick="printRecord()" role="tab">打印</span>
                                <span class="dropdown-item" onclick="expordExcel()" role="tab">导出Excel</span>
                            </div>
                        </li>
                    </ul>
                    <div id="tab-content" class="tab-content tabcontent-border p-20" style="display: none;">
                        <div role="tabpanel" class="tab-pane active" id="tab-query">
                            <%@include file="query.jsp"%>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="tab-sort">
                            <%@include file="sort.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">小组成员列表</h4>
                    <h6 class="card-subtitle">这是所有小组成员</h6>
                    <div class="table-responsive m-t-40">
                        <table id="example23" class="display nowrap table table-hover table-bordered" cellspacing="2px" width="100%">
                            <thead>
                            <tr>
                                <th>组员ID</th>
                                <th>昵称</th>
                                <th>加入时间</th>
                                <th>现有积分</th>
                                <th>物品数</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tfoot>
                            <tr>
                                <th>组员ID</th>
                                <th>昵称</th>
                                <th>加入时间</th>
                                <th>现有积分</th>
                                <th>物品数</th>
                                <th>操作</th>
                            </tr>
                            </tfoot>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- End PAge Content -->
        <!-- End Container fluid  -->
        <%@include file="../../page_footer.jsp"%>
    </div>
    <!-- End Page wrapper  -->
</div>
<!-- End Wrapper -->
<%@include file="../../page_js.jsp"%>
<%@include file="../../js/mobileclass.jsp"%>
<script src="<%=request.getContextPath()%>/js/tabview.js"></script>


<script src="<%=request.getContextPath()%>/js/lib/datatables/datatables.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
<script src="<%=request.getContextPath()%>/js/lib/dropzone/dropzone.js"></script>


<script src="<%=request.getContextPath()%>/js/groupdetails/member/list.js"></script>
<%--<script src="<%=request.getContextPath()%>/js/lib/atatables/datatables-init.js"></script>--%>

</body>

</html>
<script type="text/javascript">
    jQuery(document).ready(function() {
        TabView.init();
        MobileClass.init();
    });
</script>