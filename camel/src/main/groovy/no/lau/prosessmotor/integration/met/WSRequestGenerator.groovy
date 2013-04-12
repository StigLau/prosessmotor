package no.lau.prosessmotor.integration.met

class WSRequestGenerator {
    String method, timeserietypeID, dateformat /* format us not required */, username /* username is not required for queries */
    def stations = [], elements = [], hours = [], months = []
    def from = "", to = ""

    String toString() {
        String a = """<met:$method soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:met="MetService">\n"""
        if (timeserietypeID)
            a += """<timeserietypeID xsi:type="xsd:string">$timeserietypeID</timeserietypeID>\n"""
        if (dateformat != null)
            a += """<format xsi:type="xsd:string">$dateformat</format>\n"""
        if (from)
            a += """<from xsi:type="xsd:string">${format(from)}</from>\n"""
        if (to)
            a += """<to xsi:type="xsd:string">${format(to)}</to>\n"""
        if (stations)
            a += """<stations xsi:type="xsd:string">${format(stations)}</stations>\n"""
        if (elements)
            a += """<elements xsi:type="xsd:string">${format(elements)}</elements>\n"""
        if (hours)
            a += """<hours xsi:type="xsd:string">${format(hours)}</hours>\n"""
        if (months != null)
            a += """<months xsi:type="xsd:string">${format(months)}</months>\n"""
        if(username != null)
            a += """<username xsi:type="xsd:string">$username</username>\n"""
        a += """</met:$method>"""
        return a
    }

    String format(Date date) { date.format("yyyy-MM-dd") }

    String format(List stringList) { stringList.join(',') }

    String format(String string) { string }
}