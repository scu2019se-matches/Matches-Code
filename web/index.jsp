<%--
  Created by IntelliJ IDEA.
  User: silenus
  Date: 2019/6/12
  Time: 20:04
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
    <link rel="icon" type="image/png" sizes="16x16" href="images/favicon.png">
    <title>移动互动课堂 | 首页</title>
    <%@include file="page_css.jsp"%>
</head>

<body class="header-fix fix-sidebar">
    <!-- Main wrapper  -->
    <div id="main-wrapper">
        <%@include file="page_header.jsp"%>
        <%@include file="page_sidebar_menu.jsp"%>
        <!-- Page wrapper  -->
        <div class="page-wrapper">
            <!-- Bread crumb -->
            <div class="row page-titles">
                <div class="col-md-5 align-self-center"></div>
                <div class="col-md-7 align-self-center">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="javascript:void(0)">移动互动课堂</a></li>
                        <li class="breadcrumb-item active">首页</li>
                    </ol>
                </div>
            </div>
            <!-- End Bread crumb -->
            <!-- Container fluid  -->
            <div class="container-fluid">
                <!-- Start Page Content -->
                <div class="row">
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/userinfo/index.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon">
                                    <i class="ti-home bg-primary"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-primary">用户信息管理</div>
                                    <div class="stat-text">系统用户概览</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/questionnaire/publish/questionnaire_list.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon">
                                    <i class="ti-file bg-success"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-success">问卷管理</div>
                                    <div class="stat-text">问卷概览</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/file/file_list.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon">
                                    <i class="ti-info bg-danger"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-danger">文件管理</div>
                                    <div class="stat-text">文件概览</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/auth/index.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon bg-warning">
                                    <i class="ti-world"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-warning">权限管理</div>
                                    <div class="stat-text">用户权限概览</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End PAge Content -->
            </div>
            <!-- End Container fluid  -->
           <%@include file="page_footer.jsp"%>
        </div>
        <!-- End Page wrapper  -->
    </div>
    <!-- End Wrapper -->
    <%@include file="page_js.jsp"%>
    <%@include file="js/mobileclass.jsp"%>
    <%@include file="js/index_js.jsp"%>
</body>

</html>
<script type="text/javascript">
    jQuery(document).ready(function() {
        MobileClass.init();
        Page.init();
    });
</script>