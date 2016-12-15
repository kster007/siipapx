<ul class="nav metismenu" id="side-menu">
    <li class="nav-header">
        
        <div class="logo-element">
            SX
        </div>
    </li>
    <li class="${webRequest.controllerName == 'quartz' ? 'active':''}">
        <g:link action="list" controller="quartz">
            <i class="fa fa-cogs"></i> Jobs
            <i class="fa fa-angle-right pull-right"></i>
        </g:link>
    </li>
    <li>
        <a href="index.html">
            <i class="fa fa-exchange"></i> <span class="nav-label">Replicadores</span> <span class="fa arrow"></span>
        </a>
        <ul class="nav nav-second-level collapse">
            <li class="">
                <g:link action="productos" controller="replicador">
                     Productos
                </g:link>
            </li>
            
        </ul>
    </li>
    

</ul>