/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti.exception;

public class FusNonSalvataException extends RuntimeException {

    /*public FusNonSalvataException() {
    }*/

    public FusNonSalvataException(String message) {
        super(message);
    }

    public FusNonSalvataException(String message, Throwable cause) {
        super(message, cause);
    }

    /*public FusNonSalvataException(Throwable cause) {
        super(cause);
    }

    public FusNonSalvataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }*/

}
