package sk.zuzmat.classified.common;

/**
 * Created by zuzka on 29.03.2016.
 */
public class ValidationException extends RuntimeException{

    public ValidationException() {
    }


    public ValidationException(String msg) {
        super(msg);
    }

}
