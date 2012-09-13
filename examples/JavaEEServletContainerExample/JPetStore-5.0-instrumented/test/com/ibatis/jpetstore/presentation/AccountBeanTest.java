package com.ibatis.jpetstore.presentation;

import com.ibatis.common.util.PaginatedArrayList;
import com.ibatis.jpetstore.domain.Account;
import com.ibatis.jpetstore.domain.DomainFixture;
import com.ibatis.jpetstore.service.AccountService;
import com.ibatis.jpetstore.service.CatalogService;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import java.util.List;

public class AccountBeanTest extends MockObjectTestCase {

  public void testShouldSuccessfullyCallServicesToCreateNewAccount() {
    Account account = DomainFixture.newTestAccount();

    Mock accountServiceMock = mock(AccountService.class);

    accountServiceMock.expects(once())
        .method("insertAccount")
        .with(NOT_NULL);

    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL)
        .will(returnValue(account));

    Mock catalogServiceMock = mock(CatalogService.class);

    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));

    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);

    String result = accountBean.newAccount();
    assertEquals(AbstractBean.SUCCESS, result);
  }

  public void testShouldSuccessfullyCallServicesToUpdateExistingAccount() {
    Account account = DomainFixture.newTestAccount();

    Mock accountServiceMock = mock(AccountService.class);

    accountServiceMock.expects(once())
        .method("updateAccount")
        .with(NOT_NULL);

    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL)
        .will(returnValue(account));

    Mock catalogServiceMock = mock(CatalogService.class);

    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));

    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);

    String result = accountBean.editAccount();
    assertEquals(AbstractBean.SUCCESS, result);
  }

  public void testShouldCallEditAccountFormReturningSuccess() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL)
        .will(returnValue(account));
    Mock catalogServiceMock = mock(CatalogService.class);
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    assertEquals(AbstractBean.SUCCESS, accountBean.editAccountForm());
  }

  public void testShouldSwitchPageDirection() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    Mock catalogServiceMock = mock(CatalogService.class);
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    accountBean.setMyList(new PaginatedArrayList(5));
    accountBean.setPageDirection("next");
    assertEquals(AbstractBean.SUCCESS,accountBean.switchMyListPage());
    accountBean.setPageDirection("previous");
    assertEquals(AbstractBean.SUCCESS,accountBean.switchMyListPage());
  }

  public void testShouldSignoffAccount() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL,NOT_NULL)
        .will(returnValue(account));
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    accountBean.signon();

    assertEquals(AbstractBean.SUCCESS, accountBean.signoff());
    assertFalse(accountBean.isAuthenticated());
  }

  public void testShouldSignonAccount() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NOT_NULL,NOT_NULL)
        .will(returnValue(account));
    Mock catalogServiceMock = mock(CatalogService.class);
    catalogServiceMock.expects(once())
        .method("getProductListByCategory")
        .with(NOT_NULL)
        .will(returnValue(new PaginatedArrayList(5)));
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(account);
    assertEquals(AbstractBean.SUCCESS, accountBean.signon());
    assertTrue(accountBean.isAuthenticated());
  }

  public void testShouldFailToSignonAccount() {
    Account account = DomainFixture.newTestAccount();
    Mock accountServiceMock = mock(AccountService.class);
    accountServiceMock.expects(once())
        .method("getAccount")
        .with(NULL,NULL)
        .will(returnValue(null));
    Mock catalogServiceMock = mock(CatalogService.class);
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    assertEquals(AbstractBean.FAILURE, accountBean.signon());
    assertFalse(accountBean.isAuthenticated());
  }

  public void testShouldGetCategories() {
    AccountBean bean = new AccountBean();
    List categories = bean.getCategories();
    assertTrue(categories.contains("DOGS"));
    assertTrue(categories.contains("CATS"));
    assertTrue(categories.contains("BIRDS"));
    assertTrue(categories.contains("REPTILES"));
    assertTrue(categories.contains("FISH"));
  }

  public void testShouldGetLanguages() {
    AccountBean bean = new AccountBean();
    List langs = bean.getLanguages();
    assertTrue(langs.contains("english"));
    assertTrue(langs.contains("japanese"));
  }

  public void testShouldResetBooleanOptions() {
    AccountBean bean = new AccountBean();
    bean.setAccount(DomainFixture.newTestAccount());
    bean.getAccount().setBannerOption(true);
    bean.getAccount().setListOption(true);
    bean.reset();
    assertFalse(bean.getAccount().isBannerOption());
    assertFalse(bean.getAccount().isListOption());
  }

  public void testShouldClearAccountBean() {
    Mock accountServiceMock = mock(AccountService.class);
    Mock catalogServiceMock = mock(CatalogService.class);
    AccountBean accountBean = new AccountBean((AccountService)accountServiceMock.proxy(), (CatalogService)catalogServiceMock.proxy());
    accountBean.setAccount(DomainFixture.newTestAccount());
    accountBean.setRepeatedPassword("something");
    accountBean.setPageDirection("F");
    accountBean.setMyList(new PaginatedArrayList(5));

    accountBean.clear();

    assertEquals(null, accountBean.getAccount().getFirstName());
    assertEquals(null, accountBean.getRepeatedPassword());
    assertEquals(null, accountBean.getPageDirection());
    assertEquals(null, accountBean.getMyList());
  }

}
