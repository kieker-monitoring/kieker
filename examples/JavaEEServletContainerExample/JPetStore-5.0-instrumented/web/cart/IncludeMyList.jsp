<bean:define id="myList" name="accountBean" property="myList"/>

<logic:present name="myList">
  <p>
    Pet Favorites
    <br/>
    Shop for more of your favorite pets here.
  </p>
  <ul>
    <logic:iterate id="product" name="myList">
      <li><html:link paramId="productId" paramName="product" paramProperty="productId" page="/shop/viewProduct.shtml">
        <bean:write name="product" property="name"/></html:link>
      (<bean:write name="product" property="productId"/>)</li>
    </logic:iterate>
  </ul>

  <p>
    <logic:notEqual name="myList" property="firstPage" value="true">
      <a href="switchMyListPage.shtml?pageDirection=previous&account.listOption=<bean:write name="accountBean"
          property="account.listOption"/>&account.bannerOption=< bean:write name="accountBean"
                                                                 property="account.bannerOption"/>">&lt;&lt;Prev</a>
    </logic:notEqual>
    <logic:notEqual name="myList" property="lastPage" value="true">
      <a href="switchMyListPage.shtml?pageDirection=next&account.listOption=<bean:write name="accountBean"
          property="account.listOption"/>&account.bannerOption=< bean:write name="accountBean"
                                                                 property="account.bannerOption"/>">Next &gt;&gt;</a>
    </logic:notEqual>
  </p>

</logic:present>




