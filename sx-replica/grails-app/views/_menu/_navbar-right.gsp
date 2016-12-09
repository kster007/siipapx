<ul class="nav navbar-top-links navbar-right">
    <li>
        <g:link controller="home">
            <span class="m-r-sm text-muted welcome-message">${session.modulo?:'SX-REPLICA'}
        </span>
        </g:link>

    </li>
    

    <li class="dropdown">
        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
            <i class="fa fa-bell"></i>  <span class="label label-primary"></span>
        </a>
        <ul class="dropdown-menu dropdown-alerts">
            <li>
                No hay alertas en este momento
            </li>
            <li class="divider"></li>
            <li>
                <div class="text-center link-block">
                    <a href="notifications.html">
                        <strong>Ver todas las alertas</strong>
                        <i class="fa fa-angle-right"></i>
                    </a>
                </div>
            </li>
        </ul>
    </li>

    

    <li>
        <a class="right-sidebar-toggle">
            <i class="fa fa-tasks"></i>
        </a>
    </li>
    <li>
        <g:form controller="logout" class="" >
            <button class="btn btn-w-m btn-link" type="submit" > <i class="fa fa-sign-out"></i></i> Cerrar sesión</button>
        </g:form>

    </li>
</ul>