package com.google.android.fhir.document.scan

/**
 * The [SHLinkScanner] interface defines a contract for scanning Smart Health Link (SHL) QR codes.
 * Implementations of this interface are responsible for handling the scanning process of SHL QR
 * codes and providing relevant data through callbacks.
 *
 * The SHL QR code scanning process is outlined in detail in its documentation
 * [SHL Documentation](https://docs.smarthealthit.org/smart-health-links/).
 *
 * ## Scanning Process:
 * A SHL QR code contains a Smart Health Link. The scanning implementation should extract this link
 * and process it to provide an initialised [SHLinkScanData] object.
 *
 * ## Callbacks:
 * - `successCallback`: Invoked when the SHL QR code is successfully scanned, providing an
 *   initialised [SHLinkScanData] object.
 * - `failCallback`: Invoked when an error occurs during the scanning process, providing an [Error]
 *   object with details about the failure.
 */
interface SHLinkScanner {

  /**
   * Scans a SHL QR code and returns an initialised [SHLinkScanData] object through the success
   * callback or reports an error through the fail callback.
   *
   * @param successCallback Callback function invoked when the SHL QR code is successfully scanned.
   *   It provides an initialised [SHLinkScanData] object.
   * @param failCallback Callback function invoked when an error occurs during the scanning process.
   *   It provides an [Error] object with details about the failure.
   */
  fun scanSHLQRCode(
    successCallback: (SHLinkScanData) -> Unit,
    failCallback: (Error) -> Unit,
  )
}
