<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row">
    <div class="col-lg-6">

        <div class="form-group">
            <label class="control-label">字段选择</label>
            <select id="key1" class="form-control">
                <option value="create_time" selected="selected">创建时间</option>
                <option value="user_number" >组员数量</option>
            </select>
        </div>
        <div class="form-group">
            <select id="key2" class="form-control">
                <option value="" selected="selected">请选择</option>
                <option value="create_time">创建时间</option>
                <option value="user_number" >组员数量</option>
            </select>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="form-group">
            <label class="control-label">字段规则</label>
            <select id="rule1" class="form-control" data-placeholder="asc" tabindex="1">
                <option value="asc" selected="selected">升序</option>
                <option value="desc">降序</option>
            </select>
        </div>
        <div class="form-group">
            <select id="rule2" class="form-control" data-placeholder="asc" tabindex="2">
                <option value="asc" selected="selected">升序</option>
                <option value="desc">降序</option>
            </select>
        </div>
    </div>
</div>
<button type="button" onclick="sortRecord()" class="btn btn-default btn-flat m-b-10">确认</button>