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
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

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

  @OperationExecutionMonitoringProbe
  public List getCategoryList() {
    return categoryDao.getCategoryList();
  }

  @OperationExecutionMonitoringProbe
  public Category getCategory(String categoryId) {
    return categoryDao.getCategory(categoryId);
  }

  @OperationExecutionMonitoringProbe
  public Product getProduct(String productId) {
    return productDao.getProduct(productId);
  }

  @OperationExecutionMonitoringProbe
  public PaginatedList getProductListByCategory(String categoryId) {
    return productDao.getProductListByCategory(categoryId);
  }

  @OperationExecutionMonitoringProbe
  public PaginatedList searchProductList(String keywords) {
    return productDao.searchProductList(keywords);
  }

  @OperationExecutionMonitoringProbe
  public PaginatedList getItemListByProduct(String productId) {
    return itemDao.getItemListByProduct(productId);
  }

  @OperationExecutionMonitoringProbe
  public Item getItem(String itemId) {
    return itemDao.getItem(itemId);
  }

  @OperationExecutionMonitoringProbe
  public boolean isItemInStock(String itemId) {
    return itemDao.isItemInStock(itemId);
  }

}