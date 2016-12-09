<div id="right-sidebar">
    <div class="sidebar-container">

        <ul class="nav nav-tabs navs-3">
            <li class="active"><a data-toggle="tab" href="#tab-1">
                Notas
            </a></li>
            <li><a data-toggle="tab" href="#tab-2">
                Tareas
            </a></li>
            <li class=""><a data-toggle="tab" href="#tab-3">
                <i class="fa fa-gear"></i>
            </a></li>
        </ul>

        <div class="tab-content">


            <div id="tab-1" class="tab-pane active">

                <div class="sidebar-title">
                    <h3> <i class="fa fa-comments-o"></i> Notas</h3>
                    %{-- <small><i class="fa fa-tim"></i> You have 10 new message.</small> --}%
                </div>

                <div>

                </div>
            </div>

            <div id="tab-2" class="tab-pane">

                <div class="sidebar-title">
                    <h3> <i class="fa fa-cube"></i> Tareas</h3>
                    <small><i class="fa fa-tim"></i> No tiene tareas pendientes</small>
                </div>

                <ul class="sidebar-list">

                </ul>
            </div>

            <div id="tab-3" class="tab-pane">

                <div class="sidebar-title">
                    <h3><i class="fa fa-gears"></i> Preferencias</h3>

                </div>

                <ul class="sidebar-list">
                    <li>
                        <g:link controller="usuario" action="list">
                            <div class="small pull-right m-t-xs"></div>
                            <h4><i class="fa fa-users"></i> Usuarios</h4>
                        </g:link>
                    </li>
                </ul>

                <div class="setings-item">
                    <span>Notificaciones</span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example">
                            <label class="onoffswitch-label" for="example">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="setings-item">
                    <span>Chat</span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="collapsemenu" checked class="onoffswitch-checkbox" id="example2">
                            <label class="onoffswitch-label" for="example2">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>


                <div class="sidebar-content">
                    <h4>Configuración</h4>
                    <div class="small">

                    </div>
                </div>


            </div>
        </div>

    </div>
</div>

