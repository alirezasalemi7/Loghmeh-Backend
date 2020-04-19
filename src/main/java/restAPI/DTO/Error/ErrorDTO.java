package restAPI.DTO.Error;

public class ErrorDTO {

    String description;
    int status;

    ErrorDTO(){}

    public ErrorDTO(String description,int status){
        this.status = status;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
