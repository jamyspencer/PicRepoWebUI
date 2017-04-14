<!doctype html>
<html>
<head>
    <title>Login</title
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        input {
            margin-bottom: 10px;
            margin-left: 10px;
        }
    </style>
</head>

<body>
<div class = "container">
    <div class = "row">
        <div class = "col-md-4, col-sm-offset-2, col-lg-5, col-lg-offset-3">
            <h1>Please Sign In</h1>
        </div>
    </div>
    <div class = "row">
        <div class =  "col-md-4, col-md-offset-2, col-lg-5, col-lg-offset-3">
            <form method="post" action="http://hoare.cs.umsl.edu/servlet/j-spencer/picServlet" >
                <table>
                    <tr>
                        <td>Your name: </td>
                        <td><input type="text" name="whoisit"></td>
                    </tr>
                    <tr>
                        <td>Your password: </td>
                        <td><input type="password" name="passwd"></td>
                    </tr>
                    <tr>
                        <td><input type="hidden" name="task" value="0"></td>
                        <td><input type="submit" class="btn pull-right" value="Login"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</body>
</html>