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

import com.ibatis.jpetstore.domain.Account;
import com.ibatis.jpetstore.domain.DomainFixture;
import com.ibatis.jpetstore.persistence.iface.AccountDao;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class AccountServiceTest extends MockObjectTestCase {

  public void testShouldVerifyGetAccountIsCalledByUsername() {
    Mock mock = mock(AccountDao.class);

    mock.expects(once())
        .method("getAccount")
        .with(NOT_NULL)
        .will(returnValue(new Account()));

    AccountService accountService = new AccountService((AccountDao) mock.proxy());
    accountService.getAccount("cbegin");
  }

  public void testShouldVerifyGetAccountIsCalledByUsernameAndPassword() {
    Mock mock = mock(AccountDao.class);

    mock.expects(once())
        .method("getAccount")
        .with(NOT_NULL, NOT_NULL)
        .will(returnValue(new Account()));

    AccountService accountService = new AccountService((AccountDao) mock.proxy());
    accountService.getAccount("cbegin","PASSWORD");
  }

  public void testShouldVerifyInsertAccountIsCalled() {
    Mock mock = mock(AccountDao.class);

    mock.expects(once())
        .method("insertAccount")
        .with(NOT_NULL);

    AccountService accountService = new AccountService((AccountDao) mock.proxy());
    accountService.insertAccount(DomainFixture.newTestAccount());
  }

  public void testShouldVerifyUpdateAccountIsCalled() {
    Mock mock = mock(AccountDao.class);

    mock.expects(once())
        .method("updateAccount")
        .with(NOT_NULL);

    AccountService accountService = new AccountService((AccountDao) mock.proxy());
    accountService.updateAccount(DomainFixture.newTestAccount());
  }

}
