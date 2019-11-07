<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Start Page Content -->
<button type="button" onclick="addRecord()" class="btn btn-primary m-b-10 m-l-5">创建</button>
<%--<form  role="form" id="page_form" name="page_form" action="add_record">--%>
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <form id="newGroup">
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label>昵称</label>
                                    <input type="text" id="title" name="title" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label class="control-label">密码</label>
                                    <input type="text" id="password" name="password" class="form-control">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%--</form>--%>