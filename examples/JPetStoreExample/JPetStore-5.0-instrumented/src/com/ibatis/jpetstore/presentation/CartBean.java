package com.ibatis.jpetstore.presentation;

import com.ibatis.jpetstore.domain.Cart;
import com.ibatis.jpetstore.domain.CartItem;
import com.ibatis.jpetstore.domain.Item;
import com.ibatis.jpetstore.service.CatalogService;
import org.apache.struts.beanaction.ActionContext;

import java.util.Iterator;
import java.util.Map;
import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;

public class CartBean extends AbstractBean {

  private CatalogService catalogService;

  private Cart cart = new Cart();
  private String workingItemId;
  private String pageDirection;

  public CartBean() {
    this (new CatalogService());
  }

  public CartBean(CatalogService catalogService) {
    this.catalogService= catalogService;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public String getWorkingItemId() {
    return workingItemId;
  }

  public void setWorkingItemId(String workingItemId) {
    this.workingItemId = workingItemId;
  }

  public String getPageDirection() {
    return pageDirection;
  }

  public void setPageDirection(String pageDirection) {
    this.pageDirection = pageDirection;
  }

  @TpmonExecutionMonitoringProbe
  public String addItemToCart() {
    if (cart.containsItemId(workingItemId)) {
      cart.incrementQuantityByItemId(workingItemId);
    } else {
      // isInStock is a "real-time" property that must be updated
      // every time an item is added to the cart, even if other
      // item details are cached.
      boolean isInStock = catalogService.isItemInStock(workingItemId);
      Item item = catalogService.getItem(workingItemId);
      cart.addItem(item, isInStock);
    }

    return SUCCESS;
  }

  @TpmonExecutionMonitoringProbe
  public String removeItemFromCart() {

    Item item = cart.removeItemById(workingItemId);

    if (item == null) {
      setMessage("Attempted to remove null CartItem from Cart.");
      return FAILURE;
    } else {
      return SUCCESS;
    }
  }

  @TpmonExecutionMonitoringProbe
  public String updateCartQuantities() {
    Map parameterMap = ActionContext.getActionContext().getParameterMap();

    Iterator cartItems = getCart().getAllCartItems();
    while (cartItems.hasNext()) {
      CartItem cartItem = (CartItem) cartItems.next();
      String itemId = cartItem.getItem().getItemId();
      try {
        int quantity = Integer.parseInt((String) parameterMap.get(itemId));
        getCart().setQuantityByItemId(itemId, quantity);
        if (quantity < 1) {
          cartItems.remove();
        }
      } catch (Exception e) {
        //ignore parse exceptions on purpose
      }
    }

    return SUCCESS;
  }

  @TpmonExecutionMonitoringProbe
  public String switchCartPage() {
    if ("next".equals(pageDirection)) {
      cart.getCartItemList().nextPage();
    } else if ("previous".equals(pageDirection)) {
      cart.getCartItemList().previousPage();
    }
    return SUCCESS;
  }

  @TpmonExecutionMonitoringProbe
  public String viewCart() {
    return SUCCESS;
  }

  public void clear() {
    cart = new Cart();
    workingItemId = null;
    pageDirection = null;
  }

}
