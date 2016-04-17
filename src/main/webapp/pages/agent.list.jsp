<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="tajne">
    <meta name="author" content="ja">

    <title>Starter Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <style>
        body {
            padding-top: 5rem;
        }

        .push-down {
            margin-top: 80px;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-fixed-top navbar-dark bg-inverse">
    <a class="navbar-brand" href="#">MI-6 Dashboard</a>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-xs-4 col-xs-offset-2">
            <h2>Add new agent</h2>
            <form method="POST" action="add-agent">
                <div class="form-group row">
                    <label for="inputEmail3" class="col-sm-4 form-control-label">Agent name</label>
                    <div class="col-sm-8">
                        <input type="text" name="name" placeholder="agent name" class="form-control"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputEmail3" class="col-sm-4 form-control-label">Agent cover name</label>
                    <div class="col-sm-8">
                        <input type="text" name="coverName" placeholder="cover name" class="form-control"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputEmail3" class="col-sm-4 form-control-label">Weapon of choice</label>
                    <div class="col-sm-8">
                        <input type="text" name="favWeapon" placeholder="Favourite weapon" class="form-control"/>
                    </div>
                </div>
                <button type="submit" class="btn btn-success">Send agent</button>
            </form>
        </div>
    </div>
    <div class="row push-down">
        <div class="col-xs-8 col-xs-offset-2">
            <h2>List of agents</h2>
            <form method="POST" action="delete-agents">
                <table class="table table-hover">
                    <thead class="thead-default">
                    <tr>
                        <th>#</th>
                        <th>covername</th>
                        <th>realname</th>
                        <th>fav wep</th>
                        <th>delete</th>
                        <th>edit</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${agentList}" var="entry" varStatus="i">
                        <tr>
                            <td>
                                <c:out value="${i.index+1}"/>
                            </td>
                            <td>
                                <c:out value="${entry.coverName}"/>
                            </td>
                            <td>
                                <c:out value="${entry.name}"/>
                            </td>
                            <td>
                                <c:out value="${entry.favouriteWeapon}"/>
                            </td>
                            <td>
                                <input type="checkbox" name="id" value="<c:out value="${entry.id}" />"/>
                            </td>
                            <td>
                                <a href="edit?id=<c:out value="${entry.id}"/>" class="btn btn-warning-outline">edit</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-danger">Delete checked</button>
            </form>
        </div>
    </div>

</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>