package feddit.model;

/**
 * this class has the task of displaying messages
 * resulting from the actions performed by the user.
 * These messages can be successful if the action
 * went smoothly, or if not, they will be error messages
 *
 * @author Groupe A
 *
 * */
public class ResultObject {
    
    private String code;
    private String message;
    private String type;

    public ResultObject() {}

    public ResultObject(String id, String type, String message) {
        this.code = id;
        this.message = message;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
