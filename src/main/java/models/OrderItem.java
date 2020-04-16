package models;

public class OrderItem {

    private Food _food;
    private int _count;
    private String _parentId;

    OrderItem(Food food,int count,String id){
        this._count = count;
        this._food = food;
        this._parentId = id;
    }

    public String getParentId(){
        return _parentId;
    }



    public int getCount(){return _count;}

    public String getRestaurantId(){
        return _food.getRestaurantId();
    }

    public String getFoodName(){return _food.getName();}

    public Food getFood() { return _food;}

    public void setCount(int count){
        _count = count;
    }

    public double getPrice(){
        return _count*_food.getPrice();
    }
}
