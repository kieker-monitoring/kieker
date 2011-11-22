</div>

<div id="Footer">

  <div id="PoweredBy">
    <a href="http://ibatis.apache.org"><img src="../images/poweredby.gif"/></a>
  </div>

  <div id="Banner">
    <logic:present name="accountBean" scope="session">
      <logic:equal name="accountBean" property="authenticated" value="true">
        <logic:equal name="accountBean" property="account.bannerOption" value="true">
          <bean:write filter="false" name="accountBean" property="account.bannerName"/>
        </logic:equal>
      </logic:equal>
    </logic:present>
  </div>

</div>

</body>
</html>