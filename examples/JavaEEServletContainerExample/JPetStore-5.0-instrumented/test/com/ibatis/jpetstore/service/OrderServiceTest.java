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

import com.ibatis.common.util.PaginatedArrayList;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.jpetstore.domain.DomainFixture;
import com.ibatis.jpetstore.domain.Order;
import com.ibatis.jpetstore.persistence.iface.ItemDao;
import com.ibatis.jpetstore.persistence.iface.OrderDao;
import com.ibatis.jpetstore.persistence.iface.SequenceDao;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class OrderServiceTest extends MockObjectTestCase {

  public void testShouldCallGetOrderOnOrderDao() {

    Mock orderDao = mock(OrderDao.class);
    orderDao.expects(once())
        .method("getOrder")
        .with(NOT_NULL)
        .will(returnValue(new Order()));

    Mock daoManager = mock(DaoManager.class);
    daoManager.expects(once())
        .method("startTransaction")
        .withNoArguments();

    daoManager.expects(once())
        .method("commitTransaction")
        .withNoArguments();

    daoManager.expects(once())
        .method("endTransaction")
        .withNoArguments();

    OrderService service = new OrderService((DaoManager)daoManager.proxy(), null, (OrderDao) orderDao.proxy(), null);
    service.getOrder(1);

  }

  public void testShouldCallGetOrdersByUsernameOnOrderDao() {
    Mock orderDao = mock(OrderDao.class);
    orderDao.expects(once())
        .method("getOrdersByUsername")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));
    OrderService service = new OrderService(null, null, (OrderDao) orderDao.proxy(), null);
    service.getOrdersByUsername("j2ee");
  }

  public void testShouldCallInsertOrderOnOrderDao() {

    Mock seqDao = mock(SequenceDao.class);
    seqDao.expects(once())
        .method("getNextId")
        .with(NOT_NULL)
        .will(returnValue(1));

    Mock orderDao = mock(OrderDao.class);
    orderDao.expects(once())
        .method("insertOrder")
        .with(NOT_NULL);

    Mock itemDao = mock(ItemDao.class);
    itemDao.expects(once())
        .method("updateAllQuantitiesFromOrder")
        .with(NOT_NULL);

    Mock daoManager = mock(DaoManager.class);
    daoManager.expects(once())
        .method("startTransaction")
        .withNoArguments();

    daoManager.expects(once())
        .method("commitTransaction")
        .withNoArguments();

    daoManager.expects(once())
        .method("endTransaction")
        .withNoArguments();

    OrderService service = new OrderService((DaoManager)daoManager.proxy(), (ItemDao) itemDao.proxy(), (OrderDao) orderDao.proxy(), (SequenceDao) seqDao.proxy());
    service.insertOrder(DomainFixture.newTestOrder());

  }

  public void testShouldCallGetNextIdOnSequenceDao() {

    Mock seqDao = mock(SequenceDao.class);
    seqDao.expects(once())
        .method("getNextId")
        .with(NOT_NULL)
        .will(returnValue(1));

    OrderService service = new OrderService(null, null, null, (SequenceDao) seqDao.proxy());
    service.getNextId("ordernum");

  }

}
