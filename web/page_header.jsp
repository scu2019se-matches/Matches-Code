<%--
  Created by IntelliJ IDEA.
  User: silenus
  Date: 2019/6/12
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="header">
    <nav class="navbar top-navbar navbar-expand-md navbar-light">
        <!-- Logo -->
        <div class="navbar-header">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">
                <!-- Logo icon -->
                <b><img src="<%=request.getContextPath()%>/images/logo.png" alt="homepage" class="dark-logo" /></b>
                <!--End Logo icon -->
                <!-- Logo text -->
                <span alt="homepage" class="dark-logo" >活动与任务平台</span>
                <%--<span><img src="<%=request.getContextPath()%>/images/logo-text.png" alt="homepage" class="dark-logo" /></span>--%>
            </a>
        </div>
        <!-- End Logo -->
        <div class="navbar-collapse">
            <!-- toggle and nav items -->
            <ul class="navbar-nav mr-auto mt-md-0">
                <!-- This is  -->
                <li class="nav-item"> <a class="nav-link toggle-nav hidden-md-up text-muted  " href="javascript:void(0)"><i class="mdi mdi-menu"></i></a> </li>
                <li class="nav-item m-l-10"> <a class="nav-link sidebartoggle hidden-sm-down text-muted  " href="javascript:void(0)"><i class="ti-menu"></i></a> </li>
            </ul>
            <!-- User profile and search -->
            <ul class="navbar-nav my-lg-0">
                <!-- Profile -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-muted  " href="#" id="2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="fa fa-envelope"></i>
                        <div class="notify"> <span class="heartbit"></span> <span class="point"></span> </div>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right mailbox animated slideInRight" aria-labelledby="2">
                        <ul>
                            <li>
                                <div class="drop-title">你收到的回复</div>
                            </li>
                            <li>
                                <div class="header-notify">
                                    <!-- Message -->
                                    <a href="#">
                                        <div class="notification-contnet">
                                            <div class="row">
                                                <h3 class="mail-desc">老王回复了你</h3>
                                            </div>
                                            今天晚上一起做项目吗
                                        </div>
                                    </a>
                                    <a href="#">
                                        <div class="notification-contnet">
                                            <div class="row">
                                                <h3 class="mail-desc">XX评论了你</h3>
                                            </div>
                                            这个活动策划的挺好
                                        </div>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <a class="nav-link text-center" href="javascript:void(0);"> See all e-Mails <i class="fa fa-angle-right"></i> </a>
                            </li>
                        </ul>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-muted  " href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-user"></i></a>
                    <div class="dropdown-menu dropdown-menu-right animated slideInRight">
                        <ul class="dropdown-user">
                            <li role="separator" class="divider"></li>
                            <li><a href="<%=request.getContextPath()%>/userinfo/index.jsp"> 个人信息</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="<%=request.getContextPath()%>/AccountAction?action=logout"> 退出登录</a></li>
                        </ul>
                    </div>
                </li>
            </ul>
            <span>当前用户：</span><span id="header_username">sss</span>
        </div>
    </nav>
</div>