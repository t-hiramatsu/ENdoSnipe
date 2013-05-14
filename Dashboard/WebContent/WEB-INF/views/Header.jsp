<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<table class="headerTable">
		<tr>
			<td>
				<img src="<%=request.getContextPath()%>/resources/images/ENdoSnipe_logo.png" />
			</td>
			<td width="90%">
				<a href="<%=request.getContextPath()%>/">Dashboard</a>
				<a href="<%=request.getContextPath()%>/map/mapList">Map</a>
			</td>
		</tr>
	</table>
</body>
<script>
	$(".headerTable a").button();
</script>
</html>