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
                    <form id="newTask" method="post" action="/GroupTask?action=add_record">
                        <input type="hidden" id="begin_time" name="begin_time">
                        <input type="hidden" id="end_time" name="end_time">
                        <input type="hidden" id="group_id" name="group_id">
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label>描述</label>
                                    <input type="text" id="context" name="context" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label class="control-label">积分</label>
                                    <input type="number" id="grades" name="grades" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label class="control-label">任务范围</label>
                                    <input type="text" id="dateRangeSelect" name="dateRangeSelect" class="form-control">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%--</form>--%>