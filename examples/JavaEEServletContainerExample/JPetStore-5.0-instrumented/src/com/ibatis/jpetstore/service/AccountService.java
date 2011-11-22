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

import com.ibatis.dao.client.DaoManager;
import com.ibatis.jpetstore.domain.Account;
import com.ibatis.jpetstore.persistence.iface.AccountDao;
import com.ibatis.jpetstore.persistence.DaoConfig;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class AccountService {

  private AccountDao accountDao;

  public AccountService() {
    DaoManager daoMgr = DaoConfig.getDaoManager();
    this.accountDao = (AccountDao) daoMgr.getDao(AccountDao.class);
  }

  public AccountService(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  @OperationExecutionMonitoringProbe
  public Account getAccount(String username) {
    return accountDao.getAccount(username);
  }

  @OperationExecutionMonitoringProbe
  public Account getAccount(String username, String password) {
    return accountDao.getAccount(username, password);
  }

  @OperationExecutionMonitoringProbe
  public void insertAccount(Account account) {
    accountDao.insertAccount(account);
  }

  @OperationExecutionMonitoringProbe
  public void updateAccount(Account account) {
    accountDao.updateAccount(account);
  }

}
