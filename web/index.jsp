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
    <title>活动与任务平台 | 首页</title>
    <%@include file="page_css.jsp"%>
</head>

<body class="header-fix fix-sidebar">
    <!-- Main wrapper  -->
    <div id="main-wrapper">
        <%@include file="page_header.jsp"%>
        <%@include file="page_sidebar_menu.jsp"%>
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
                                    <div class="stat-heading color-primary">个人中心</div>
                                    <div class="stat-text">查看用户信息</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/activity/list.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon">
                                    <i class="ti-file bg-success"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-success">活动管理</div>
                                    <div class="stat-text">查看活动</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-3">
                        <div class="card">
                            <a href="<%=request.getContextPath()%>/group/list.jsp"></a>
                            <div class="stat-widget-five">
                                <div class="stat-icon">
                                    <i class="ti-info bg-danger"></i>
                                </div>
                                <div class="stat-content">
                                    <div class="stat-heading color-danger">任务管理</div>
                                    <div class="stat-text">创建与进入分组</div>
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
                    <div class="col-lg-6">
                        <div class="card">
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
                    <div class="col-lg-6">
                        <div class="card">
                            <div class="card-title">
                                <h4>关注的活动</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>名称</th>
                                            <th>发布者</th>
                                            <th>组织</th>
                                            <th>举办时间</th>
                                            <th>状态</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>
                                            </td>
                                            <td>Lew Shawon</td>
                                            <td><span>Dell-985</span></td>
                                            <td><span>456 pcs</span></td>
                                            <td><span class="badge badge-success">已结束</span></td>
                                        </tr>
                                        </tbody>
                                    </table>
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
<script src="<%=request.getContextPath()%>/js/userpanel.js"></script>