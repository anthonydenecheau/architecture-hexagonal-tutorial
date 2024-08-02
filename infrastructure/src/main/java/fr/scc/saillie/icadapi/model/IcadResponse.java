package fr.scc.saillie.icadapi.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//https://zetcode.com/springboot/restxml/
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("LOF")
public class IcadResponse {

    @JacksonXmlProperty(localName = "DTDECES")
    String dateDeces;
}
