package structures;

public class OrderItem {

    private String _foodName;
    private int _count;

    OrderItem(String foodName,int count){
        this._count = count;
        this._foodName = foodName;
    }

    public int getCount(){return _count;}

    public String getFoodName(){return _foodName;}

    public void setCount(int count){
        _count = count;
    }
}
