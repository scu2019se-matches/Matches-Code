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
    <title>活动与任务平台 | 组员详情</title>
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
    <input type="hidden" id="member_id" name="member_id" value="<%=request.getParameter("member_id")%>" />

    <!-- Page wrapper  -->
    <div class="page-wrapper">
        <!-- Bread crumb -->
        <div class="row page-titles">
            <div class="col-md-5 align-self-center"></div>
            <div class="col-md-7 align-self-center">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="javascript:void(0)">活动与任务平台</a></li>
                    <li class="breadcrumb-item active">组员详情</li>
                </ol>
            </div>
        </div>
        <!-- End Bread crumb -->
        <!-- Container fluid  -->
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <%--<label id="fold-btn"> <a class="nav-link" href="#"><span><i id="fold-icon" style="font-size:20px" class="fa fa-angle-down"></i></span></a> </label>--%>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="commodityRecord()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">进入商城</span></a> </li>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="taskRecord()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">查看任务</span></a> </li>
                        <li> <a class="nav-link" data-toggle="tab" href="#" onclick="ReturnBack()" role="tab"><span class="hidden-sm-up"></span> <span class="hidden-xs-down">返回</span></a> </li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <div class="row">
                <div class="col-md-3">
                    <div class="card bg-primary p-20">
                        <div class="media widget-ten">
                            <div class="media-left meida media-middle">
                                <span><i class="ti-bag f-s-40"></i></span>
                            </div>
                            <div class="media-body media-text-right">
                                <h2 class="color-white text-white">278</h2>
                                <p class="m-b-0 text-white">积分</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning p-20">
                        <div class="media widget-ten">
                            <div class="media-left meida media-middle">
                                <span><i class="ti-comment f-s-40"></i></span>
                            </div>
                            <div class="media-body media-text-right">
                                <h2 class="color-white text-white">278</h2>
                                <p class="m-b-0 text-white">物品数</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success p-20">
                        <div class="media widget-ten">
                            <div class="media-left meida media-middle">
                                <span><i class="ti-vector f-s-40"></i></span>
                            </div>
                            <div class="media-body media-text-right">
                                <h2 class="color-white text-white">27647</h2>
                                <p class="m-b-0 text-white">成功执行任务</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-danger p-20">
                        <div class="media widget-ten">
                            <div class="media-left meida media-middle">
                                <span><i class="ti-location-pin f-s-40"></i></span>
                            </div>
                            <div class="media-body media-text-right">
                                <h2 class="color-white text-white">278</h2>
                                <p class="m-b-0 text-white">使用物品</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /# card -->
            <div class="card">
                <div class="card-title">
                    <h4>物品仓库</h4>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover ">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>名称</th>
                                <th>价格</th>
                                <th>数量</th>
                                <th>使用</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th scope="row">1</th>
                                <td>Kolor Tea Shirt For Man</td>
                                <td>7</td>
                                <td>
                                    <button type="button" class="btn btn-success btn-xs"><i class="ti-minus"></i></button>
                                    7
                                    <button type="button" class="btn btn-success btn-xs"><i class="ti-plus"></i></button>
                                </td>
                                <td class="color-primary">
                                    <button type="button" class="btn btn-info btn-xs"><i class="ti-close"></i></button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-title">
                    <h4>个人记录</h4>
                </div>
                <div class="card-body">
                    <ul class="timeline">
                        <li>
                            <div class="timeline-badge success"><i class="fa fa-check-circle-o"></i></div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h5 class="timeline-title">完成了英语学习半小时</h5>
                                </div>
                                <div class="timeline-body">
                                    <p>15分钟前</p>
                                </div>
                            </div>
                        </li>
                    </ul>
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


<script src="<%=request.getContextPath()%>/js/groupdetails/memberdetails/list.js"></script>
<%--<script src="<%=request.getContextPath()%>/js/lib/atatables/datatables-init.js"></script>--%>

</body>

</html>
<script type="text/javascript">
    jQuery(document).ready(function() {
        TabView.init();
        MobileClass.init();
    });
</script>