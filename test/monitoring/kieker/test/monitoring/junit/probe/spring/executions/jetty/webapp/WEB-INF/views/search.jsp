<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
	<head>
		<title>Result</title>
	</head>
	<body>
        We have found this very nice book for you: <strong><c:out value="${result}"></c:out></strong>
	</body>
</html>