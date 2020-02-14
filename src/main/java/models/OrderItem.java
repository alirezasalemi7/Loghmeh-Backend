package models;

public class OrderItem {

    private Food _food;
    private int _count;

    OrderItem(Food food,int count){
        this._count = count;
        this._food = food;
    }

    public int getCount(){return _count;}

    public String getFoodName(){return _food.getName();}

    public Food getFood() { return _food;}

    public void setCount(int count){
        _count = count;
    }
}
