package com.ibatis.jpetstore.domain;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.jpetstore.presentation.AccountBean;
import com.ibatis.jpetstore.presentation.CartBean;
import com.ibatis.jpetstore.presentation.CatalogBean;
import com.ibatis.jpetstore.presentation.OrderBean;
import junit.framework.TestCase;

import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BeanTest extends TestCase {

  private Class[] classes = new Class[] {
      Account.class,
      Cart.class,
      CartItem.class,
      Category.class,
      Item.class,
      LineItem.class,
      Order.class,
      Product.class,
      Sequence.class,
      AccountBean.class,
      CartBean.class,
      CatalogBean.class,
      OrderBean.class
  };

  public void testAllReadWriteProperties () {
    try {
      for (int i=0; i < classes.length; i++) {
        Object object = classes[i].newInstance();
        ClassInfo info = ClassInfo.getInstance(classes[i]);
        List writeables = Arrays.asList(info.getWriteablePropertyNames());
        List readables = Arrays.asList(info.getReadablePropertyNames());
        for (int j=0; j < writeables.size(); j++) {
          String writeable = (String)writeables.get(j);
          if (readables.contains(writeable)) {
            Class type = info.getGetterType(writeable);
            Object sample = getSampleFor(type);
            Probe probe = ProbeFactory.getProbe(object);
            probe.setObject(object, writeable, sample);
            assertEquals(sample,probe.getObject(object, writeable));
          }
        }
      }
    } catch(Exception e) {
      throw new RuntimeException("Error. ", e);
    }
  }

  public Object getSampleFor(Class type) throws Exception {
    Map sampleMap = new HashMap();
    sampleMap.put(String.class, "Hello");
    sampleMap.put(Integer.class, new Integer(1));
    sampleMap.put(int.class, new Integer(1));
    sampleMap.put(Long.class, new Long(1));
    sampleMap.put(long.class, new Long(1));
    sampleMap.put(Double.class, new Double(1));
    sampleMap.put(double.class, new Double(1));
    sampleMap.put(Float.class, new Float(1));
    sampleMap.put(float.class, new Float(1));
    sampleMap.put(Short.class, new Short((short)1));
    sampleMap.put(short.class, new Short((short)1));
    sampleMap.put(Character.class, new Integer(1));
    sampleMap.put(char.class, new Integer(1));
    sampleMap.put(Date.class, new Date());
    sampleMap.put(boolean.class, new Boolean(true));
    sampleMap.put(Boolean.class, new Boolean(true));
    sampleMap.put(BigDecimal.class, new BigDecimal("1.00"));
    sampleMap.put(BigInteger.class, new BigInteger("1"));
    sampleMap.put(List.class, new ArrayList());
    sampleMap.put(List.class, new ArrayList());
    if (!sampleMap.containsKey(type)) {
      try {
        sampleMap.put(type, type.newInstance());
      } catch (Exception e) {
        // ignore on purpose...we don't care if this fails
      }
    }
    return sampleMap.get(type);
  }

}

