package it.mds.sdk.gestoreesiti.validazioneXSD;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.*;

@Slf4j
public class MainTester {
	public boolean xmlValidationAgainstXSD(File oXML, InputStream schema) {

		log.debug("{}.valida() - File {} - Schema {} - BEGIN", this.getClass().getName(), oXML.getName(), schema.toString());
		SAXParserFactory sp = SAXParserFactory.newInstance(); //NOSONAR
		sp.setNamespaceAware(true);
		sp.setValidating(true); //NOSONAR
		// sp.setValidating(false);
		CallbackManager oCallbackManager = new CallbackManager();

		try {
			SAXParser oSAXParser = sp.newSAXParser();
			oSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			oSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schema);
			try {
				oSAXParser.parse(oXML, oCallbackManager);
			} catch (SAXException | IOException e) {
				StringBuffer sb = new StringBuffer();
				sb.append("ERRORE NEL PROCESSO DI VALIDAZIONE " + e.getMessage());
				log.error(e.getMessage(), e);
				oCallbackManager.getErrorTrace().addErrorBuffer(sb);
				return false;
			}
		} catch (ParserConfigurationException | SAXException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("ERRORE NEL PROCESSO DI VALIDAZIONE", e);
		}

		StringBuffer sb = oCallbackManager.getErrorTrace().getErrors();
		return true;
	}

}
