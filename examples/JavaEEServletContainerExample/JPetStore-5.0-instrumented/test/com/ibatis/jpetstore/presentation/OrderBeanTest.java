package com.ibatis.jpetstore.presentation;

import com.ibatis.common.util.PaginatedArrayList;
import com.ibatis.jpetstore.service.AccountService;
import com.ibatis.jpetstore.service.OrderService;
import com.ibatis.jpetstore.service.CatalogService;
import com.ibatis.jpetstore.domain.Order;
import com.ibatis.jpetstore.domain.Account;
import com.ibatis.jpetstore.domain.DomainFixture;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.apache.struts.beanaction.ActionContext;

import java.util.List;
import java.util.Map;

public class OrderBeanTest extends MockObjectTestCase {

  public void testShouldGetCardTypes() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    List cardList = bean.getCreditCardTypes();
    assertEquals(3, cardList.size());
  }

  public void testListListOrdersByUsername() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    PaginatedArrayList orderList = new PaginatedArrayList(5);
    orderList.add(new Order());
    orderList.add(new Order());
    orderList.add(new Order());
    orderServiceMock.expects(once())
        .method("getOrdersByUsername")
        .with(NOT_NULL)
        .will(returnValue(orderList));
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = new AccountBean();
    accountBean.setUsername("not null");
    sessionMap.put("accountBean", accountBean);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    assertEquals(AbstractBean.SUCCESS, bean.listOrders());
    assertEquals(3, bean.getOrderList().size());
  }

  public void testShouldSwitchOrderListPagesBackAndForth() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    PaginatedArrayList orderList = new PaginatedArrayList(2);
    orderList.add(new Order());
    orderList.add(new Order());
    orderList.add(new Order());
    orderServiceMock.expects(once())
        .method("getOrdersByUsername")
        .with(NOT_NULL)
        .will(returnValue(orderList));
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = new AccountBean();
    accountBean.setUsername("not null");
    sessionMap.put("accountBean", accountBean);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.listOrders();
    bean.setPageDirection("next");
    bean.switchOrderPage();
    assertEquals(1, bean.getOrderList().getPageIndex());
    bean.setPageDirection("previous");
    bean.switchOrderPage();
    assertEquals(0, bean.getOrderList().getPageIndex());
  }

  public void testShouldResetShippingAddressRequirement() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.setShippingAddressRequired(true);
    bean.reset();
    assertFalse(bean.isShippingAddressRequired());
  }

  public void testShouldClearAllFields() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    Order order = new Order();
    bean.setOrder(order);
    bean.setShippingAddressRequired(true);
    bean.setOrderId(4);
    bean.setConfirmed(true);
    bean.setPageDirection("not null");
    bean.clear();
    assertFalse(bean.getOrder() == order);
    assertFalse(bean.isShippingAddressRequired());
    assertEquals(0, bean.getOrderId());
    assertFalse(bean.isConfirmed());
    assertNull(bean.getPageDirection());
  }

  public void testShouldSuccessfullyViewOrder() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = new AccountBean();
    accountBean.setUsername("user");
    sessionMap.put("accountBean", accountBean);

    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    Order order = new Order();
    order.setUsername("user");
    orderServiceMock.expects(once())
        .method("getOrder")
        .with(NOT_NULL)
        .will(returnValue(order));
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());

    assertEquals(AbstractBean.SUCCESS, bean.viewOrder());
    assertEquals(order, bean.getOrder());
  }

  public void testShouldFailToViewOrderDueToMismatchedUsername() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    AccountBean accountBean = new AccountBean();
    accountBean.setUsername("not proper user");
    sessionMap.put("accountBean", accountBean);

    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    Order order = new Order();
    order.setUsername("user");
    orderServiceMock.expects(once())
        .method("getOrder")
        .with(NOT_NULL)
        .will(returnValue(order));
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());

    assertEquals(AbstractBean.FAILURE, bean.viewOrder());
    assertNull(bean.getOrder());
  }

  public void testShouldForceSignonWhenAttemptingToCreateANewOrderWithoutBeingSignedIn () {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    assertEquals(AbstractBean.SIGNON, bean.newOrderForm());
  }

  public void testShouldFailDueToAMissingCart () {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL,NOT_NULL)
        .will(returnValue(account));
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    accountBean.signon();

    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    sessionMap.put("accountBean", accountBean);

    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    assertEquals(AbstractBean.FAILURE, bean.newOrderForm());
  }

  public void testSuccessfullyViewCart() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL,NOT_NULL)
        .will(returnValue(account));
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    accountBean.signon();

    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    sessionMap.put("accountBean", accountBean);
    sessionMap.put("cartBean", new CartBean());

    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());

    assertEquals(AbstractBean.SUCCESS, bean.newOrderForm());
  }

  public void testShouldRequireShippingAddressBeforeNewOrder() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.setShippingAddressRequired(true);
    assertEquals(AbstractBean.SHIPPING,bean.newOrder());
  }

  public void testShouldConfirmationBeforeNewOrder() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.setConfirmed(false);
    assertEquals(AbstractBean.CONFIRM,bean.newOrder());
  }

  public void testShouldFaileDueToMissingNewOrder() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.setConfirmed(true);
    bean.setOrder(null);
    assertEquals(AbstractBean.FAILURE,bean.newOrder());
  }

  public void testShouldSuccessfullyCreateNewOrder() {
    Map sessionMap = ActionContext.getActionContext().getSessionMap();
    sessionMap.put("cartBean", new CartBean());

    Mock accountServiceMock = mock(AccountService.class);
    Mock orderServiceMock = mock(OrderService.class);
    orderServiceMock.expects(once())
        .method("insertOrder")
        .with(NOT_NULL);
    OrderBean bean = new OrderBean((AccountService)accountServiceMock.proxy(), (OrderService)orderServiceMock.proxy());
    bean.setConfirmed(true);
    assertEquals(AbstractBean.SUCCESS,bean.newOrder());
  }

}
