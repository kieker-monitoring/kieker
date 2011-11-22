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
import com.ibatis.jpetstore.domain.Category;
import com.ibatis.jpetstore.domain.Item;
import com.ibatis.jpetstore.domain.Product;
import com.ibatis.jpetstore.service.CatalogService;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class CatalogBean extends AbstractBean {

  private CatalogService catalogService;

  private String keyword;
  private String pageDirection;

  private String categoryId;
  private Category category;
  private PaginatedList categoryList;

  private String productId;
  private Product product;
  private PaginatedList productList;

  private String itemId;
  private Item item;
  private PaginatedList itemList;

  public CatalogBean() {
    this(new CatalogService());
  }

  public CatalogBean(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getPageDirection() {
    return pageDirection;
  }

  public void setPageDirection(String pageDirection) {
    this.pageDirection = pageDirection;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public PaginatedList getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(PaginatedList categoryList) {
    this.categoryList = categoryList;
  }

  public PaginatedList getProductList() {
    return productList;
  }

  public void setProductList(PaginatedList productList) {
    this.productList = productList;
  }

  public PaginatedList getItemList() {
    return itemList;
  }

  public void setItemList(PaginatedList itemList) {
    this.itemList = itemList;
  }

  @OperationExecutionMonitoringProbe
  public String viewCategory() {
    if (categoryId != null) {
      productList = catalogService.getProductListByCategory(categoryId);
      category = catalogService.getCategory(categoryId);
    }
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String viewProduct() {
    if (productId != null) {
      itemList = catalogService.getItemListByProduct(productId);
      product = catalogService.getProduct(productId);
    }
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String viewItem() {
    item = catalogService.getItem(itemId);
    product = item.getProduct();
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String searchProducts() {
    if (keyword == null || keyword.length() < 1) {
      setMessage("Please enter a keyword to search for, then press the search button.");
      return FAILURE;
    } else {
      productList = catalogService.searchProductList(keyword.toLowerCase());
      return SUCCESS;
    }
  }

  @OperationExecutionMonitoringProbe
  public String switchProductListPage() {
    if ("next".equals(pageDirection)) {
      productList.nextPage();
    } else if ("previous".equals(pageDirection)) {
      productList.previousPage();
    }
    return SUCCESS;
  }

  @OperationExecutionMonitoringProbe
  public String switchItemListPage() {
    if ("next".equals(pageDirection)) {
      itemList.nextPage();
    } else if ("previous".equals(pageDirection)) {
      itemList.previousPage();
    }
    return SUCCESS;
  }

  public void clear () {
    keyword = null;
    pageDirection = null;

    categoryId = null;
    category = null;
    categoryList = null;

    productId = null;
    product = null;
    productList = null;

    itemId = null;
    item = null;
    itemList = null;
  }

}
