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

import com.ibatis.common.util.PaginatedArrayList;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.jpetstore.domain.Category;
import com.ibatis.jpetstore.domain.Item;
import com.ibatis.jpetstore.domain.Product;
import com.ibatis.jpetstore.service.CatalogService;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class CatalogBeanTest extends MockObjectTestCase {

  public void testShouldPopulateCategoryByIdForViewing() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getCategory")
        .with(NOT_NULL)
        .will(returnValue(new Category()));
    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(4)));
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setCategoryId("DOGS");
    assertEquals(AbstractBean.SUCCESS, bean.viewCategory());
    assertNotNull(bean.getCategory());
    assertNotNull(bean.getProductList());
  }

  public void testShouldPopulateProductByIdForViewing() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getProduct")
        .with(NOT_NULL)
        .will(returnValue(new Product()));
    catalogServiceMock.expects(once())
        .method("getItemListByProduct")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(4)));
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setProductId("EST-1");
    assertEquals(AbstractBean.SUCCESS, bean.viewProduct());
    assertNotNull(bean.getProduct());
    assertNotNull(bean.getItemList());
  }

  public void testShouldPopulateItemByIdForViewing() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getItem")
        .with(NOT_NULL)
        .will(returnValue(new Item()));
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setItemId("FS-SW-1");
    assertEquals(AbstractBean.SUCCESS, bean.viewItem());
    assertNotNull(bean.getItem());
  }

  public void testShouldFailToSearchProducts() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setKeyword(null);
    assertEquals(AbstractBean.FAILURE, bean.searchProducts());
    assertNull(bean.getProductList());
  }

  public void testShouldSearchProductsByKeyword() {
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("searchProductList")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(4)));
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setKeyword("dog");
    assertEquals(AbstractBean.SUCCESS, bean.searchProducts());
    assertNotNull(bean.getProductList());
  }

  public void testShouldSwitchProductPageBackAndForth() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    PaginatedList productList = new PaginatedArrayList(2);
    productList.add(new Product());
    productList.add(new Product());
    productList.add(new Product());
    productList.add(new Product());
    productList.add(new Product());
    bean.setProductList(productList);

    bean.setPageDirection("next");
    bean.switchProductListPage();
    assertEquals(1, productList.getPageIndex());
    bean.setPageDirection("previous");
    bean.switchProductListPage();
    assertEquals(0, productList.getPageIndex());
  }

  public void testShouldSwitchItemPageBackAndForth() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    PaginatedList itemList = new PaginatedArrayList(2);
    itemList.add(new Item());
    itemList.add(new Item());
    itemList.add(new Item());
    itemList.add(new Item());
    itemList.add(new Item());
    bean.setItemList(itemList);

    bean.setPageDirection("next");
    assertEquals(AbstractBean.SUCCESS, bean.switchItemListPage());
    assertEquals(1, itemList.getPageIndex());
    bean.setPageDirection("previous");
    assertEquals(AbstractBean.SUCCESS, bean.switchItemListPage());
    assertEquals(0, itemList.getPageIndex());
  }

  public void testShouldClearCatalogBean() {
    Mock catalogServiceMock = mock(CatalogService.class);
    CatalogBean bean = new CatalogBean((CatalogService) catalogServiceMock.proxy());
    bean.setKeyword("not null");
    bean.setPageDirection("not null");
    bean.setCategoryId("not null");
    bean.setProductId("not null");
    bean.setItemId("not null");
    bean.setCategory(new Category());
    bean.setProduct(new Product());
    bean.setItem(new Item());
    bean.setCategoryList(new PaginatedArrayList(2));
    bean.setProductList(new PaginatedArrayList(2));
    bean.setItemList(new PaginatedArrayList(2));
    bean.clear();
    assertNull(bean.getKeyword());
    assertNull(bean.getPageDirection());
    assertNull(bean.getCategoryId());
    assertNull(bean.getCategory());
    assertNull(bean.getCategoryList());
    assertNull(bean.getProductId());
    assertNull(bean.getProduct());
    assertNull(bean.getProductList());
    assertNull(bean.getItemId());
    assertNull(bean.getItem());
    assertNull(bean.getItemList());
  }


}
