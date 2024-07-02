/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti.exception;

public class EsitoNonSalvatoException extends RuntimeException {

    public EsitoNonSalvatoException() {
    }

    public EsitoNonSalvatoException(String message) {
        super(message);
    }

    public EsitoNonSalvatoException(String message, Throwable cause) {
        super(message, cause);
    }

    /*public EsitoNonSalvatoException(Throwable cause) {
        super(cause);
    }

    public EsitoNonSalvatoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }*/
}
