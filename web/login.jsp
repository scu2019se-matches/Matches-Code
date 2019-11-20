<%--
  Created by IntelliJ IDEA.
  User: silenus
  Date: 2019/6/12
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    var flag=<%=session.getAttribute("login_errno")!=null && ((int)session.getAttribute("login_errno"))!=0%>;
    console.log(flag);
    if(flag)
    {
        alert("<%=session.getAttribute("login_msg")%>");
    }
</script>
<%
    String email=session.getAttribute("email")==null ? "" : (String)session.getAttribute("email");
    session.setAttribute("login_errno", 0);
%>
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
    <title>活动与任务平台 | 登录</title>
    <!-- Custom CSS -->

    <link href="css/style.css" rel="stylesheet">
    <style>
        body {
            background-image: url("images/wallpaper/7.png");
            /*background-position: center center;*/
            background-attachment: fixed;
            background-size: cover;
            /*background-size:100% 100%;*/
        }

    </style>

</head>

<body class="header-fix fix-sidebar">

<!-- Main wrapper  -->
<div id="main-wrapper">

    <div class="unix-login">
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-4">
                    <div class="login-content card">
                        <div class="login-form">
                            <h4>登录</h4>
                            <form action="<%=request.getContextPath()%>/AccountAction" method="post">
                                <input type="hidden" name="action" value="login">
                                <div class="form-group">
                                    <label>邮箱</label>
                                    <input name="email" type="text" class="form-control" placeholder="请填写注册过的邮箱" value="<%=email%>">
                                </div>
                                <div class="form-group">
                                    <label>密码</label>
                                    <input name="password" type="password" class="form-control" placeholder="请输入密码">
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox"> 记住我
                                    </label>
                                    <label class="pull-right">
                                        <a href="#">忘记密码？</a>
                                    </label>
                                </div>
                                <button type="submit" class="btn btn-primary btn-flat m-b-30 m-t-30">登录</button>
                                <div class="register-link m-t-15 text-center">
                                    <p>还没有账号？<a href="register.jsp">点这里注册</a></p>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- End Wrapper -->

<!-- All Jquery -->
<script src="js/lib/jquery/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<script src="js/lib/bootstrap/js/popper.min.js"></script>
<script src="js/lib/bootstrap/js/bootstrap.min.js"></script>
<!-- slimscrollbar scrollbar JavaScript -->
<script src="js/jquery.slimscroll.js"></script>
<!--Menu sidebar -->
<script src="js/sidebarmenu.js"></script>
<!--stickey kit -->
<script src="js/lib/sticky-kit-master/dist/sticky-kit.min.js"></script>
<!--Custom JavaScript -->
<script src="js/custom.min.js"></script>

</body>

</html>
