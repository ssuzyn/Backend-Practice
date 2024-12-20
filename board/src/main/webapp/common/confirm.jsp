<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${userinfo ne null}">
	<div class="row justify-content-center">
	  <div class="col-lg-8 col-md-10 col-sm-12 m-3 text-end">
		<strong>${userinfo.userName}</strong> (${userinfo.userId})님 안녕하세요.
		<a href="${root}/user?action=logout">로그아웃</a><br />
	  </div>
	</div>
</c:if>
<c:if test="${empty userinfo}">
	<script>
	alert("로그인이 필요한 페이지입니다.");
	location.href = "${root}/user?action=mvlogin";
	</script>
</c:if>