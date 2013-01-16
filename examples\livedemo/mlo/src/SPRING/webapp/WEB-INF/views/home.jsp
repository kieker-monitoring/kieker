<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Bookstore</title>
    </head>
    <body>
        <h1>Welcome to the best bookstore</h1>
        <form action="search" method="get">
            Search for books:  <input type="text" name="term" />
            <input type="submit" value="Search" />
        </form> 
    </body>
</html>