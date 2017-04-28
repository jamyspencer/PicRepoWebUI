<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <title>Pics</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        . {
            font-size: 1.2em;
        }
        div.scrolling {
            height: 400px;
            overflow: scroll;
        }
        pre {
            background-color: inherit;
            border-width: 0px;
        }
        .field {
            background-color: #f5f5f5;
        }
        .form-group{
            border: 2px solid #ccc;
            border-radius: 4px;
            padding: 5px;
            margin: 5px;
        }
        .btn{
            margin-top: 10px;
            min-width: 120px;
            max-width: 100%;
        }
    </style>
</head>

<header>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="http://hoare.cs.umsl.edu/servlet/j-spencer/picServlet">PicSearch</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li><a href="http://hoare.cs.umsl.edu/servlet/j-spencer/picServlet?logout=true&sessionID=${sessionID}">Logout</a></li>
                </ul>

            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

</header>

<body>
<div class = "container-fluid">
    <div class = "row">

        <div class="col-xs-12 col-sm-12 col-md-3 col-lg-2">
            <div class = "row">
                <form class="form-group" method="get" action="http://hoare.cs.umsl.edu/servlet/j-spencer/picServlet" >
                    <h2>Search Pic Database</h2>
                    <label for="searchterm">Search for Tag</label>
                    <input id="searchterm" class="field" style="width: 100%; margin-bottom: 10px;"  type="text" name="search_term" hint="Search Term">
                    <input type="hidden" name="sessionID" value="${sessionID}">
                    <input type="hidden" name="task" value="search">
                    <input type="submit" class="btn pull-right" value="Search">
                    <span style="clear:both"/>

                </form>
            </div>
            <div class = "row">
                <form class="form-group"method="post" action="http://hoare.cs.umsl.edu/servlet/j-spencer/picServlet" >
                    <h2>Login to Upload</h2>
                    <label for="userid">Log-in Name</label>
                    <input id="userid" class="field" style="width: 100%;" type="text" hint="login id" name="whoisit">
                    <label for="passwd">Password</label>
                    <input id="passwd" class="field" style="width: 100%; margin-bottom: 10px;" type="password" hint="Password" name="passwd">
                    <input type="hidden" name="sessionID" value="${sessionID}">
                    <input type="hidden" name="task" value="login">
                    <input type="submit" class="btn pull-right" value="Login">
                    <span style="clear:both"/>

                </form>
            </div>
            <div class = "row">
                <form class="form-group" method="post" action="http://hoare.cs.umsl.edu/servlet/j-spencer/infoServlet" >
                    <h2>Project Details</h2>
                    <label for="files">Select File</label>
                    <div id="files" class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="servlet" checked>
                            Servlet
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="search.jsp">
                            search.jsp
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="Add_Pic.jsp">
                            Add_Pic.jsp
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="web.xml">
                            web.xml
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="Pic.java">
                            Pic.java
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="PicList.java">
                            PicList.java
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input class="form-check-input" type="radio" name="files"  value="CustomSession.java">
                            CustomSession.java
                        </label>
                    </div>
                    <input type="submit" class="btn pull-right" value="Search">
                    <span style="clear:both"/>
                </form>
            </div>
        </div>
        <div id="pic_container" class="col-xs-12 col-sm-12 col-md-9 col-lg-10">
            ${picHTML}
        </div>


    </div>
</div>
</body>
</html>