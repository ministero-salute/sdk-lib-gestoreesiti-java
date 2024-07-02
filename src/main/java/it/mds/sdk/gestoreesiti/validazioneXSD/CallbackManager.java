/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti.validazioneXSD;

import it.mds.sdk.gestoreesiti.validazioneXSD.ErrorTrace;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

@Slf4j
public class CallbackManager extends DefaultHandler {

    private ErrorTrace oErrorTrace = new ErrorTrace();

    public ErrorTrace getErrorTrace() {
        return this.oErrorTrace;
    }

    public void error(SAXParseException exception) throws SAXParseException {

        log.debug("{}.error() ", this.getClass().getName());

        StringBuffer oBuffer = new StringBuffer(300);
        oBuffer.append("ERRORE VERIFICATO ALLA RIGA/COLONNA:" + exception.getLineNumber() + "/"
                + exception.getColumnNumber());
        oBuffer.append("   MESSAGGIO ERRORE::" + exception.getMessage());
        // ****************************************************/
//			if (exception.getMessage().indexOf("cvc-complex-type.3.2.2") == -1)
        {
            this.oErrorTrace.addErrorBuffer(oBuffer);
        }
        if (exception.getMessage().contains("cvc-elt.1")) {
            throw new SAXParseException(exception.getMessage(), "", "", exception.getLineNumber(),
                    exception.getColumnNumber(), exception);
        }
        // ****************************************************/
    }

    public void fatalError(SAXParseException exception) {
        log.debug("{}.fatalError() ", this.getClass().getName());
        StringBuffer oBuffer = new StringBuffer(300);
        oBuffer.append("ERRORE VERIFICATO ALLA RIGA/COLONNA:" + exception.getLineNumber() + "/"
                + exception.getColumnNumber());
        oBuffer.append("   MESSAGGIO ERRORE::" + exception.getMessage());
        this.oErrorTrace.addErrorBuffer(oBuffer);
    }

//    private void logDebug(String sMessage) {
//        System.out.println("[CallbackManager]" + sMessage);
//    }
}