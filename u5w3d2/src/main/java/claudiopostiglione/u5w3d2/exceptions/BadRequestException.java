package claudiopostiglione.u5w3d2.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super((message));
    }
}
