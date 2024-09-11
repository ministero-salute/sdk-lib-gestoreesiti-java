/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti.validazioneXSD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabio Staro
 *
 */

public final class ErrorTrace {
	
	private List oErrors = null;
	
	public ErrorTrace()
	{
		oErrors = new ArrayList();
	}
	
	public void addErrorBuffer(StringBuffer oError)
	{
		oErrors.add(oError);
	}
	
	public StringBuffer getErrors()
	{
		StringBuffer oBuffer = new StringBuffer(500);
		for (int i=0; i< this.oErrors.size();i++)
		{			
			if (oBuffer.length()>10000000)
			{
				StringBuffer messaggioDimensioneElevata = new StringBuffer("ATTENZIONE:\nIL FILE XML CONTIENE UN NUMERO DI ERRORI ELEVATO.\nSI RIPORTANO SOLO I PRIMI 10MByte RISULTANTI DALLA VALIDAZIONE\n\n\n");
				messaggioDimensioneElevata.append(oBuffer);
				oBuffer = messaggioDimensioneElevata;
				break; //limito a 10 MByte la dimensione del testo degli errori XSD				
			}
			oBuffer.append(this.oErrors.get(i)+"\n");
		}
		
		return oBuffer;
	}
	
	public boolean areThereErrors()
	{
		if (null == this.oErrors || (null != this.oErrors && this.oErrors.size()==0))
			return false;
		else return true;
	}
}
