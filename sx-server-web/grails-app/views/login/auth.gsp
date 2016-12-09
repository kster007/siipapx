<html>
<head>

    <meta name="layout" content="blank" />
    <title>Acceso</title>
    <style type="text/css" media="screen">
		body{
			background-color: #f3f3f4;
		}
	</style>
</head>

<body class="gray-bg" style="background-color: #f3f3f4;">

<div class="loginColumns animated fadeInDown">
    <div class="row ">
        <div class="col-md-6">
            <h2 class="font-bold">Bienvenido a <span class="text-navy">SIIPAPX </span></h2>
            <p>
                Sistema de administración SIIPAPX
            </p>
            <p><small>Control inteligente para su negocio</small>
            </p>
        </div>

        <div class="col-md-6">
            <div class="ibox-content">
                <g:if test="${flash.message}">
                    <div class="alert alert-danger">
                        ${flash.message}
                    </div>
                </g:if>
                <form class="m-t" role="form"
                      action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
                    <div class="form-group">
                        <input type="text" class="form-control" id="usename"
                               name="username" placeholder="Usuario" autocomplete="off" autofocus="true">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password" name="password" placeholder="password">
                    </div>
                    <button type="submit" class="btn btn-primary block full-width m-b">Login</button>

                </form>
                <p class="m-t">
                    <small>SIIPAPX &copy; 2015</small>
                </p>
            </div>
        </div>

    </div>
    <div class="row">
        <div class="col-md-6">
            Copyright Papel S.A. de C.V.
        </div>
        <div class="col-md-6 text-right">
            <small>© 2014-2015</small>
        </div>
    </div>
</div>


</body>
</html>
