package servlets.customTags;

import models.OrderItem;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class OrderItemJSPTag extends SimpleTagSupport {

    StringWriter sw = new StringWriter();
    private OrderItem item;

    @Override
    public void doTag() throws JspException, IOException {
        if(item!=null){
            JspWriter out = getJspContext().getOut();
            out.println("<li>" + item.getFoodName() + " : " + item.getCount() + " : " + item.getPrice() + "</li>");
        }
    }

    public void setItem(OrderItem item) {
        this.item = item;
    }
}
