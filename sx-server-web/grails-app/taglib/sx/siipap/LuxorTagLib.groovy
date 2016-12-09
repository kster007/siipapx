package sx.siipap

class LuxorTagLib {
    static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace="lx"

    def header = { attrs, body ->
        out << "<div class='row wrapper border-bottom white-bg page-heading' >"
        out << body()
        out << "</div>"
    }

    def ibox = { attrs, body ->
        out << "<div class='ibox float-e-margins'>"
        out << body()
        out << "</div>"
    }

    def iboxContent = { attrs, body ->
        out << "<div class='ibox-content'>"
        out << body()
        out << "</div>"
    }

    /**
     * Titulo para el Ibox
     *
     * @attrs title  Título para el ibox
     */
    def iboxTitle = { attrs, body ->
        out << "<div class='ibox-title'>"
        out << "<h5>${attrs.title}</h5>"
        out << body()
        out << "</div>"
    }

    def iboxTools = {attrs, body ->
        out << "<div class='ibox-tools'>"
        out << body()
        out << "<a class='collapse-link'><i class='fa fa-chevron-up'></i></a>"

        out << "</div>"
    }

    def iboxFooter = { attrs, body ->
        out << "<div class='ibox-footer'>"
        out << body()
        out << "</div>"
    }
}
