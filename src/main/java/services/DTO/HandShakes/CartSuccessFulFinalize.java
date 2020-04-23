package services.DTO.HandShakes;

import services.DTO.Order.OrderDetailDTO;

public class CartSuccessFulFinalize {

    private int status;
    private OrderDetailDTO order;

    public CartSuccessFulFinalize(){}

    public CartSuccessFulFinalize(int status,OrderDetailDTO detailDTO){
        this.status = status;
        this.order = detailDTO;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderDetailDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDetailDTO order) {
        this.order = order;
    }
}
