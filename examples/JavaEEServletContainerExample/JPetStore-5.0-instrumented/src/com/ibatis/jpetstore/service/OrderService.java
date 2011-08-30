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
