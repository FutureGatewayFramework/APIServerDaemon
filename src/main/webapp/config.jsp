<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>APIServerDaemon</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
       <![endif]-->        
    </head>
    <body>
        <h1>APIServerDaemon</h1>
        <p>
            APIServerDaemon web application installed at : ${contextPath}
        </p>
        <h2>APIServerDaemon DB settings</h2>
        <p>
            <div class="table-responsive">
            <table class="table">
                <tr><td>apisrv_dbhost</td><td>${apisrv_dbhost}</td></tr>
                <tr><td>apisrv_dbport</td><td>${apisrv_dbport}</td></tr>
                <tr><td>apisrv_dbname</td><td>${apisrv_dbname}</td></tr>
                <tr><td>apisrv_dbuser</td><td>${apisrv_dbuser}</td></tr>
                <tr><td>apisrv_dbpass</td><td>${apisrv_dbpass}</td></tr>
            </table>
            </div>
        </p>
        <h2>APIServerDaemon Threads settings</h2>
            <div class="table-responsive">
            <table class="table">
                <tr><td>asdMaxThreads</td><td>${asdMaxThreads}</td></tr>
                <tr><td>asdCloseTimeout</td><td>${asdCloseTimeout}</td></tr>
            </table>
            </div> 
        <h1>Target settings</h1>
        <h2>GridEngine</h2>
        <p>GridEngine' UsersTracking Database
            <div class="table-responsive">
            <table class="table">
                <tr><td>utdb_jndi</td><td>${utdb_jndi}</td></tr>
                <tr><td>utdb_host</td><td>${utdb_host}</td></tr>
                <tr><td>utdb_port</td><td>${utdb_port}</td></tr>
                <tr><td>utdb_name</td><td>${utdb_name}</td></tr>
                <tr><td>utdb_user</td><td>${utdb_user}</td></tr>
                <tr><td>utdb_pass</td><td>${utdb_pass}</td></tr>
            </table>
            </div>
        </p>
        
    </body>
</html>
