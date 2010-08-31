package com.ibatis.jpetstore.service;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.jpetstore.domain.LineItem;
import com.ibatis.jpetstore.domain.Order;
import com.ibatis.jpetstore.persistence.DaoConfig;
import com.ibatis.jpetstore.persistence.iface.ItemDao;
import com.ibatis.jpetstore.persistence.iface.OrderDao;
import com.ibatis.jpetstore.persistence.iface.SequenceDao;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class OrderService {

  private DaoManager daoManager;

  private ItemDao itemDao;
  private OrderDao orderDao;
  private SequenceDao sequenceDao;

  public OrderService() {
    daoManager = DaoConfig.getDaoManager();
    itemDao = (ItemDao) daoManager.getDao(ItemDao.class);
    sequenceDao = (SequenceDao) daoManager.getDao(SequenceDao.class);
    orderDao = (OrderDao) daoManager.getDao(OrderDao.class);
  }

  public OrderService(DaoManager daoManager, ItemDao itemDao, OrderDao orderDao, SequenceDao sequenceDao) {
    this.daoManager = daoManager;
    this.itemDao = itemDao;
    this.orderDao = orderDao;
    this.sequenceDao = sequenceDao;
  }

  @OperationExecutionMonitoringProbe
  public void insertOrder(Order order) {
    try {
      // Get the next id within a separate transaction
      order.setOrderId(getNextId("ordernum"));

      daoManager.startTransaction();

      itemDao.updateAllQuantitiesFromOrder(order);
      orderDao.insertOrder(order);

      daoManager.commitTransaction();
    } finally {
      daoManager.endTransaction();
    }
  }

  @OperationExecutionMonitoringProbe
  public Order getOrder(int orderId) {
    Order order = null;

    try {
      daoManager.startTransaction();

      order = orderDao.getOrder(orderId);

      for (int i = 0; i < order.getLineItems().size(); i++) {
        LineItem lineItem = (LineItem) order.getLineItems().get(i);
        lineItem.setItem(itemDao.getItem(lineItem.getItemId()));
      }

      daoManager.commitTransaction();
    } finally {
      daoManager.endTransaction();
    }

    return order;
  }

  @OperationExecutionMonitoringProbe
  public PaginatedList getOrdersByUsername(String username) {
    return orderDao.getOrdersByUsername(username);
  }

  public int getNextId(String key) {
    return sequenceDao.getNextId(key);
  }


}
