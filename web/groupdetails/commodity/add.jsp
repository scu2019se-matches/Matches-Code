<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Start Page Content -->
<button type="button" onclick="addCommodity()" class="btn btn-primary m-b-10 m-l-5">添加商品</button>
<%--<form  role="form" id="page_form" name="page_form" action="add_record">--%>
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <form id="newCommodity" method="post" action="Commodity">
                        <input type="hidden" name="action" value="addCommodity">
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label>商品名称</label>
                                    <input type="text" id="context" name="title" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label class="control-label">商品积分</label>
                                    <input type="text" id="grades" name="password" class="form-control">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%--</form>--%>