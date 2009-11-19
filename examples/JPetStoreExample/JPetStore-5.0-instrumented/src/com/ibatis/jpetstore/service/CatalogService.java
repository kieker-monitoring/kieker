package com.ibatis.jpetstore.service;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.jpetstore.domain.Category;
import com.ibatis.jpetstore.domain.Item;
import com.ibatis.jpetstore.domain.Product;
import com.ibatis.jpetstore.persistence.iface.CategoryDao;
import com.ibatis.jpetstore.persistence.iface.ItemDao;
import com.ibatis.jpetstore.persistence.iface.ProductDao;
import com.ibatis.jpetstore.persistence.DaoConfig;

import java.util.List;
import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;

public class CatalogService {

  private CategoryDao categoryDao;
  private ItemDao itemDao;
  private ProductDao productDao;

  public CatalogService() {
    DaoManager daoManager = DaoConfig.getDaoManager();
    categoryDao = (CategoryDao) daoManager.getDao(CategoryDao.class);
    productDao = (ProductDao) daoManager.getDao(ProductDao.class);
    itemDao = (ItemDao) daoManager.getDao(ItemDao.class);
  }

  public CatalogService(CategoryDao categoryDao, ItemDao itemDao, ProductDao productDao) {
    this.categoryDao = categoryDao;
    this.itemDao = itemDao;
    this.productDao = productDao;
  }

  @TpmonExecutionMonitoringProbe
  public List getCategoryList() {
    return categoryDao.getCategoryList();
  }

  @TpmonExecutionMonitoringProbe
  public Category getCategory(String categoryId) {
    return categoryDao.getCategory(categoryId);
  }

  @TpmonExecutionMonitoringProbe
  public Product getProduct(String productId) {
    return productDao.getProduct(productId);
  }

  @TpmonExecutionMonitoringProbe
  public PaginatedList getProductListByCategory(String categoryId) {
    return productDao.getProductListByCategory(categoryId);
  }

  @TpmonExecutionMonitoringProbe
  public PaginatedList searchProductList(String keywords) {
    return productDao.searchProductList(keywords);
  }

  @TpmonExecutionMonitoringProbe
  public PaginatedList getItemListByProduct(String productId) {
    return itemDao.getItemListByProduct(productId);
  }

  @TpmonExecutionMonitoringProbe
  public Item getItem(String itemId) {
    return itemDao.getItem(itemId);
  }

  @TpmonExecutionMonitoringProbe
  public boolean isItemInStock(String itemId) {
    return itemDao.isItemInStock(itemId);
  }

}