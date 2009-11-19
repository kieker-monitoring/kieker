package com.ibatis.jpetstore.presentation;

import com.ibatis.jpetstore.domain.Cart;
import com.ibatis.jpetstore.domain.CartItem;
import com.ibatis.jpetstore.domain.Item;
import com.ibatis.jpetstore.service.CatalogService;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.apache.struts.beanaction.ActionContext;

public class CartBeanTest extends MockObjectTestCase {

  public void testShouldSuccessfullyReturnFromViewCart() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    assertEquals(AbstractBean.SUCCESS, bean.viewCart());
  }

  public void testShouldSwitchPagesBackAndForth() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    assertEquals(AbstractBean.SUCCESS, bean.viewCart());

    Cart cart = new Cart();
    for (int i = 0; i < cart.getCartItemList().getPageSize() * 2; i++) {
      cart.getCartItemList().add(new Item());
    }
    bean.setCart(cart);
    bean.setPageDirection("next");
    assertEquals(AbstractBean.SUCCESS, bean.switchCartPage());
    assertEquals(1, cart.getCartItemList().getPageIndex());
    bean.setPageDirection("previous");
    assertEquals(AbstractBean.SUCCESS, bean.switchCartPage());
    assertEquals(0, cart.getCartItemList().getPageIndex());

  }

  public void testShouldClearAllCartData() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    Cart cart = new Cart();
    bean.setCart(cart);
    bean.setWorkingItemId("not null");
    bean.setPageDirection("not null");
    bean.clear();
    assertFalse(cart == bean.getCart());
    assertNull(bean.getWorkingItemId());
    assertNull(bean.getPageDirection());
  }

  public void testShouldAddItemToCart() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(atLeastOnce())
        .method("isItemInStock")
        .with(NOT_NULL)
        .will(returnValue(true));
    Item item = new Item();
    item.setItemId("AnID");
    catalogServiceMock.expects(atLeastOnce())
        .method("getItem")
        .with(NOT_NULL)
        .will(returnValue(item));
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    bean.setWorkingItemId("SomeItem");
    assertEquals(AbstractBean.SUCCESS, bean.addItemToCart());
    CartItem cartItem = (CartItem)bean.getCart().getCartItemList().get(0);
    assertEquals(1,cartItem.getQuantity());
    assertEquals(AbstractBean.SUCCESS, bean.addItemToCart());
    assertEquals(2,cartItem.getQuantity());
  }

  public void testShouldFailToRemoveItemFromCart() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    bean.setWorkingItemId("nonexistant");
    assertEquals(AbstractBean.FAILURE, bean.removeItemFromCart());
  }

  public void testShouldRemoveItemFromCart() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(atLeastOnce())
        .method("isItemInStock")
        .with(NOT_NULL)
        .will(returnValue(true));
    Item item = new Item();
    item.setItemId("AnID");
    catalogServiceMock.expects(atLeastOnce())
        .method("getItem")
        .with(NOT_NULL)
        .will(returnValue(item));
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    bean.setWorkingItemId("AnID");
    bean.addItemToCart();
    assertEquals(AbstractBean.SUCCESS, bean.removeItemFromCart());
  }

  public void testShouldUpdateCartQuantities() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(atLeastOnce())
        .method("isItemInStock")
        .with(NOT_NULL)
        .will(returnValue(true));
    Item item = new Item();
    item.setItemId("AnID");
    catalogServiceMock.expects(atLeastOnce())
        .method("getItem")
        .with(NOT_NULL)
        .will(returnValue(item));
    CartBean bean = new CartBean((CatalogService) catalogServiceMock.proxy());
    bean.setWorkingItemId("AnID");
    bean.addItemToCart();

    ActionContext.getActionContext().getParameterMap().put("AnID", "5");

    assertEquals(AbstractBean.SUCCESS, bean.updateCartQuantities());
    CartItem cartItem = (CartItem)bean.getCart().getCartItemList().get(0);
    assertEquals(5,cartItem.getQuantity());
  }


}
