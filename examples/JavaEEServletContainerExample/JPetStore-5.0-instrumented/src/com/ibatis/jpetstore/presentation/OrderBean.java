/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package com.ibatis.jpetstore.presentation;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.jpetstore.domain.Order;
import com.ibatis.jpetstore.service.AccountService;
import com.ibatis.jpetstore.service.OrderService;
import org.apache.struts.beanaction.ActionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class OrderBean extends AbstractBean {

  private static final List CARD_TYPE_LIST;

  private AccountService accountService;
  private OrderService orderService;

  private Order order;
  private int orderId;
  private boolean shippingAddressRequired;
  private boolean confirmed;
  private PaginatedList orderList;
  private String pageDirection;

  static {
    List cardList = new ArrayList();
    cardList.add("Visa");
    cardList.add("MasterCard");
    cardList.add("American Express");
    CARD_TYPE_LIST = Collections.unmodifiableList(cardList);
  }

  public OrderBean() {
    this(new AccountService(), new OrderService());
  }

  public OrderBean(AccountService accountService, OrderService orderService) {
    order = new Order();
    shippingAddressRequired = false;
    confirmed = false;
    this.accountService = accountService;
    this.orderService = orderService;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public boolean isShippingAddressRequired() {
    return shippingAddressRequired;
  }

  public void setShippingAddressRequired(boolean shippingAddressRequired) {
    this.shippingAddressRequired = shippingAddressRequired;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  public List getCreditCardTypes() {
    return CARD_TYPE_LIST;
  }

  public String getPageDirection() {
    return pageDirection;
  }

  public void setPageDirection(String pageDirection) {
    this.pageDirection = pageDirection;
  }

  public PaginatedList getOrderList() {
    return orderList;
  }

  @OperationExecutionMonitoringProbe
  public String listOrders() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = (AccountBean) sessionMap.get("accountBean");
    orderList = orderService.getOrdersByUsername(accountBean.getAccount().getUsername());
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String switchOrderPage() {
    if ("next".equals(pageDirection)) {
      orderList.nextPage();
    } else if ("previous".equals(pageDirection)) {
      orderList.previousPage();
    }
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String newOrderForm() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = (AccountBean) sessionMap.get("accountBean");
    CartBean cartBean = (CartBean) sessionMap.get("cartBean");

    clear();
    if (accountBean == null || !accountBean.isAuthenticated()){
      setMessage("You must sign on before attempting to check out.  Please sign on and try checking out again.");
      return SIGNON;
    } else if (cartBean != null) {
      order.initOrder(accountBean.getAccount(), cartBean.getCart());
      return SUCCESS;
    } else {
      setMessage("An order could not be created because a cart could not be found.");
      return FAILURE;
    }
  }

  @OperationExecutionMonitoringProbe
  public String newOrder() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();

    if (shippingAddressRequired) {
      shippingAddressRequired = false;
      return SHIPPING;
    } else if (!isConfirmed()) {
      return CONFIRM;
    } else if (getOrder() != null) {

      orderService.insertOrder(order);

      CartBean cartBean = (CartBean)sessionMap.get("cartBean");
      cartBean.clear();

      setMessage("Thank you, your order has been submitted.");

      return SUCCESS;
    } else {
      setMessage("An error occurred processing your order (order was null).");
      return FAILURE;
    }
  }

  @OperationExecutionMonitoringProbe
  public String viewOrder() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = (AccountBean) sessionMap.get("accountBean");

    order = orderService.getOrder(orderId);

    if (accountBean.getAccount().getUsername().equals(order.getUsername())) {
      return SUCCESS;
    } else {
      order = null;
      setMessage("You may only view your own orders.");
      return FAILURE;
    }
  }

  public void reset() {
    shippingAddressRequired = false;
  }

  public void clear() {
    order = new Order();
    orderId = 0;
    shippingAddressRequired = false;
    confirmed = false;
    orderList = null;
    pageDirection = null;
  }

}
