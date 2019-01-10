A Fálj feltöltést kezelő abstract osztály dokumentációja:

/**
 * Handler class which performs various methods during a file upload process.
 * Contains default behavior. Implementations are free to redefine the methods or
 * to keep them as they want. Note that using a concrete implementation is required,
 * if no suitable handler is found then the file upload is considered a failure.
 *
 * <p>Each method receives an UploadContext object which contains the information
 * about the file upload process. Its values are expected to change between the
 * method calls or even replaced with a new object, so make sure you carefully check
 * its state.
 * </p>
 */
public abstract class UploadHandler
{

    /**
     * Method which is called at the beginning of the upload process, after enough
     * bytes have been read to determine file metadata, if available.
     *
     * @param context Metadata holder
     */
    public void onStart(UploadContext context){}

    /**
     * Method which is called after the uploaded file has been processed without
     * errors. Note that this method can still fail the process, meaning that
     * even though the file is saved on the server the client side can still
     * receive a failure message.
     * @param context Metadata holder
     */
    public void onDone(UploadContext context) {}

    /**
     * Method which is called after an error occurred during the upload process.
     * That error can come from the upload service or from the onStart / onDone
     * method of this handler.
     *
     * <p>The default implementation handles the common errors.</p>
     *
     * @param context Metadata holder
     * @param error Upload related exception, thrown in the "critical block" of the service
     */
    public void onFail(UploadContext context, Throwable error) {}
}