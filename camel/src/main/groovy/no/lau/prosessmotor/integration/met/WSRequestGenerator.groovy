package no.lau.prosessmotor.integration.met

class WSRequestGenerator {
    String method, timeserietypeID, dateformat /* format us not required */, username /* username is not required for queries */
    def stations = [], elements = [], hours = [], months = []
    def from = "", to = ""

    String toString() {
        """<met:$method soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:met="MetService">
            ${formatType('timeserietypeID', timeserietypeID)}
            ${formatType('format', dateformat)}
            ${formatType('from', from)}
            ${formatType('to', to)}
            ${formatType('stations', stations)}
            ${formatType('elements', elements)}
            ${formatType('hours', hours)}
            ${months!=null?"""<months xsi:type="xsd:string">${format(months)}</months>""":""}
            ${formatType('username', username)}
        </met:$method>"""
    }



    String formatType(String name, def object1) {
        object1?"""<$name xsi:type="xsd:string">${format(object1)}</$name>""":""
    }


    String format(Date date) { date.format("yyyy-MM-dd") }

    String format(List stringList) { stringList.join(',') }

    String format(String string) { string }
}