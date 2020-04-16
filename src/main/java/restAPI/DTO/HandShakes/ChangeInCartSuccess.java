package restAPI.DTO.HandShakes;

public class ChangeInCartSuccess {

    private int status;
    private String food;
    private int count;

    public ChangeInCartSuccess(){}

    public ChangeInCartSuccess(int status,String foodName,int count){
        this.status = status;
        this.food = foodName;
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
